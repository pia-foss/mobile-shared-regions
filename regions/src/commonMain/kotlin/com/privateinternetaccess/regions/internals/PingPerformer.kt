package com.privateinternetaccess.regions.internals

internal expect class PingPerformer() {

    /**
     * @param endpoints Map<String, String>. Key: Region. String: Endpoint to ping in the region.
     * @param callback Map<String, Long>. Key: Region. Value: Latency.
     */
    fun pingEndpoints(
        endpoints: Map<String, String>,
        callback: (result: Map<String, Long>) -> Unit
    )
}
