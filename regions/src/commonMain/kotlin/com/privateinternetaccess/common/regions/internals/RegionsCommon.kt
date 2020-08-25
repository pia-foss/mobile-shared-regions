package com.privateinternetaccess.common.regions.internals

import com.privateinternetaccess.common.regions.PingRequest
import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
import com.privateinternetaccess.common.regions.RegionsAPI
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.common.regions.MessageVerificator
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.common.regions.model.TranslationsGeoResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.coroutines.CoroutineContext

public class RegionsCommon(
        private val pingDependency: PingRequest,
        private val messageVerificator: MessageVerificator
) : RegionsAPI, CoroutineScope {

    companion object {
        private const val ENDPOINT = "https://serverlist.piaservers.net/vpninfo/servers/new"
        private const val REQUEST_TIMEOUT_MS = 5000L
    }

    private enum class RegionsState {
        IDLE,
        REQUESTING
    }

    private data class RegionEndpointInformation(
            val region: String,
            val name: String,
            val iso: String,
            val dns: String,
            val protocol: String,
            val endpoint: String,
            val portForwarding: Boolean
    )

    private val json = Json(JsonConfiguration(ignoreUnknownKeys = true))
    private val client = HttpClient() {
        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
        }
    }
    private var knownRegionsResponse: RegionsResponse? = null
    private var state = RegionsState.IDLE

    // region CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    // endregion
    override fun fetchLocalization(callback: (response: TranslationsGeoResponse?, error: Error?) -> Unit) {
        if (state == RegionsState.REQUESTING) {
            callback(null, Error("Request already in progress"))
            return
        }
        state = RegionsState.REQUESTING
        runBlocking {
            fetchLocalizationAsync(callback)
        }
    }

    override fun fetchRegions(callback: (response: RegionsResponse?, error: Error?) -> Unit) {
        if (state == RegionsState.REQUESTING) {
            callback(knownRegionsResponse, Error("Request already in progress"))
            return
        }
        state = RegionsState.REQUESTING
        runBlocking {
            fetchRegionsAsync(callback)
        }
    }

    override fun pingRequests(
            protocol: RegionsProtocol,
            callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        if (state == RegionsState.REQUESTING) {
            callback(emptyList(), Error("Request already in progress"))
            return
        }
        state = RegionsState.REQUESTING
        runBlocking {
            pingRequestsAsync(protocol, callback)
        }
    }
    // endregion

    // region Private
    private fun pingRequestsAsync(
        protocol: RegionsProtocol,
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) = launch(Dispatchers.Default) {
        handlePingRequest(protocol, callback)
    }

    private fun fetchLocalizationAsync(callback: (response: TranslationsGeoResponse?, error: Error?) -> Unit) = launch {
        val completionCallback: (TranslationsGeoResponse?, Error?) -> Unit = { response: TranslationsGeoResponse?, error: Error? ->
            launch {
                withContext(Dispatchers.Main) {
                    state = RegionsState.IDLE
                    callback(response, error)
                }
            }
        }

        val response = client.getCatching<Pair<String?, Exception?>> {
            url("http://164.90.215.141:8088/translations")
            header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVmM2QzNWExYjY3ODZiMDAxM2Y2MTJmNiIsImlhdCI6MTU5Nzg0Njk0OH0.WT5r40obfxkohs3867eu2w8wKwPyVzgo5i3-NAWuPPA")
        }
        response.first?.let {
            val serializedLocalization = json.parse(TranslationsGeoResponse.serializer(), it)
            completionCallback(serializedLocalization, null)
        }
        response.second?.let {
            completionCallback(null, Error(it.message))
        }
    }

    private fun fetchRegionsAsync(callback: (response: RegionsResponse?, error: Error?) -> Unit) = launch {
        val completionCallback: (RegionsResponse?, Error?) -> Unit = { response: RegionsResponse?, error: Error? ->
            launch {
                withContext(Dispatchers.Main) {
                    state = RegionsState.IDLE
                    callback(response, error)
                }
            }
        }

        val response = client.getCatching<Pair<String?, Exception?>> {
            url(ENDPOINT)
        }
        response.first?.let {
            handleFetchRegionsResponse(it, completionCallback)
        }
        response.second?.let {
            completionCallback(null, Error(it.message))
        }
    }

    public suspend fun handleFetchRegionsResponse(
        response: String,
        callback: (response: RegionsResponse?, error: Error?) -> Unit
    ) {
        val responseList = response.split("\n\n")
        val json = responseList.first()
        val key = responseList.last()

        var error: Error? = null
        if (messageVerificator.verifyMessage(json, key)) {
            knownRegionsResponse = serializeRegions(json)
        } else {
            error = Error("Invalid signature")
        }

        withContext(Dispatchers.Main) {
            state = RegionsState.IDLE
            callback(knownRegionsResponse, error)
        }
    }

    public fun serializeRegions(jsonResponse: String) =
            json.parse(RegionsResponse.serializer(), jsonResponse)

    public suspend fun handlePingRequest(
        protocol: RegionsProtocol,
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        var error: Error? = null
        var response = listOf<RegionLowerLatencyInformation>()
        knownRegionsResponse?.let {
            response = requestEndpointsLowerLatencies(protocol, it)
        } ?: run {
            error = Error("Unknown regions")
        }

        withContext(Dispatchers.Main) {
            state = RegionsState.IDLE
            callback(response, error)
        }
    }

    private fun requestEndpointsLowerLatencies(
            protocol: RegionsProtocol,
            regionsResponse: RegionsResponse
    ): List<RegionLowerLatencyInformation> {
        val endpointsToPing = mutableMapOf<String, List<String>>()
        val lowerLatencies = mutableListOf<RegionLowerLatencyInformation>()

        val allKnownEndpointsDetails = flattenEndpointsInformation(protocol, regionsResponse)
        for ((region, regionEndpointInformation) in allKnownEndpointsDetails) {
            val regionEndpoints = mutableListOf<String>()
            regionEndpointInformation.forEach {
                regionEndpoints.add(it.endpoint)
            }
            endpointsToPing[region] = regionEndpoints
        }

        pingDependency.platformPingRequest(endpointsToPing) { latencyResults ->
            for ((region, results) in latencyResults) {
                results.minBy { it.latency }?.let { minEndpointLatency ->
                    allKnownEndpointsDetails[region]?.let { allKnownEndpointsDetails ->
                        allKnownEndpointsDetails.firstOrNull {
                            it.endpoint == minEndpointLatency.endpoint
                        }?.let { minEndpointLatencyDetails ->
                            lowerLatencies.add(RegionLowerLatencyInformation(
                                    minEndpointLatencyDetails.region,
                                    minEndpointLatencyDetails.endpoint,
                                    minEndpointLatency.latency
                            ))
                        }
                    }
                }
            }
        }
        return lowerLatencies
    }

    private fun flattenEndpointsInformation(
            protocol: RegionsProtocol,
            response: RegionsResponse
    ): Map<String, List<RegionEndpointInformation>> {
        val result = mutableMapOf<String, MutableList<RegionEndpointInformation>>()
        response.regions.forEach { region ->
            region.servers[protocol.protocol]?.forEach { regionServerProtocol ->
                if (result[region.id] == null) {
                    result[region.id] = mutableListOf()
                }
                result[region.id]?.add(RegionEndpointInformation(
                        region.id,
                        region.name,
                        region.country,
                        region.dns,
                        protocol.protocol,
                        regionServerProtocol.ip,
                        region.portForward
                ))
            }
        }
        return result
    }
    // endregion

    // region HttpClient extensions
    private suspend inline fun <reified T> HttpClient.getCatching(
            block: HttpRequestBuilder.() -> Unit = {}
    ): Pair<String?, Exception?> {
        var exception: Exception? = null
        var response: String? = null
        try {
            response = request {
                method = HttpMethod.Get
                apply(block)
            }
        } catch (e: Exception) {
            exception = e
        } catch (throwable: Throwable) {
            // Temporary catch of throwable. Waiting for Ktor's release with Exceptions.
            exception = Exception(throwable.message)
        }
        return Pair(response, exception)
    }
    // endregion
}