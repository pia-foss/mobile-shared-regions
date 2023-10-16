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
import com.privateinternetaccess.regions.model.TranslationsGeoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext


internal expect object RegionHttpClient {

    /**
     * @param certificate String?. Certificate required for pinning capabilities.
     * @param pinnedEndpoint Pair<String, String>?. Contains endpoint as first,
     * commonName as second.
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
     * @param callback Map<String, List<Pair<String, Long>>>. Key: Region.
     * List<Pair<String, Long>>>: Endpoints and latencies within the region.
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

public class Regions internal constructor(
    private val userAgent: String,
    private val regionsListRequestPath: String,
    private val metadataRequestPath: String,
    private val regionJsonFallback: RegionJsonFallback?,
    private val endpointsProvider: IRegionEndpointProvider,
    private val certificate: String?,
    private val inMemory: RegionsCacheDataSource,
    private val persistence: RegionsCacheDataSource
) : RegionsAPI, CoroutineScope {

    companion object {
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
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = false }

    // region CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    // endregion

    // region RegionsCommonAPI
    override fun fetchRegions(
        locale: String,
        callback: (response: RegionsResponse?, error: List<Error>) -> Unit
    ) {
        data class FetchRegionsResult(
            val response: RegionsResponse?,
            val error: List<Error>
        )

        launch {
            val deferred: CompletableDeferred<FetchRegionsResult> = CompletableDeferred()
            fetchLocalizationAsync(
                endpointsProvider.regionEndpoints()
            ) { metadataResponse, metadataErrors ->
                if (metadataErrors.isNotEmpty()) {
                    deferred.complete(
                        FetchRegionsResult(response = null, error = metadataErrors)
                    )
                    return@fetchLocalizationAsync
                }
                if (metadataResponse == null) {
                    deferred.complete(
                        FetchRegionsResult(
                            response = null,
                            error = listOf(Error("Invalid metadata response"))
                        )
                    )
                    return@fetchLocalizationAsync
                }

                val (metadataMessage, metadataKey) =
                    processResponseIntoMessageAndKey(metadataResponse)
                if (!MessageVerificator.verifyMessage(metadataMessage, metadataKey)) {
                    deferred.complete(
                        FetchRegionsResult(
                            response = null,
                            error = listOf(Error("Invalid signature on metadata response"))
                        )
                    )
                    return@fetchLocalizationAsync
                }

                fetchRegionsAsync(
                    endpointsProvider.regionEndpoints()
                ) { regionsResponse, regionsErrors ->
                    if (regionsErrors.isNotEmpty()) {
                        deferred.complete(
                            FetchRegionsResult(response = null, error = regionsErrors)
                        )
                        return@fetchRegionsAsync
                    }
                    if (regionsResponse == null) {
                        deferred.complete(
                            FetchRegionsResult(
                                response = null,
                                error = listOf(Error("Invalid regions response"))
                            )
                        )
                        return@fetchRegionsAsync
                    }

                    val (regionsMessage, regionsKey) =
                        processResponseIntoMessageAndKey(regionsResponse)
                    if (!MessageVerificator.verifyMessage(regionsMessage, regionsKey)) {
                        deferred.complete(
                            FetchRegionsResult(
                                response = null,
                                error = listOf(Error("Invalid signature on regions response"))
                            )
                        )
                        return@fetchRegionsAsync
                    }

                    val knownRegionsResponse: RegionsResponse? =
                        decodeRegions(metadataMessage, regionsMessage, locale)
                    if (knownRegionsResponse == null) {
                        deferred.complete(
                            FetchRegionsResult(
                                response = null,
                                error = listOf(Error("There was an issue decoding data"))
                            )
                        )
                        return@fetchRegionsAsync
                    }

                    inMemory.saveRegion(locale, knownRegionsResponse)
                    persistence.saveRegion(locale, knownRegionsResponse)
                    deferred.complete(
                        FetchRegionsResult(response =
                        knownRegionsResponse, error = emptyList())
                    )
                }
            }

            val result: FetchRegionsResult = deferred.await()
            if (result.response != null && result.error.isEmpty()) {
                withContext(Dispatchers.Main) {
                    callback(result.response, result.error)
                }
            } else {
                val knownRegionsResponse: RegionsResponse? = inMemory.getRegion(locale).getOrElse {
                    persistence.getRegion(locale).getOrElse {
                        fallbackResponseIfPossible(locale)
                    }
                }
                withContext(Dispatchers.Main) {
                    if(knownRegionsResponse != null) {
                        callback(knownRegionsResponse, emptyList())
                    } else {
                        callback(result.response, result.error)
                    }
                }
            }
        }
    }

    override fun pingRequests(
        callback: (response: List<RegionLowerLatencyInformation>, error: List<Error>) -> Unit
    ) {
        launch {
            pingRequestsAsync(
                callback = callback
            )
        }
    }
    // endregion

    // region private
    private fun fallbackResponseIfPossible(locale: String): RegionsResponse? {
        if (regionJsonFallback == null) {
            return null
        }

        return decodeRegions(
            regionJsonFallback.metadataJson,
            regionJsonFallback.regionsJson,
            locale
        )
    }

    private fun decodeRegions(
        metadataJson: String,
        regionsJson: String,
        locale: String
    ): RegionsResponse? {

        val regionsResponse: RegionsResponse?
        val translationsGeoResponse: TranslationsGeoResponse?

        try {
            regionsResponse =
                json.decodeFromString(RegionsResponse.serializer(), regionsJson)
            translationsGeoResponse =
                json.decodeFromString(TranslationsGeoResponse.serializer(), metadataJson)
        } catch (exception: SerializationException) {
            return null
        }

        val localizedRegions = mutableListOf<RegionsResponse.Region>()
        for (region in regionsResponse.regions) {
            val regionTranslations = translationsForRegion(region.name, translationsGeoResponse)
            if (regionTranslations == null) {
                localizedRegions.add(region)
                continue
            }
            var regionTranslation = regionTranslationForLocale(locale, regionTranslations)
            if (regionTranslation.isNullOrEmpty()) {
                regionTranslation = region.name
            }
            var updatedRegion = region.copy(name = regionTranslation)
            val regionCoordinates = coordinatesForRegionId(region.id, translationsGeoResponse)
            if (regionCoordinates != null) {
                updatedRegion = updatedRegion.copy(
                    latitude = regionCoordinates.first,
                    longitude = regionCoordinates.second
                )
            }
            localizedRegions.add(updatedRegion)
        }
        return regionsResponse.copy(regions = localizedRegions)
    }

    private fun translationsForRegion(
        region: String,
        translationsGeoResponse: TranslationsGeoResponse
    ): Map<String, String>? {
        for ((regionName, regionTranslations) in translationsGeoResponse.translations) {
            if (regionName.equals(region, ignoreCase = true)) {
                return regionTranslations
            }
        }
        return null
    }

    private fun regionTranslationForLocale(
        targetLocale: String,
        regionTranslations: Map<String, String>
    ): String? {
        for ((locale, translation) in regionTranslations) {

            // Try with a match of the locale.
            if (locale.equals(targetLocale, ignoreCase = true)) {
                return translation
            }

            // Try with the language only. Regardless of the country.
            val localeLanguage = if (targetLocale.contains("-")) {
                targetLocale.split("-").first()
            } else {
                targetLocale
            }
            if (locale.startsWith(localeLanguage, ignoreCase = true)) {
                return translation
            }
        }
        return null
    }

    private fun coordinatesForRegionId(
        targetRegionId: String,
        translationsGeoResponse: TranslationsGeoResponse
    ): Pair<String, String>? {
        for ((regionId, coordinates) in translationsGeoResponse.gps) {
            if (targetRegionId.equals(regionId, ignoreCase = true)) {
                return Pair(coordinates[0], coordinates[1])
            }
        }
        return null
    }

    private fun processResponseIntoMessageAndKey(response: String): Pair<String, String> {
        val responseList = response.split("\n\n")
        val message = responseList.first()
        val key = responseList.last()
        return Pair(message, key)
    }

    private fun pingRequestsAsync(
        callback: (response: List<RegionLowerLatencyInformation>, error: List<Error>) -> Unit
    ) = async {
        handlePingRequest(
            callback = callback
        )
    }

    private fun fetchRegionsAsync(
        endpoints: List<RegionEndpoint>,
        callback: (response: String?, error: List<Error>) -> Unit
    ) = launch {
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
                RegionHttpClient.client(
                    certificate,
                    Pair(endpoint.endpoint, endpoint.certificateCommonName!!)
                )
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
                url("https://${endpoint.endpoint}$regionsListRequestPath")
            }

            response.first?.let { httpResponse ->
                try {
                    if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                        listErrors.add(
                            Error("${httpResponse.status.value} ${httpResponse.status.description}")
                        )
                    } else {
                        requestResponse = httpResponse.bodyAsText()
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

            // If there were no errors in the request for the current endpoint.
            // No need to try the next endpoint.
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
    ) = launch {
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
                RegionHttpClient.client(
                    certificate,
                    Pair(endpoint.endpoint, endpoint.certificateCommonName!!)
                )
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
                url("https://${endpoint.endpoint}$metadataRequestPath")
            }

            response.first?.let { httpResponse ->
                try {
                    if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                        listErrors.add(
                            Error("${httpResponse.status.value} ${httpResponse.status.description}")
                        )
                    } else {
                        requestResponse = httpResponse.bodyAsText()
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

            // If there were no errors in the request for the current endpoint.
            // No need to try the next endpoint.
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
        val knownRegionsResponse: RegionsResponse? = inMemory.getRegion(
            locale = null
        ).recoverCatching {
            persistence.getRegion(locale = null).getOrThrow()
        }.getOrNull()

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
                if (results.isEmpty()) {
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

    private fun flattenEndpointsInformation(
        response: RegionsResponse
    ): Map<String, List<RegionEndpointInformation>> {
        val result = mutableMapOf<String, MutableList<RegionEndpointInformation>>()
        response.regions.forEach { region ->
            region.servers[RegionsProtocol.META.protocol]?.forEach { regionServerProtocol ->
                if (result[region.id] == null) {
                    result[region.id] = mutableListOf()
                }
                result[region.id]?.add(
                    RegionEndpointInformation(
                        region = region.id,
                        name = region.name,
                        iso = region.country,
                        dns = region.dns,
                        protocol = RegionsProtocol.META.protocol,
                        endpoint = regionServerProtocol.ip,
                        portForwarding = region.portForward
                    )
                )
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