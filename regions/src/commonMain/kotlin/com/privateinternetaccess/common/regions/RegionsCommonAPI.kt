/*
 *  Copyright (c) 2020 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Mobile Client.
 *
 *  The Private Internet Access Mobile Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Mobile Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Mobile Client.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.privateinternetaccess.common.regions

import com.privateinternetaccess.common.regions.internals.RegionsCommon
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.common.regions.model.TranslationsGeoResponse


public enum class RegionsProtocol(val protocol: String) {
    OPENVPN_TCP("ovpntcp"),
    OPENVPN_UDP("ovpnudp"),
    WIREGUARD("wg"),
    META("meta")
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
     * @param callback `(response: RegionsResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun pingRequests(callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit)
}

/**
 * Interface defining the client's data provider.
 */
public interface RegionClientStateProvider {

    /**
     * It returns the list of endpoints to try to reach when performing a request. Order is relevant.
     *
     * @return `List<RegionEndpoint>`
     */
    fun regionEndpoints(): List<RegionEndpoint>
}

/**
 * Builder class responsible for creating an instance of an object conforming to
 * the `RegionsAPI` interface.
 */
public class RegionsCommonBuilder {
    private var clientStateProvider: RegionClientStateProvider? = null
    private var pingRequestDependency: PingRequest? = null
    private var messageVerificatorDependency: MessageVerificator? = null

    fun setClientStateProvider(clientStateProvider: RegionClientStateProvider): RegionsCommonBuilder =
        apply { this.clientStateProvider = clientStateProvider }

    fun setPingRequestDependency(pingRequestDependency: PingRequest): RegionsCommonBuilder =
        apply { this.pingRequestDependency = pingRequestDependency }

    fun setMessageVerificatorDependency(messageVerificatorDependency: MessageVerificator): RegionsCommonBuilder =
        apply { this.messageVerificatorDependency = messageVerificatorDependency }

    /**
     * @return `RegionsAPI` instance.
     */
    fun build(): RegionsCommonAPI {
        val clientStateProvider = this.clientStateProvider
            ?: throw Exception("Client state provider missing.")
        val pingDependency = pingRequestDependency
            ?: throw Exception("Essential ping request dependency missing.")
        val messageVerificator = messageVerificatorDependency
            ?: throw Exception("Essential message verification dependency missing.")
        return RegionsCommon(clientStateProvider, pingDependency, messageVerificator)
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

/**
 * Data class defining the endpoints data needed when performing a request on it.
 */
public data class RegionEndpoint(
    val endpoint: String,
    val isProxy: Boolean,
    val usePinnedCertificate: Boolean = false,
    val certificateCommonName: String? = null
)
