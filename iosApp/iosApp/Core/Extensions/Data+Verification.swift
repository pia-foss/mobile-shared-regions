//
//  Data+Verification.swift
//  iosApp
//  
//  Created by Jose Antonio Blaya Garcia on 04/06/2020.
//  Copyright © 2020 Private Internet Access, Inc.
//
//  This file is part of the Private Internet Access iOS Client.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software 
//  without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
//  permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
//  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
//  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

//

import Foundation
#if os(iOS)
import CommonCrypto
#else
import Security
#endif

// http://blog.flirble.org/2011/01/05/rsa-public-key-openssl-ios/
// https://stackoverflow.com/questions/33172939/verifying-rsa-signature-ios

extension Data {

    // PKCS #1 rsaEncryption szOID_RSA_RSA
    private static let seqOID = Data([
        0x30, 0x0d, 0x06, 0x09, 0x2a, 0x86, 0x48, 0x86,
        0xf7, 0x0d, 0x01, 0x01, 0x01, 0x05, 0x00
    ])
    
    func withStrippedASN1Header() -> Data? {
        guard !isEmpty else {
            return nil
        }
        
        return withUnsafeBytes { (c_key: UnsafePointer<UInt8>?) in
            var idx = 0
            
            guard let c_key = c_key else {
                return nil
            }

            guard (c_key[idx] == 0x30) else {
                return nil
            }
            idx += 1

            let offset = c_key[idx]
            if (offset > 0x80) {
                idx += Int(UInt8(offset) - 0x80 + 1)
            } else {
                idx += 1
            }

            guard (subdata(in: idx..<(idx + 15)) == Data.seqOID) else {
                return nil
            }
            idx += 15;
            
            guard (c_key[idx] == 0x03) else {
                return nil
            }
            idx += 1

            if (c_key[idx] > 0x80) {
                idx += Int(UInt8(c_key[idx]) - 0x80 + 1)
            } else {
                idx += 1
            }
            
            guard (c_key[idx] == 0) else {
                return nil
            }
            idx += 1

            // Now make a new NSData from this buffer
            return subdata(in: idx..<count)
        }
    }
    
    func sha256() -> Data {
        #if os(iOS)
            var hash = [UInt8](repeating: 0,  count: Int(CC_SHA256_DIGEST_LENGTH))
            withUnsafeBytes {
                _ = CC_SHA256($0, CC_LONG(count), &hash)
            }
            return Data(bytes: hash)
        #else
            let transform = SecDigestTransformCreate(kSecDigestSHA2, 256, nil)
            SecTransformSetAttribute(transform, kSecTransformInputAttributeName, self as CFTypeRef, nil)
            return SecTransformExecute(transform, nil) as! Data
        #endif
    }

    func verifySHA256(withRSASignature signature: Data, publicKey: SecKey) -> Bool {
        #if os(iOS)
            let signedData = [UInt8](sha256())
            let signatureBytes = [UInt8](signature)
            
            let status = SecKeyRawVerify(
                publicKey,
                .PKCS1SHA256,
                signedData,
                signedData.count,
                signatureBytes,
                signatureBytes.count
            )
            return (status == errSecSuccess)
        #else
            let signedData = sha256()

            guard let transform = SecVerifyTransformCreate(publicKey, signature as CFData, nil) else {
                return false
            }
            SecTransformSetAttribute(transform, kSecTransformInputAttributeName, signedData as CFData, nil)
            SecTransformSetAttribute(transform, kSecDigestTypeAttribute, kSecDigestSHA2, nil)
            SecTransformSetAttribute(transform, kSecDigestLengthAttribute, 256 as CFNumber, nil)
            return SecTransformExecute(transform, nil) as? Bool ?? false
        #endif
    }
}
