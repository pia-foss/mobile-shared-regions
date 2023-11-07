package com.privateinternetaccess.regions.internals

internal expect class PingPerformer() {

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
