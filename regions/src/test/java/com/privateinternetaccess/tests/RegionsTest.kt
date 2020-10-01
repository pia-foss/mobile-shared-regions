package com.privateinternetaccess.tests

import com.privateinternetaccess.common.regions.*
import com.privateinternetaccess.common.regions.internals.RegionsCommon
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.*

@ExperimentalCoroutinesApi
class RegionsTest {

    companion object {
        private const val MSG_RESPONSE = "{\"groups\": {\"ovpntcp\": [{\"name\": \"openvpn_tcp\", \"ports\": [11]}], \"ovpnudp\": [{\"name\": \"openvpn_udp\", \"ports\": [11]}], \"wg\": [{\"name\": \"wireguard\", \"ports\": [11]}], \"ikev2\": [{\"name\": \"ikev2\", \"ports\": [11]}], \"proxysocks\": [{\"name\": \"socks\", \"ports\": [11]}], \"proxyss\": [{\"name\": \"shadowsocks\", \"ports\": [11]}]}, \"regions\": [{\"id\": \"us_california\", \"name\": \"US California\", \"country\": \"US\", \"auto_region\": true, \"dns\": \"127.0.0.1\", \"port_forward\": false, \"servers\": {\"ovpnudp\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"ovpntcp\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"ikev2\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"wg\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"meta\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}]}}]}"
        private const val KEY_RESPONSE = "Xe42XDfjsshe9QcwrolEjkx4Fp4YFqSKqfT7HoVPuaxdc+2FTy+hs93WbjJ0\n" +
                "9SxvqZ2hxwNJ2e/jl0YHmIYQqgAc2euhn5a0OPKRHIKWHOteP7ydaTdSz5Nw\n" +
                "1MczbCO5PcvTpvbr38678ATPSbt0CCQqIeL2WqRy5j8vb8jIBHJMXLslqdv7\n" +
                "t4OW8JvjfjZjPHsxqc5FrHU2EUWM+9cvcHNsoKF+CvKxeq1tAahnaq7Kw5Xi\n" +
                "igJxzzIRXMdjw3qqOIlK7IRO/20lslDkaK3ga8eRY+A4/xf5pDp3F4QOK5KR\n" +
                "0xz9BQYrZU+uLdQDyC1DDLhodJXevLF1nTPxO4PQJg=="
        private const val RESPONSE = "$MSG_RESPONSE\n\n$KEY_RESPONSE"
    }

    private val dispatcher = TestCoroutineDispatcher()

    class GenericRegionStateProvider : RegionClientStateProvider {
        override fun regionEndpoints(): List<RegionEndpoint> {
            return listOf(RegionEndpoint("serverlist.piaservers.net",
                isProxy = false,
                usePinnedCertificate = false,
                certificateCommonName = null
            ))
        }
    }

    class GenericPingHandlerTest : PingRequest {
        override fun platformPingRequest(
            endpoints: Map<String, List<String>>,
            callback: (result: Map<String, List<PingRequest.PlatformPingResult>>) -> Unit
        ) {
            callback(emptyMap())
        }
    }

    class GenericMessageVerificatorTest : MessageVerificator {
        override fun verifyMessage(message: String, key: String): Boolean {
            return true
        }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testMessageVerificatorInvocation() = runBlocking {
        class MessageVerificatorTest : MessageVerificator {
            override fun verifyMessage(message: String, key: String): Boolean {
                assertEquals(message, MSG_RESPONSE)
                assertEquals(key, KEY_RESPONSE)
                return true
            }
        }
        val regions = RegionsCommon(GenericRegionStateProvider(), GenericPingHandlerTest(), MessageVerificatorTest())
        val response = regions.handleFetchRegionsResponse(RESPONSE)
    }

    @Test
    fun testPingRequestInvocation() = runBlocking {
        class PingHandlerTest : PingRequest {
            override fun platformPingRequest(
                endpoints: Map<String, List<String>>,
                callback: (result: Map<String, List<PingRequest.PlatformPingResult>>) -> Unit
            ) {
                assertFalse(endpoints.isEmpty())
                assertEquals(endpoints.keys.first(), "us_california")
                assertEquals(endpoints["us_california"]?.size, 2) // Two UDP endpoints available
                callback(emptyMap())
            }
        }
        val regions = RegionsCommon(GenericRegionStateProvider(), PingHandlerTest(), GenericMessageVerificatorTest())
        regions.handleFetchRegionsResponse(RESPONSE)
        regions.handlePingRequest {_, _ -> }
    }

    @Test
    fun testPingRequestInvocationError() = runBlocking {
        val regions = RegionsCommon(GenericRegionStateProvider(), GenericPingHandlerTest(), GenericMessageVerificatorTest())
        regions.handlePingRequest { response, error ->
            assertTrue(response.isEmpty())
            assertNotNull(error)
        }
    }

    @Test
    fun testResponseSerializationWithKnownKeys() = runBlocking {
        val regions = RegionsCommon(GenericRegionStateProvider(), GenericPingHandlerTest(), GenericMessageVerificatorTest())
        val result = regions.serializeRegions(MSG_RESPONSE)
        assertEquals(result.regions.size, 1)
        assertEquals(result.regions.first().country, "US")
        assertEquals(result.regions.first().servers.size, 5) // 5 protocols supported in the region
    }

    @Test
    fun testResponseSerializationWithUnknownKeys() = runBlocking {
        val response = "{\"groups\": {\"ovpntcp\": [{\"name\": \"openvpn_tcp\", \"ports\": [11]}], \"ovpnudp\": [{\"name\": \"openvpn_udp\", \"ports\": [11]}], \"wg\": [{\"name\": \"wireguard\", \"ports\": [11]}], \"ikev2\": [{\"name\": \"ikev2\", \"ports\": [11]}], \"proxysocks\": [{\"name\": \"socks\", \"ports\": [11]}], \"proxyss\": [{\"name\": \"shadowsocks\", \"ports\": [11]}]}, \"regions\": [{\"id\": \"us_california\", \"UNKNOWN\": \"ANY\", \"name\": \"US California\", \"country\": \"US\", \"auto_region\": true, \"dns\": \"127.0.0.1\", \"port_forward\": false, \"servers\": {\"ovpnudp\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"ovpntcp\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"ikev2\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}], \"wg\": [{\"ip\": \"127.0.0.1\", \"cn\": \"losangeles455\"}, {\"ip\": \"127.0.0.1\", \"cn\": \"losangeles404\"}]}}]}"
        val regions = RegionsCommon(GenericRegionStateProvider(), GenericPingHandlerTest(), GenericMessageVerificatorTest())
        val result = regions.serializeRegions(response)
        assertEquals(result.regions.size, 1)
        assertEquals(result.regions.first().country, "US")
        assertEquals(result.regions.first().servers.size, 4) // 4 protocols supported in the region
    }
}