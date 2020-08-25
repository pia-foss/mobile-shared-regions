package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.common.regions.RegionLowerLatencyInformation
import com.privateinternetaccess.common.regions.RegionsAPI
import com.privateinternetaccess.common.regions.RegionsCommonBuilder
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.common.regions.model.TranslationsGeoResponse
import com.privateinternetaccess.regions.internals.handlers.PingRequestHandler
import com.privateinternetaccess.regions.internals.handlers.MessageVerificationHandler

class Regions : RegionsAPI {

    private val regions: RegionsAPI = RegionsCommonBuilder()
        .setPingRequestDependency(PingRequestHandler())
        .setMessageVerificatorDependency(MessageVerificationHandler())
        .build()

    override fun fetchLocalization(callback: (response: TranslationsGeoResponse?, error: Error?) -> Unit) {
        regions.fetchLocalization(callback)
    }
    override fun fetchRegions(callback: (response: RegionsResponse?, error: Error?) -> Unit) {
        regions.fetchRegions(callback)
    }

    override fun pingRequests(
        protocol: RegionsProtocol,
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        regions.pingRequests(protocol, callback)
    }
    // endregion
}