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

package com.privateinternetaccess.tests

import com.privateinternetaccess.common.regions.RegionsCommonBuilder
import com.privateinternetaccess.regions.internals.handlers.MessageVerificationHandler
import com.privateinternetaccess.regions.internals.handlers.PingRequestHandler
import kotlin.test.*


class RegionsAPITests {

    @Test
    fun testUndefinedDependencies() {
        assertFailsWith(Exception::class) {
            RegionsCommonBuilder().build()
        }
    }

    @Test
    fun testUndefinedPingRequestDependency() {
        assertFailsWith(Exception::class) {
            RegionsCommonBuilder().setMessageVerificatorDependency(MessageVerificationHandler()).build()
        }
    }

    @Test
    fun testUndefinedMessageVerificatorDependency() {
        assertFailsWith(Exception::class) {
            RegionsCommonBuilder().setPingRequestDependency(PingRequestHandler()).build()
        }
    }

    @Test
    fun testDefinedDependencies() {
        // Testing lack of exceptions
        RegionsCommonBuilder()
            .setClientStateProvider(RegionsTest.GenericRegionStateProvider())
            .setMessageVerificatorDependency(MessageVerificationHandler())
            .setPingRequestDependency(PingRequestHandler())
            .build()
    }
}