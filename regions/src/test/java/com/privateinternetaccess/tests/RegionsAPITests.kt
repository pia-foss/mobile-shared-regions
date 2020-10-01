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