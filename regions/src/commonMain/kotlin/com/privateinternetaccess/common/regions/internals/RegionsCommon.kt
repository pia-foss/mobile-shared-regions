package com.privateinternetaccess.common.regions.internals

import com.privateinternetaccess.common.regions.PingRequest
import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
import com.privateinternetaccess.common.regions.RegionsAPI
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.common.regions.MessageVerificator
import com.privateinternetaccess.common.regions.model.RegionsResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.request.get
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

    // region RegionsAPI
    override fun fetch(
            callback: (response: RegionsResponse?, error: Error?) -> Unit
    ) = runBlocking {
        if (state == RegionsState.REQUESTING) {
            callback(knownRegionsResponse, Error("Request already in progress"))
            return@runBlocking
        }
        state = RegionsState.REQUESTING
        fetchAsync(callback)
        return@runBlocking
    }

    override fun pingRequests(
            protocol: RegionsProtocol,
            callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) = runBlocking {
        if (state == RegionsState.REQUESTING) {
            callback(emptyList(), Error("Request already in progress"))
            return@runBlocking
        }
        state = RegionsState.REQUESTING
        pingRequestsAsync(protocol, callback)
        return@runBlocking
    }
    // endregion

    // region Private
    private fun pingRequestsAsync(
        protocol: RegionsProtocol,
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) = launch(Dispatchers.Default) {
        handlePingRequest(protocol, callback)
    }

    private fun fetchAsync(
            callback: (response: RegionsResponse?, error: Error?) -> Unit
    ) = launch {
        handleFetchResponse(client.get(ENDPOINT), callback)
    }

    public suspend fun handleFetchResponse(
        response: String,
        callback: (response: RegionsResponse?, error: Error?) -> Unit
    ) {
        val responseList = response.split("\n\n")
        val json = responseList.first()
        val key = responseList.last()

        var error: Error? = null
        if (messageVerificator.verifyMessage(json, key)) {
            knownRegionsResponse = serialize(json)
        } else {
            error = Error("Invalid signature")
        }

        withContext(Dispatchers.Main) {
            state = RegionsState.IDLE
            callback(knownRegionsResponse, error)
        }
    }

    public fun serialize(jsonResponse: String) =
            Json(JsonConfiguration(
                    ignoreUnknownKeys = true
            )).parse(RegionsResponse.serializer(), jsonResponse)

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
}