package com.privateinternetaccess.common.regions.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationsGeoResponse(
    @SerialName("country_groups")
    val countryGroups: Map<String, String>, // e.g. {"de": "Germany", "au": "Australia"}
    @SerialName("gps")
    val gps: Map<String, List<String>>, // e.g. {"ae": ["24.474796", "54.370576"]]}
    @SerialName("translations")
    val translations: Map<String, Map<String, String>> // e.g. {"Germany": { "fr": ""}}
)