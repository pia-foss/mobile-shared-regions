package com.privateinternetaccess.common.regions

import com.privateinternetaccess.common.regions.internals.RegionsCommon
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.common.regions.model.TranslationsGeoResponse

public enum class RegionsProtocol(val protocol: String) {
    OPENVPN_TCP("ovpntcp"),
    OPENVPN_UDP("ovpnudp"),
    WIREGUARD("wg")
}

/**
 * Interface defining the API to be offered by the common module.
 */
public interface RegionsCommonAPI {

    /**
     * Fetch all regions localization information for next-gen.
     *
     * @param callback `(response: TranslationsGeoResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun fetchLocalization(callback: (response: TranslationsGeoResponse?, error: Error?) -> Unit)

    /**
     * Fetch all regions information for next-gen.
     *
     * @param callback `(response: RegionsResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun fetchRegions(callback: (response: RegionsResponse?, error: Error?) -> Unit)

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
 * Builder class responsible for creating an instance of an object conforming to
 * the `RegionsAPI` interface.
 */
public class RegionsCommonBuilder {
    private var pingRequestDependency: PingRequest? = null
    private var messageVerificatorDependency: MessageVerificator? = null

    fun setPingRequestDependency(pingRequestDependency: PingRequest): RegionsCommonBuilder =
            apply { this.pingRequestDependency = pingRequestDependency }

    fun setMessageVerificatorDependency(messageVerificatorDependency: MessageVerificator): RegionsCommonBuilder =
            apply { this.messageVerificatorDependency = messageVerificatorDependency }

    /**
     * @return `RegionsAPI` instance.
     */
    fun build(): RegionsCommonAPI {
        val pingDependency = pingRequestDependency
                ?: throw Exception("Essential ping request dependency missing.")
        val messageVerificator = messageVerificatorDependency
                ?: throw Exception("Essential message verification dependency missing.")
        return RegionsCommon(pingDependency, messageVerificator)
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
 * Class defining the logic for the key message verification.
 */
public interface MessageVerificator {

    /**
     * @param message String. Message to verify.
     * @param key String. Verification key.
     */
    fun verifyMessage(message: String, key: String): Boolean
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
