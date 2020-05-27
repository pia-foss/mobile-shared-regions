package com.privateinternetaccess.regions.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RegionsResponse(
        @SerialName("groups")
        val groups: Map<String, List<ProtocolDetails>> = mutableMapOf(),
        @SerialName("regions")
        val regions: List<Region> = listOf()
) {
    @Serializable
    data class ProtocolDetails(
        @SerialName("name")
         val name: String = "",
        @SerialName("ports")
        val ports: List<Int> = listOf()
    )

    @Serializable
    data class Region(
            @SerialName("id")
            val id: String = "",
            @SerialName("name")
            val name: String = "",
            @SerialName("country")
            val country: String = "",
            @SerialName("auto_region")
            val autoRegion: Boolean = false,
            @SerialName("port_forward")
            val portForward: Boolean = true,
            @SerialName("dns")
            val dns: String = "",
            @SerialName("servers")
            val servers: Map<String, List<ServerDetails>> = mutableMapOf()
    ) {
        @Serializable
        data class ServerDetails(
            @SerialName("ip")
            val ip: String = "",
            @SerialName("cn")
            val cn: String = ""
        )
    }
}