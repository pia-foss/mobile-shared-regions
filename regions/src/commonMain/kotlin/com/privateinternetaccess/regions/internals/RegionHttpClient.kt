package com.privateinternetaccess.regions.internals

import io.ktor.client.HttpClient


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
