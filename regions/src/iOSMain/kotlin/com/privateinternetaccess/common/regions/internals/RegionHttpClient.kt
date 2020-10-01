package com.privateinternetaccess.common.regions.internals

import com.privateinternetaccess.common.regions.internals.RegionsCommon.Companion.REQUEST_TIMEOUT_MS
import com.privateinternetaccess.common.regions.internals.RegionsCommon.Companion.CERTIFICATE
import io.ktor.client.HttpClient
import io.ktor.client.engine.ios.Ios
import io.ktor.client.features.HttpTimeout
import io.ktor.client.engine.ios.*
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.*

actual object RegionHttpClient {
    actual fun client(pinnedEndpoint: Pair<String, String>?) = HttpClient(Ios) {
        expectSuccess = false
        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
        }
        pinnedEndpoint?.let {
            engine {
                handleChallenge(RegionCertificatePinner(pinnedEndpoint.first, pinnedEndpoint.second))
            }
        }
    }
}

private class RegionCertificatePinner(private val hostname: String, private val commonName: String) : ChallengeHandler {

    companion object {
        private val certificateData = NSData.create(
            base64EncodedString =
            CERTIFICATE
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replace("\n", ""),
            options = NSDataBase64Encoding64CharacterLineLength
        )
    }

    override fun invoke(
        session: NSURLSession,
        task: NSURLSessionTask,
        challenge: NSURLAuthenticationChallenge,
        completionHandler: (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Unit
    ) {
        if (challenge.protectionSpace.authenticationMethod != NSURLAuthenticationMethodServerTrust) {
            challenge.sender?.cancelAuthenticationChallenge(challenge)
            completionHandler(NSURLSessionAuthChallengeCancelAuthenticationChallenge, null)
            return
        }

        val serverTrust = challenge.protectionSpace.serverTrust
        val serverCertificateRef = SecTrustGetCertificateAtIndex(serverTrust, 0)
        val certificateDataRef = CFBridgingRetain(certificateData) as CFDataRef
        val certificateRef = SecCertificateCreateWithData(null, certificateDataRef)
        val policyRef = SecPolicyCreateSSL(true, null)

        memScoped {
            var preparationSucceeded = true
            val serverCommonNameRef = alloc<CFStringRefVar>()
            SecCertificateCopyCommonName(serverCertificateRef, serverCommonNameRef.ptr)
            val commonNameEvaluationSucceeded = (commonName == CFBridgingRelease(serverCommonNameRef.value))
            val hostNameEvaluationSucceeded = (hostname == challenge.protectionSpace.host)

            val trust = alloc<SecTrustRefVar>()
            val trustCreation = SecTrustCreateWithCertificates(serverCertificateRef, policyRef, trust.ptr)
            if (trustCreation != errSecSuccess) {
                preparationSucceeded = false
            }

            val mutableArrayRef = CFArrayCreateMutable(kCFAllocatorDefault, 1, null)
            CFArrayAppendValue(mutableArrayRef, certificateRef)

            val trustAnchor = SecTrustSetAnchorCertificates(trust.value, mutableArrayRef)
            if (trustAnchor != errSecSuccess) {
                preparationSucceeded = false
            }

            val error = alloc<CFErrorRefVar>()
            val certificateEvaluationSucceeded = SecTrustEvaluateWithError(trust.value, error.ptr)
            challenge.sender?.useCredential(NSURLCredential.create(serverTrust), challenge)
            if (preparationSucceeded && hostNameEvaluationSucceeded && commonNameEvaluationSucceeded && certificateEvaluationSucceeded) {
                completionHandler(NSURLSessionAuthChallengeUseCredential, NSURLCredential.create(serverTrust))
            } else {
                completionHandler(NSURLSessionAuthChallengeCancelAuthenticationChallenge, null)
            }

            CFRelease(serverCertificateRef)
            CFRelease(certificateDataRef)
            CFRelease(certificateRef)
            CFRelease(policyRef)
            CFRelease(mutableArrayRef)
        }
    }
}