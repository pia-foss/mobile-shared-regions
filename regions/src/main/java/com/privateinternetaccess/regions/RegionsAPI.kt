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