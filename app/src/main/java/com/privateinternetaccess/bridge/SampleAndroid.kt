package com.privateinternetaccess.bridge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.privateinternetaccess.bridge.handlers.MessageVerificationHandler
import com.privateinternetaccess.bridge.handlers.PingHandler
import com.privateinternetaccess.regions.RegionsBuilder
import com.privateinternetaccess.regions.RegionsProtocol
import sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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