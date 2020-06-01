package com.privateinternetaccess.bridge.handlers

import com.privateinternetaccess.regions.PingRequest
import com.privateinternetaccess.regions.PingRequest.PlatformPingResult
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis
import java.net.InetAddress
import java.io.IOException

private class PingHandler : PingRequest {

    companion object {
        const val TIMEOUT = 3000
    }

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
            InetAddress.getByName(endpoint).isReachable(TIMEOUT)
        } catch (e: IOException) {
            e.printStackTrace()
            error = Error("Error reaching endpoint: $endpoint exception: $e")
        }
        return error
    }
    // endregion
}