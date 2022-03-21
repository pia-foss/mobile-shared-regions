package com.privateinternetaccess.regions.internals

/*
 *  Copyright (c) 2020 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Mobile Client.
 *
 *  The Private Internet Access Mobile Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Mobile Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Mobile Client.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.privateinternetaccess.regions.*
import com.privateinternetaccess.regions.model.RegionsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


internal expect object RegionsArtifact {

    /**
     * Bootstrap the artifacts before accessing the decoding or other functionalities. e.g. load libraries, etc.
     */
    fun bootstrap()

    /**
     * @param regionsMetadataJson String. Json string response coming from the metadata endpoint.
     * @param regionsJson String. Json string response coming from the regions endpoint.
     * @param locale String. Client's locale.
     *
     * @return `RegionsResponse`.
     */
    fun decodeRegions(
        regionsMetadataJson: String,
        regionsJson: String,
        locale: String
    ): RegionsResponse
}

internal expect object RegionHttpClient {

    /**
     * @param certificate String?. Certificate required for pinning capabilities.
     * @param pinnedEndpoint Pair<String, String>?. Contains endpoint as first, commonName as second.
     *
     * @return `Pair<HttpClient?, Exception?>`.
     */
    fun client(
        certificate: String? = null,
        pinnedEndpoint: Pair<String, String>? = null
    ): Pair<HttpClient?, Exception?>
}

expect class PingPerformer() {

    /**
     * @param endpoints Map<String, List<String>>. Key: Region. List<String>: Endpoints within the
     * region.
     * @param callback Map<String, List<Pair<String, Long>>>. Key: Region. List<Pair<String, Long>>>: Endpoints and
     * latencies within the region.
     */
    fun pingEndpoints(
        endpoints: Map<String, List<String>>,
        callback: (result: Map<String, List<Pair<String, Long>>>) -> Unit
    )
}

expect object MessageVerificator {

    /**
     * @param message String. Message to verify.
     * @param key String. Verification key.
     */
    fun verifyMessage(message: String, key: String): Boolean
}

public class Regions(
    private val userAgent: String,
    private val endpointsProvider: IRegionEndpointProvider,
    private val certificate: String?
) : RegionsAPI, CoroutineScope {

    companion object {
        private const val LOCALIZATION_ENDPOINT = "/vpninfo/regions/v2"
        private const val REGIONS_ENDPOINT = "/vpninfo/servers/v6"
        internal const val REQUEST_TIMEOUT_MS = 6000L
        internal const val PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzLYHwX5Ug/oUObZ5eH5P\n" +
                "rEwmfj4E/YEfSKLgFSsyRGGsVmmjiXBmSbX2s3xbj/ofuvYtkMkP/VPFHy9E/8ox\n" +
                "Y+cRjPzydxz46LPY7jpEw1NHZjOyTeUero5e1nkLhiQqO/cMVYmUnuVcuFfZyZvc\n" +
                "8Apx5fBrIp2oWpF/G9tpUZfUUJaaHiXDtuYP8o8VhYtyjuUu3h7rkQFoMxvuoOFH\n" +
                "6nkc0VQmBsHvCfq4T9v8gyiBtQRy543leapTBMT34mxVIQ4ReGLPVit/6sNLoGLb\n" +
                "gSnGe9Bk/a5V/5vlqeemWF0hgoRtUxMtU1hFbe7e8tSq1j+mu0SHMyKHiHd+OsmU\n" +
                "IQIDAQAB\n" +
                "-----END PUBLIC KEY-----"
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

    private val pingPerformer = PingPerformer()
    private var knownRegionsResponse: RegionsResponse? = null

    init {
        RegionsArtifact.bootstrap()
    }

    // region CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    // endregion

    // region RegionsCommonAPI
    override fun fetchRegions(locale: String, callback: (response: RegionsResponse?, error: List<Error>) -> Unit) {
        launch {
            fetchLocalizationAsync(endpointsProvider.regionEndpoints()) { metadataResponse, metadataErrors ->
                if (metadataErrors.isNotEmpty()) {
                    callback(null, metadataErrors)
                    return@fetchLocalizationAsync
                }
                if (metadataResponse == null) {
                    callback(null, listOf(Error("Invalid metadata response")))
                    return@fetchLocalizationAsync
                }

                val (metadataMessage, metadataKey) = processResponseIntoMessageAndKey(metadataResponse)
                if (!MessageVerificator.verifyMessage(metadataMessage, metadataKey)) {
                    callback(null, listOf(Error("Invalid signature on metadata response")))
                    return@fetchLocalizationAsync
                }

                fetchRegionsAsync(endpointsProvider.regionEndpoints()) { regionsResponse, regionsErrors ->
                    if (regionsErrors.isNotEmpty()) {
                        callback(null, regionsErrors)
                        return@fetchRegionsAsync
                    }
                    if (regionsResponse == null) {
                        callback(null, listOf(Error("Invalid regions response")))
                        return@fetchRegionsAsync
                    }

                    val (regionsMessage, regionsKey) = processResponseIntoMessageAndKey(regionsResponse)
                    if (!MessageVerificator.verifyMessage(regionsMessage, regionsKey)) {
                        callback(null, listOf(Error("Invalid signature on regions response")))
                        return@fetchRegionsAsync
                    }

                    knownRegionsResponse = RegionsArtifact.decodeRegions(metadataMessage, regionsMessage, locale)
                    callback(knownRegionsResponse, emptyList())
                }
            }
        }
    }

    override fun pingRequests(callback: (response: List<RegionLowerLatencyInformation>, error: List<Error>) -> Unit) {
        launch {
            pingRequestsAsync(callback)
        }
    }
    // endregion

    // region private
    private fun processResponseIntoMessageAndKey(response: String): Pair<String, String> {
        val responseList = response.split("\n\n")
        val message = responseList.first()
        val key = responseList.last()
        return Pair(message, key)
    }

    private fun pingRequestsAsync(
        callback: (response: List<RegionLowerLatencyInformation>, error: List<Error>) -> Unit
    ) = async {
        handlePingRequest(callback)
    }

    private fun fetchRegionsAsync(
        endpoints: List<RegionEndpoint>,
        callback: (response: String?, error: List<Error>) -> Unit
    ) = async {
        var requestResponse: String? = null
        val listErrors: MutableList<Error> = mutableListOf()
        if (endpoints.isNullOrEmpty()) {
            listErrors.add(Error("No available endpoints to perform the request"))
        }

        for (endpoint in endpoints) {
            if (endpoint.usePinnedCertificate && certificate.isNullOrEmpty()) {
                listErrors.add(Error("No available certificate for pinning purposes"))
                continue
            }

            val httpClientConfigResult = if (endpoint.usePinnedCertificate) {
                RegionHttpClient.client(certificate, Pair(endpoint.endpoint, endpoint.certificateCommonName!!))
            } else {
                RegionHttpClient.client()
            }

            val httpClient = httpClientConfigResult.first
            val httpClientError = httpClientConfigResult.second
            if (httpClientError != null) {
                listErrors.add(Error(httpClientError.message))
                continue
            }

            if (httpClient == null) {
                listErrors.add(Error("Invalid http client"))
                continue
            }

            var succeeded = false
            val response = httpClient.getCatching<Pair<HttpResponse?, Exception?>> {
                url("https://${endpoint.endpoint}$REGIONS_ENDPOINT")
            }

            response.first?.let { httpResponse ->
                try {
                    if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                        listErrors.add(Error("${httpResponse.status.value} ${httpResponse.status.description}"))
                    } else {
                        requestResponse = httpResponse.receive<String>()
                        succeeded = true
                    }
                } catch (exception: NoTransformationFoundException) {
                    listErrors.add(Error("600 - Unexpected response transformation: $exception"))
                } catch (exception: DoubleReceiveException) {
                    listErrors.add(Error("600 - Request receive already invoked: $exception"))
                }
            }
            response.second?.let {
                listErrors.add(Error(it.message))
            }

            // If there were no errors in the request for the current endpoint. No need to try the next endpoint.
            if (succeeded) {
                listErrors.clear()
                break
            }
        }

        withContext(Dispatchers.Main) {
            callback(requestResponse, listErrors)
        }
    }

    private fun fetchLocalizationAsync(
        endpoints: List<RegionEndpoint>,
        callback: (response: String?, error: List<Error>) -> Unit
    ) = async {
        var requestResponse: String? = null
        val listErrors: MutableList<Error> = mutableListOf()
        if (endpoints.isEmpty()) {
            listErrors.add(Error("No available endpoints to perform the request"))
        }

        for (endpoint in endpoints) {
            if (endpoint.usePinnedCertificate && certificate.isNullOrEmpty()) {
                listErrors.add(Error("No available certificate for pinning purposes"))
                continue
            }

            val httpClientConfigResult = if (endpoint.usePinnedCertificate) {
                RegionHttpClient.client(certificate, Pair(endpoint.endpoint, endpoint.certificateCommonName!!))
            } else {
                RegionHttpClient.client()
            }

            val httpClient = httpClientConfigResult.first
            val httpClientError = httpClientConfigResult.second
            if (httpClientError != null) {
                listErrors.add(Error(httpClientError.message))
                continue
            }

            if (httpClient == null) {
                listErrors.add(Error("Invalid http client"))
                continue
            }

            var succeeded = false
            val response = httpClient.getCatching<Pair<HttpResponse?, Exception?>> {
                url("https://${endpoint.endpoint}$LOCALIZATION_ENDPOINT")
            }

            response.first?.let { httpResponse ->
                try {
                    if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                        listErrors.add(Error("${httpResponse.status.value} ${httpResponse.status.description}"))
                    } else {
                        requestResponse = httpResponse.receive<String>()
                        succeeded = true
                    }
                } catch (exception: NoTransformationFoundException) {
                    listErrors.add(Error("600 - Unexpected response transformation: $exception"))
                } catch (exception: DoubleReceiveException) {
                    listErrors.add(Error("600 - Request receive already invoked: $exception"))
                }
            }
            response.second?.let {
                listErrors.add(Error(it.message))
            }

            // If there were no errors in the request for the current endpoint. No need to try the next endpoint.
            if (succeeded) {
                listErrors.clear()
                break
            }
        }

        withContext(Dispatchers.Main) {
            callback(requestResponse, listErrors)
        }
    }

    private fun handlePingRequest(
        callback: (response: List<RegionLowerLatencyInformation>, error: List<Error>) -> Unit
    ) {
        knownRegionsResponse?.let {
            requestEndpointsLowerLatencies(it) { response ->
                launch(Dispatchers.Main) {
                    callback(response, emptyList())
                }
            }
        } ?: run {
            launch(Dispatchers.Main) {
                callback(emptyList(), listOf(Error("Unknown regions")))
            }
        }
    }

    private fun requestEndpointsLowerLatencies(
        regionsResponse: RegionsResponse,
        callback: (response: List<RegionLowerLatencyInformation>) -> Unit
    ) {
        val endpointsToPing = mutableMapOf<String, List<String>>()
        val lowerLatencies = mutableListOf<RegionLowerLatencyInformation>()

        val allKnownEndpointsDetails = flattenEndpointsInformation(regionsResponse)
        for ((region, regionEndpointInformation) in allKnownEndpointsDetails) {
            val regionEndpoints = mutableListOf<String>()
            regionEndpointInformation.forEach {
                regionEndpoints.add(it.endpoint)
            }
            endpointsToPing[region] = regionEndpoints
        }

        pingPerformer.pingEndpoints(endpointsToPing) { latencyResults ->
            for ((region, results) in latencyResults) {
                if (results.isNullOrEmpty()) {
                    continue
                }

                results.minByOrNull { it.second }?.let { minEndpointLatency ->
                    allKnownEndpointsDetails[region]?.let { allKnownEndpointsDetails ->
                        allKnownEndpointsDetails.firstOrNull {
                            it.endpoint == minEndpointLatency.first
                        }?.let { minEndpointLatencyDetails ->
                            lowerLatencies.add(RegionLowerLatencyInformation(
                                minEndpointLatencyDetails.region,
                                minEndpointLatencyDetails.endpoint,
                                minEndpointLatency.second
                            ))
                        }
                    }
                }
            }
            callback(lowerLatencies)
        }
    }

    private fun flattenEndpointsInformation(response: RegionsResponse): Map<String, List<RegionEndpointInformation>> {
        val result = mutableMapOf<String, MutableList<RegionEndpointInformation>>()
        response.regions.forEach { region ->
            region.servers[RegionsProtocol.META.protocol]?.forEach { regionServerProtocol ->
                if (result[region.id] == null) {
                    result[region.id] = mutableListOf()
                }
                result[region.id]?.add(RegionEndpointInformation(
                    region.id,
                    region.name,
                    region.country,
                    region.dns,
                    RegionsProtocol.META.protocol,
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
    ): Pair<HttpResponse?, Exception?> {
        var exception: Exception? = null
        var response: HttpResponse? = null
        try {
            response = request {
                method = HttpMethod.Get
                userAgent(userAgent)
                apply(block)
            }
        } catch (e: Exception) {
            exception = e
        }
        return Pair(response, exception)
    }
    // endregion
}