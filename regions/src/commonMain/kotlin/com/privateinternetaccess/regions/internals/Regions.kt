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
import com.privateinternetaccess.regions.model.ShadowsocksRegionsResponse
import com.privateinternetaccess.regions.model.VpnRegionsResponse
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


public class Regions internal constructor(
    private val userAgent: String,
    private val vpnRegionsRequestPath: String,
    private val shadowsocksRegionsRequestPath: String,
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
    override fun fetchVpnRegions(
        locale: String,
        callback: (response: VpnRegionsResponse?, error: Error?) -> Unit
    ) {
        launch {
            requestVpnRegions(locale = locale).fold(
                onSuccess = { vpnRegionsResponse ->
                    inMemory.saveVpnRegions(locale, vpnRegionsResponse)
                    persistence.saveVpnRegions(locale, vpnRegionsResponse)
                    withContext(Dispatchers.Main) {
                        callback(vpnRegionsResponse, null)
                    }
                },
                onFailure = { decodeVpnRegionsFailure ->
                    val knownVpnRegionsResponse =
                        inMemory.getVpnRegions(locale).getOrElse {
                            persistence.getVpnRegions(locale).getOrElse {
                                fallbackVpnRegionsResponseIfPossible(locale).getOrNull()
                            }
                        }
                    withContext(Dispatchers.Main) {
                        knownVpnRegionsResponse?.let {
                            callback(it, null)
                        } ?: run {
                            callback(null, Error(decodeVpnRegionsFailure))
                        }
                    }
                }
            )
        }
    }

    override fun fetchShadowsocksRegions(
        locale: String,
        callback: (response: List<ShadowsocksRegionsResponse>, error: Error?) -> Unit
    ) {
        launch {
            requestShadowsocksRegions(locale = locale).fold(
                onSuccess = { shadowsocksRegionsResponse ->
                    inMemory.saveShadowsocksRegions(locale, shadowsocksRegionsResponse)
                    persistence.saveShadowsocksRegions(locale, shadowsocksRegionsResponse)
                    withContext(Dispatchers.Main) {
                        callback(shadowsocksRegionsResponse, null)
                    }
                },
                onFailure = { decodeShadowsocksRegionsFailure ->
                    val knownShadowsocksRegionsResponse =
                        inMemory.getShadowsocksRegions(locale).getOrElse {
                            persistence.getShadowsocksRegions(locale).getOrElse {
                                fallbackShadowsocksRegionsResponseIfPossible(locale).getOrNull()
                            }
                        }
                    withContext(Dispatchers.Main) {
                        knownShadowsocksRegionsResponse?.let {
                            callback(it, null)
                        } ?: run {
                            callback(emptyList(), Error(decodeShadowsocksRegionsFailure))
                        }
                    }
                }
            )
        }
    }

    override fun pingRequests(
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        launch {
            pingRequestsAsync(callback = callback)
        }
    }
    // endregion

    // region private
    private suspend fun requestVpnRegions(locale: String): Result<VpnRegionsResponse> {
        val metadataRequestResponse = performRequest(
            endpoints = endpointsProvider.regionEndpoints(),
            requestPath = metadataRequestPath,
        )

        val metadataResult = processResponseIntoMessageWithKeyAndVerifyIntegrity(
            response = metadataRequestResponse.getOrNull()
        )

        val vpnRegionsRequestResponse = performRequest(
            endpoints = endpointsProvider.regionEndpoints(),
            requestPath = vpnRegionsRequestPath,
        )

        val vpnRegionsResult = processResponseIntoMessageWithKeyAndVerifyIntegrity(
            response = vpnRegionsRequestResponse.getOrNull()
        )

        return decodeVpnRegions(
            metadataJson = metadataResult.getOrNull(),
            vpnRegionsJson = vpnRegionsResult.getOrNull(),
            locale = locale
        )
    }

    private suspend fun requestShadowsocksRegions(
        locale: String
    ): Result<List<ShadowsocksRegionsResponse>> {
        val vpnRegionsResponse = requestVpnRegions(locale = locale)

        val shadowsocksRegionsRequestResponse = performRequest(
            endpoints = endpointsProvider.regionEndpoints(),
            requestPath = shadowsocksRegionsRequestPath,
        )

        val shadowsocksRegionsResult = processResponseIntoMessageWithKeyAndVerifyIntegrity(
            response = shadowsocksRegionsRequestResponse.getOrNull()
        )

        return decodeShadowsocksRegions(
            vpnRegionsResponse = vpnRegionsResponse.getOrNull(),
            shadowsocksRegionsJson = shadowsocksRegionsResult.getOrNull(),
        )
    }

    private fun processResponseIntoMessageWithKeyAndVerifyIntegrity(
        response: String?
    ): Result<String> {
        if (response == null) {
            return Result.failure(Error("Invalid response"))
        }

        val (message, key) = processResponseIntoMessageAndKey(response)
        return if (MessageVerificator.verifyMessage(message, key)) {
            Result.success(message)
        } else {
            Result.failure(Error("Invalid signature on response"))
        }
    }

    private fun fallbackVpnRegionsResponseIfPossible(
        locale: String
    ): Result<VpnRegionsResponse> {
        if (regionJsonFallback == null) {
            return Result.failure(Error("Unknown fallback response"))
        }
        if (regionJsonFallback.vpnRegionsJson.isEmpty()) {
            return Result.failure(Error("Invalid vpn regions json fallback"))
        }

        return decodeVpnRegions(
            metadataJson = regionJsonFallback.metadataJson,
            vpnRegionsJson = regionJsonFallback.vpnRegionsJson,
            locale = locale
        )
    }

    private fun fallbackShadowsocksRegionsResponseIfPossible(
        locale: String
    ): Result<List<ShadowsocksRegionsResponse>> {
        val fallbackVpnRegionsResponse = fallbackVpnRegionsResponseIfPossible(locale = locale)
        if (fallbackVpnRegionsResponse.isFailure) {
            return Result.failure(Error("Unknown fallback regions response"))
        }

        if (regionJsonFallback == null) {
            return Result.failure(Error("Unknown fallback response"))
        }
        if (regionJsonFallback.shadowsocksRegionsJson.isEmpty()) {
            return Result.failure(Error("Invalid shadowsocks regions json fallback"))
        }

        return decodeShadowsocksRegions(
            vpnRegionsResponse = fallbackVpnRegionsResponse.getOrNull(),
            shadowsocksRegionsJson = regionJsonFallback.shadowsocksRegionsJson,
        )
    }

    private fun decodeVpnRegions(
        metadataJson: String?,
        vpnRegionsJson: String?,
        locale: String
    ): Result<VpnRegionsResponse> {

        if (vpnRegionsJson.isNullOrEmpty()) {
            return Result.failure(Error("Invalid vpn regions json"))
        }

        if (metadataJson.isNullOrEmpty()) {
            return Result.failure(Error("Invalid metadata json"))
        }

        val vpnRegionsResponse: VpnRegionsResponse
        val translationsGeoResponse: TranslationsGeoResponse

        try {
            vpnRegionsResponse = json.decodeFromString(vpnRegionsJson)
            translationsGeoResponse = json.decodeFromString( metadataJson)
        } catch (exception: SerializationException) {
            return Result.failure(exception)
        }

        val localizedRegions = mutableListOf<VpnRegionsResponse.Region>()
        for (region in vpnRegionsResponse.regions) {
            val regionTranslations = translationsForVpnRegion(region.name, translationsGeoResponse)
            if (regionTranslations == null) {
                localizedRegions.add(region)
                continue
            }
            var regionTranslation = vpnRegionTranslationForLocale(locale, regionTranslations)
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
        return Result.success(vpnRegionsResponse.copy(regions = localizedRegions))
    }

    private fun decodeShadowsocksRegions(
        vpnRegionsResponse: VpnRegionsResponse?,
        shadowsocksRegionsJson: String?,
    ): Result<List<ShadowsocksRegionsResponse>> {

        if (shadowsocksRegionsJson.isNullOrEmpty()) {
            return Result.failure(Error("Invalid shadowsocks regions json"))
        }

        if (vpnRegionsResponse == null) {
            return Result.failure(Error("Invalid regions response"))
        }

        val shadowsocksRegionsResponse: Array<ShadowsocksRegionsResponse>

        try {
            shadowsocksRegionsResponse = json.decodeFromString(shadowsocksRegionsJson)
        } catch (exception: SerializationException) {
            return Result.failure(exception)
        }

        val localizedRegions = mutableListOf<ShadowsocksRegionsResponse>()
        for (region in shadowsocksRegionsResponse) {
            shadowsocksRegionTranslation(region.region, vpnRegionsResponse)?.let { translation ->
                shadowsocksRegionIso(region.region, vpnRegionsResponse)?.let { iso ->
                    localizedRegions.add(region.copy(iso = iso, region = translation))
                }
            }
        }
        return Result.success(localizedRegions)
    }

    private fun translationsForVpnRegion(
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

    private fun vpnRegionTranslationForLocale(
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

    private fun shadowsocksRegionTranslation(
        shadowsocksRegion: String,
        vpnRegionsResponse: VpnRegionsResponse
    ): String? {
        for (region in vpnRegionsResponse.regions) {
            if (region.id.equals(shadowsocksRegion, ignoreCase = true)) {
                return region.name
            }
        }
        return null
    }

    private fun shadowsocksRegionIso(
        shadowsocksRegion: String,
        vpnRegionsResponse: VpnRegionsResponse
    ): String? {
        for (region in vpnRegionsResponse.regions) {
            if (region.id.equals(shadowsocksRegion, ignoreCase = true)) {
                return region.country
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
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) = async {
        handlePingRequest(
            callback = callback
        )
    }

    private suspend fun performRequest(
        endpoints: List<RegionEndpoint>,
        requestPath: String,
    ): Result<String> {
        var requestResponse: String? = null
        var error: Error? = null
        if (endpoints.isEmpty()) {
            error = Error("No available endpoints to perform the request")
        }

        for (endpoint in endpoints) {
            if (endpoint.usePinnedCertificate && certificate.isNullOrEmpty()) {
                error = Error(Error("No available certificate for pinning purposes"))
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
                error = Error(Error(httpClientError.message))
                continue
            }

            if (httpClient == null) {
                error = Error(Error("Invalid http client"))
                continue
            }

            var succeeded = false
            httpClient.getCatching<Pair<HttpResponse?, Exception?>> {
                url("https://${endpoint.endpoint}$requestPath")
            }.fold(
                onSuccess = { httpResponse ->
                    try {
                        if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                            error = Error(
                                "${httpResponse.status.value} ${httpResponse.status.description}"
                            )
                        } else {
                            requestResponse = httpResponse.bodyAsText()
                            succeeded = true
                        }
                    } catch (exception: NoTransformationFoundException) {
                        error = Error("Unexpected response transformation: $exception")
                    } catch (exception: DoubleReceiveException) {
                        error = Error("Request receive already invoked: $exception")
                    }
                },
                onFailure = {
                    error = Error(it.message)
                }
            )

            // If there were no errors in the request for the current endpoint.
            // No need to try the next endpoint.
            if (succeeded) {
                error = null
                break
            }
        }

        return requestResponse?.let {
            Result.success(it)
        } ?: run {
            Result.failure(Error(error))
        }
    }

    private fun handlePingRequest(
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        val knownRegionsResponse: VpnRegionsResponse? = inMemory.getVpnRegions(
            locale = null
        ).recoverCatching {
            persistence.getVpnRegions(locale = null).getOrThrow()
        }.getOrNull()

        knownRegionsResponse?.let {
            requestEndpointsLowerLatencies(it) { response ->
                launch(Dispatchers.Main) {
                    callback(response, null)
                }
            }
        } ?: run {
            launch(Dispatchers.Main) {
                callback(emptyList(), Error("Unknown regions"))
            }
        }
    }

    private fun requestEndpointsLowerLatencies(
        regionsResponse: VpnRegionsResponse,
        callback: (response: List<RegionLowerLatencyInformation>) -> Unit
    ) {
        val endpointsToPing = mutableMapOf<String, String>()
        val latencies = mutableListOf<RegionLowerLatencyInformation>()

        val allKnownEndpointsDetails = flattenEndpointsInformation(regionsResponse)
        for ((region, regionEndpointInformation) in allKnownEndpointsDetails) {
            endpointsToPing[region] = regionEndpointInformation.endpoint
        }

        pingPerformer.pingEndpoints(endpointsToPing) { latencyResults ->
            for ((region, latency) in latencyResults) {
                latencies.add(RegionLowerLatencyInformation(region, latency))
            }
            callback(latencies)
        }
    }

    private fun flattenEndpointsInformation(
        response: VpnRegionsResponse
    ): Map<String, RegionEndpointInformation> {
        val result = mutableMapOf<String, RegionEndpointInformation>()
        response.regions.forEach { region ->
            region.servers[RegionsProtocol.META.protocol]?.firstOrNull()?.let {
                result[region.id] = RegionEndpointInformation(
                    region = region.id,
                    name = region.name,
                    iso = region.country,
                    dns = region.dns,
                    protocol = RegionsProtocol.META.protocol,
                    endpoint = it.ip,
                    portForwarding = region.portForward
                )
            }
        }
        return result
    }
    // endregion

    // region HttpClient extensions
    private suspend inline fun <reified T> HttpClient.getCatching(
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<HttpResponse> =
        try {
            val response = request {
                method = HttpMethod.Get
                userAgent(userAgent)
                apply(block)
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    // endregion
}
