package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
import com.privateinternetaccess.common.regions.RegionsCommonAPI
import com.privateinternetaccess.common.regions.RegionsCommonBuilder
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.common.regions.model.TranslationsGeoResponse
import com.privateinternetaccess.regions.RegionsAPI
import com.privateinternetaccess.regions.internals.handlers.PingRequestHandler
import com.privateinternetaccess.regions.internals.handlers.MessageVerificationHandler
import java.util.*

class Regions : RegionsAPI {

    private var knownLocalizationResponse: TranslationsGeoResponse? = null
    private val regions: RegionsCommonAPI = RegionsCommonBuilder()
        .setPingRequestDependency(PingRequestHandler())
        .setMessageVerificatorDependency(MessageVerificationHandler())
        .build()

    // region RegionsAPI
    override fun fetchRegions(locale: String, callback: (response: RegionsResponse?, error: Error?) -> Unit) {
        fetchLocalizationIfNeeded { localizationError ->
            if (localizationError != null) {
                callback(null, localizationError)
                return@fetchLocalizationIfNeeded
            }
            regions.fetchRegions { regionsResponse, regionsError ->
                if (regionsError != null) {
                    callback(null, regionsError)
                    return@fetchRegions
                }
                if (regionsResponse == null) {
                    callback(null, Error("Invalid regions response"))
                    return@fetchRegions
                }
                callback(localizeRegionsResponse(locale, regionsResponse), null)
            }
        }
    }

    override fun pingRequests(
        protocol: RegionsProtocol,
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        regions.pingRequests(protocol, callback)
    }
    // endregion

    // region private
    private fun fetchLocalizationIfNeeded(callback: (error: Error?) -> Unit) {
        if (knownLocalizationResponse != null) {
            callback(null)
            return
        }

        regions.fetchLocalization { localizationResponse, localizationError ->
            if (localizationError != null) {
                callback(localizationError)
                return@fetchLocalization
            }
            knownLocalizationResponse = localizationResponse
            callback(null)
        }
    }

    private fun localizeRegionsResponse(
        locale: String,
        regionsResponse: RegionsResponse
    ): RegionsResponse {
        val localizedRegions = mutableListOf<RegionsResponse.Region>()
        for (region in regionsResponse.regions) {
            val regionTranslations = translationsForRegion(region.name)
            if (regionTranslations == null) {
                localizedRegions.add(region)
                continue
            }
            val regionTranslation = regionTranslationForLocale(locale, regionTranslations)
            if (regionTranslation == null) {
                localizedRegions.add(region)
                continue
            }
            var updatedRegion = region.copy(name = regionTranslation)
            val regionCoordinates = coordinatesForRegionId(region.id)
            if (regionCoordinates != null) {
                updatedRegion = updatedRegion.copy(latitude = regionCoordinates.first, longitude = regionCoordinates.second)
            }
            localizedRegions.add(updatedRegion)
        }
        return regionsResponse.copy(regions = localizedRegions)
    }

    private fun translationsForRegion(region: String): Map<String, String>? {
        val knownTranslations = knownLocalizationResponse?.translations ?: return null

        for ((regionName, regionTranslations) in knownTranslations) {
            if (regionName.toLowerCase(Locale.getDefault()) == region.toLowerCase(Locale.getDefault())) {
                return regionTranslations
            }
        }
        return null
    }

    private fun regionTranslationForLocale(targetLocale: String, regionTranslations: Map<String, String>): String? {
        for ((locale, translation) in regionTranslations) {
            if (locale.toLowerCase(Locale.getDefault()) == targetLocale.toLowerCase(Locale.getDefault())) {
                return translation
            }
        }
        return null
    }

    private fun coordinatesForRegionId(targetRegionId: String): Pair<String, String>? {
        val knownCoordinates = knownLocalizationResponse?.gps ?: return null

        for ((regionId, coordinates) in knownCoordinates) {
            if (targetRegionId.toLowerCase(Locale.getDefault()) == regionId.toLowerCase(Locale.getDefault())) {
                return Pair(coordinates[0], coordinates[1])
            }
        }
        return null
    }
    // endregion
}