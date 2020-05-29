package sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.privateinternetaccess.regions.PingRequest
import com.privateinternetaccess.regions.MessageVerificator
import com.privateinternetaccess.regions.RegionsBuilder
import com.privateinternetaccess.regions.RegionsProtocol

class MainActivity : AppCompatActivity(), PingRequest, MessageVerificator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val region = RegionsBuilder().setPingRequestDependency(this).setMessageVerificatorDependency(this).build()
        region.fetch { response, error ->
            println("PIAAndroid. fetch response: $response error: $error")

            region.pingRequests(RegionsProtocol.OPENVPN_UDP) { response, error ->
                println("PIAAndroid. ping response: $response error: $error")
            }
        }
    }

    override fun platformPingRequest(
        endpoints: Map<String, List<String>>,
        callback: (result: Map<String, List<PingRequest.PlatformPingResult>>) -> Unit
    ) {
        println("PIAAndroid. Ping everyone!")
        callback(emptyMap())
    }

    override fun verifyMessage(message: String, key: String): Boolean {
        println("PIAAndroid. Verified!")
        return true
    }
}