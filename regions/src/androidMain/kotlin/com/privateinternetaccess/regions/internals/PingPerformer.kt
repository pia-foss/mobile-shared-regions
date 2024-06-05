package com.privateinternetaccess.regions.internals

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

import com.privateinternetaccess.regions.REGIONS_PING_TIMEOUT
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.Collections
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis


internal actual class PingPerformer : CoroutineScope {

    companion object {
        private const val REGIONS_PING_PORT = 443
    }

    // region CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    // endregion

    actual fun pingEndpoints(
        endpoints: Map<String, String>,
        callback: (result: Map<String, Long>) -> Unit
    ) {
        async {
            val syncResult = Collections.synchronizedMap(mutableMapOf<String, Long>())
            val requests: MutableList<Job> = mutableListOf()
            for ((region, endpointInRegion) in endpoints) {
                requests.add(async(Dispatchers.IO) {
                    var error: Error?
                    var latency = measureTimeMillis {
                        error = ping(endpointInRegion)
                    }
                    latency = error?.let {
                        REGIONS_PING_TIMEOUT.toLong()
                    } ?: latency
                    syncResult[region] = latency
                })
            }
            requests.joinAll()
            launch(Dispatchers.Main) { callback(syncResult) }
        }
    }

    // region private
    private fun ping(endpoint: String): Error? {
        var error: Error? = null
        try {
            val socket = Socket()
            socket.tcpNoDelay = true
            socket.connect(InetSocketAddress(endpoint, REGIONS_PING_PORT), REGIONS_PING_TIMEOUT)
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            error = Error("Error reaching endpoint: $endpoint exception: $e")
        }
        return error
    }
    // endregion
}