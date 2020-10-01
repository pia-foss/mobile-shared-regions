package com.privateinternetaccess.regions

import com.privateinternetaccess.common.regions.RegionClientStateProvider
import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
import com.privateinternetaccess.common.regions.RegionsCommonBuilder
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.regions.internals.Regions

/**
 * Const defining the timeout being used on the ping requests
 */
public const val REGIONS_PING_TIMEOUT: Int = 1500

/**
 * Interface defining the API to be offered by the common module.
 */
public interface RegionsAPI {

    /**
     * Fetch all regions information for next-gen.
     *
     * @param locale `String`. Regions locale. If unknown defaults to en-us.
     * @param callback `(response: RegionsResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun fetchRegions(locale: String, callback: (response: RegionsResponse?, error: Error?) -> Unit)

    /**
     * Starts the process of ping requests and return the updated `ServerResponse` object as a
     * callback parameter.
     *
     * @param callback `(response: RegionsResponse?, error: Error?)`. Invoked on the main thread.
     */
    fun pingRequests(callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit)
}

/**
 * Builder class responsible for creating an instance of an object conforming to
 * the `RegionsAPI` interface.
 */
public class RegionsBuilder {
    private var clientStateProvider: RegionClientStateProvider? = null

    fun setClientStateProvider(clientStateProvider: RegionClientStateProvider): RegionsBuilder =
            apply { this.clientStateProvider = clientStateProvider }

    /**
     * @return `RegionsAPI` instance.
     */
    fun build(): RegionsAPI {
        val clientStateProvider = this.clientStateProvider
                ?: throw Exception("Client state provider missing.")
        return Regions(clientStateProvider)
    }
}