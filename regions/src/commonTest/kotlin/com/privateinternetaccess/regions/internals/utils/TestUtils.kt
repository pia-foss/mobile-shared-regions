package com.privateinternetaccess.regions.internals.utils

/*
 *  Copyright (c) 2021 Private Internet Access, Inc.
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

import com.privateinternetaccess.regions.IRegionEndpointProvider
import com.privateinternetaccess.regions.RegionEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext


internal expect class TestUtils() {
    fun bootstrap()
    fun teardown()
    fun runBlockingTest(block: suspend CoroutineScope.()-> Unit)
    val testCoroutineContext: CoroutineContext
}

internal class MockEndpointProvider : IRegionEndpointProvider {

    private var endpoints = listOf<RegionEndpoint>()

    override fun regionEndpoints(): List<RegionEndpoint> {
        return endpoints
    }
}