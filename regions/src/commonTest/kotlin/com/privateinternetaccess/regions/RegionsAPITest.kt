package com.privateinternetaccess.regions

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

import com.privateinternetaccess.regions.internals.utils.MockEndpointProvider
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFailsWith


class RegionsAPITest {

    @Test
    fun `Test missing mandatory all information`() {
        assertFailsWith(Exception::class) {
            RegionsBuilder()
                .build()
        }
    }

    @Test
    fun `Test missing endpoint provider information`() {
        assertFailsWith(Exception::class) {
            RegionsBuilder()
                .setCertificate("test-certificate")
                .setUserAgent("test-user-agent")
                .build()
        }
    }

    @Test
    fun `Test missing user agent information`() {
        assertFailsWith(Exception::class) {
            RegionsBuilder()
                .setEndpointProvider(MockEndpointProvider())
                .setCertificate("test-certificate")
                .build()
        }
    }

    @Test
    fun `Test missing regions request path information`() {
        assertFailsWith(Exception::class) {
            RegionsBuilder()
                .setEndpointProvider(MockEndpointProvider())
                .setCertificate("test-certificate")
                .setUserAgent("test-user-agent")
                .setMetadataRequestPath("metadata-request-path")
                .build()
        }
    }

    @Test
    fun `Test missing metadata request path information`() {
        assertFailsWith(Exception::class) {
            RegionsBuilder()
                .setEndpointProvider(MockEndpointProvider())
                .setCertificate("test-certificate")
                .setUserAgent("test-user-agent")
                .setRegionsListRequestPath("regions-list-request-path")
                .build()
        }
    }

    @Ignore
    fun `Test missing certificate information should succeed`() {
        // We are testing the lack of exception as certificate is optional.
        RegionsBuilder()
            .setEndpointProvider(MockEndpointProvider())
            .setUserAgent("test-user-agent")
            .build()
    }


    @Ignore
    fun `Test setting all information should succeed`() {
        // We are testing the lack of exception as we are setting all information.
        RegionsBuilder()
            .setEndpointProvider(MockEndpointProvider())
            .setCertificate("test-certificate")
            .setUserAgent("test-user-agent")
            .setRegionsListRequestPath("regions-list-request-path")
            .setMetadataRequestPath("metadata-request-path")
            .build()
    }
}