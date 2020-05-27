package com.privateinternetaccess.regions

import com.privateinternetaccess.regions.internals.Regions
import com.privateinternetaccess.regions.model.RegionsResponse

public enum class RegionsProtocol(val protocol: String) {
    OPENVPN_TCP("ovpntcp"),
    OPENVPN_UDP("ovpnudp"),
    WIREGUARD("wg")
}

/**
 * Interface defining the API to be offered by the module.
 */
public interface RegionsAPI {

    /**
     * Fetch all servers information on GEN4.
     *
     * @param callback `(response: RegionsResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun fetch(callback: (response: RegionsResponse?, error: Error?) -> Unit)

    /**
     * Starts the process of ping requests and return the updated `ServerResponse` object as a
     * callback parameter.
     *
     * @param protocol `RegionsProtocol`. The protocol the application is working with.
     * @param callback `(response: RegionsResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun pingRequests(
            protocol: RegionsProtocol,
            callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    )
}

/**
 * Factory class creating an instance of an object conforming to the `RegionsAPI` interface.
 */
public class RegionsFactory {

    /**
     * @return `RegionsAPI` instance.
     */
    companion object {
        fun create(pingDependency: PingRequest): RegionsAPI = Regions(pingDependency)
    }
}

/**
 * Class defining the information needed for the platform to perform a ping request.
 * Platform specific request.
 */
public interface PingRequest {

    /**
     * @param endpoints Map<String, List<String>>. Key: Region. List<String>: Endpoints within the
     * region.
     * @param callback Map<String, List<PlatformPingResult>>. Key: Region. List<PlatformPingResult>:
     * Endpoint and latencies within the region.
     */
    fun platformPingRequest(
            endpoints: Map<String, List<String>>,
            callback: (result: Map<String, List<PlatformPingResult>>) -> Unit
    )

    /**
     * @param endpoint String. Endpoint.
     * @param latency Long. Latency to the endpoint.
     */
    data class PlatformPingResult(val endpoint: String, val latency: Long)
}

/**
 * Data class defining the response object for a ping request. @see `fun pingRequests(...)`.
 *
 * @param region `String`.
 * @param endpoint `String`.
 * @param latency `String`.
 */
public data class RegionLowerLatencyInformation(
        val region: String,
        val endpoint: String,
        val latency: Long
)