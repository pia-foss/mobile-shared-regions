package com.privateinternetaccess.regions

import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
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
public class RegionsBuilder {

    /**
     * @return `RegionsAPI` instance.
     */
    fun build(): RegionsAPI {
        return Regions()
    }
}