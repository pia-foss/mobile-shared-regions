package com.privateinternetaccess.regions.internals.handlers

import com.privateinternetaccess.common.regions.PingRequest
import com.privateinternetaccess.common.regions.PingRequest.PlatformPingResult
import com.privateinternetaccess.regions.REGIONS_PING_TIMEOUT
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis
import java.net.InetAddress
import java.io.IOException

internal class PingRequestHandler : PingRequest {

    override fun platformPingRequest(
        endpoints: Map<String, List<String>>,
        callback: (result: Map<String, List<PlatformPingResult>>) -> Unit
    ) = runBlocking {
        val result = mutableMapOf<String, List<PlatformPingResult>>()
        val requests = async {
            for ((region, endpointsInRegion) in endpoints) {
                val regionEndpointsResults = mutableListOf<PlatformPingResult>()
                endpointsInRegion.forEach {
                    var error: Error? = null
                    var latency = measureTimeMillis {
                        error = ping(it)
                    }
                    latency = error?.let {
                        println(error)
                        0L
                    } ?: latency
                    regionEndpointsResults.add(PlatformPingResult(it, latency))
                    result[region] = regionEndpointsResults
                }
            }
        }
        requests.await()
        callback(result)
    }

    // region private
    private fun ping(endpoint: String): Error? {
        var error: Error? = null
        try {
            InetAddress.getByName(endpoint).isReachable(REGIONS_PING_TIMEOUT)
        } catch (e: IOException) {
            e.printStackTrace()
            error = Error("Error reaching endpoint: $endpoint exception: $e")
        }
        return error
    }
    // endregion
}