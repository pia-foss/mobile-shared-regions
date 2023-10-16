package com.privateinternetaccess.regions.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TranslationsGeoResponse(
    @SerialName("country_groups")
    val countryGroups: Map<String, String>,
    @SerialName("gps")
    val gps: Map<String, List<String>>,
    @SerialName("translations")
    val translations: Map<String, Map<String, String>>
)
