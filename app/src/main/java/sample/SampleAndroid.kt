package sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.privateinternetaccess.regions.PingRequest
import com.privateinternetaccess.regions.RegionsFactory
import com.privateinternetaccess.regions.RegionsProtocol

class MainActivity : AppCompatActivity(), PingRequest {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val region = RegionsFactory.create(this)
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
}