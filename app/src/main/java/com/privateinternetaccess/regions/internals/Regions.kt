package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
import com.privateinternetaccess.common.regions.RegionsAPI
import com.privateinternetaccess.common.regions.RegionsCommonBuilder
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.regions.internals.handlers.PingHandler
import com.privateinternetaccess.regions.internals.handlers.MessageVerificationHandler

class Regions : RegionsAPI {

    private val regions: RegionsAPI = RegionsCommonBuilder()
        .setPingRequestDependency(PingHandler())
        .setMessageVerificatorDependency(MessageVerificationHandler())
        .build()

    // region RegionsAPI
    override fun fetch(
        callback: (response: RegionsResponse?, error: Error?) -> Unit
    ) {
        regions.fetch(callback)
    }

    override fun pingRequests(
        protocol: RegionsProtocol,
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        regions.pingRequests(protocol, callback)
    }
    // endregion
}