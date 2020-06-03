package com.privateinternetaccess.regions

import com.privateinternetaccess.common.regions.RegionsBuilder
import com.privateinternetaccess.common.regions.RegionsProtocol
import com.privateinternetaccess.regions.handlers.MessageVerificationHandler
import com.privateinternetaccess.regions.handlers.PingHandler

class SampleAndroid {

    fun helloWorld() {
        val region = RegionsBuilder()
            .setPingRequestDependency(PingHandler())
            .setMessageVerificatorDependency(MessageVerificationHandler())
            .build()
        region.fetch { fetchResponse, fetchError ->
            println("PIAAndroid. fetch response: $fetchResponse error: $fetchError")

            region.pingRequests(RegionsProtocol.OPENVPN_UDP) { pingResponse, pingError ->
                println("PIAAndroid. ping response: $pingResponse error: $pingError")
            }
        }
    }
}