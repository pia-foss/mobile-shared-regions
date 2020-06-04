//
//  VerifySignature.swift
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

public class VerifySignature {
    
    private let jsonString: String
    private let signature: Data
    private var publicKey: SecKey?
    
    public init(json: String, signature: Data) {
        
        self.jsonString = json
        self.signature = signature
        self.publicKey = nil
        
        if let publicKey = PublicKeyKeychainStore().publicKeyEntry() {
            self.publicKey = publicKey
        } else {
            DispatchQueue(label: "SynchronizedSecKeyDataAccess", attributes: .concurrent).sync {
                let bundle = Bundle(for: VerifySignature.self)
                guard let pubKeyFile = bundle.path(forResource: "PIA", ofType: "pub") else {
                    preconditionFailure("Can't find pub key file")
                }
                guard let pubKeyData = try? Data(contentsOf: URL(fileURLWithPath: pubKeyFile)) else {
                    preconditionFailure("Can't create Data object from PUB file")
                }
                guard let strippedData = pubKeyData.withStrippedASN1Header() else {
                    preconditionFailure("Can't strip ASN1 header")
                }
                guard let publicKey = PublicKeyKeychainStore().setPublicKey(withData: strippedData) else {
                    //Use the key manually if can't be stored
                    let sizeInBits = strippedData.count * 8
                    let keyDict: [CFString: Any] = [
                        kSecAttrKeyType: kSecAttrKeyTypeRSA,
                        kSecAttrKeyClass: kSecAttrKeyClassPublic,
                        kSecAttrKeySizeInBits: NSNumber(value: sizeInBits),
                        kSecReturnPersistentRef: true
                    ]
                    
                    var error: Unmanaged<CFError>?
                    guard let publicKey = SecKeyCreateWithData(strippedData as CFData,
                                                               keyDict as CFDictionary,
                                                               &error) else {
                                                                preconditionFailure("Can't set the public key in the Keychain")
                    }
                    self.publicKey = publicKey
                    return
                }
                self.publicKey = publicKey
            }
        }

    }
    
    public func verify() -> Bool {
        
        guard let data = jsonString.data(using: .utf8) else {
            fatalError("Cannot encode jsonString to data")
        }
        
        guard let publicKey = publicKey else {
            return false
        }
        
        return data.verifySHA256(withRSASignature: signature, publicKey: publicKey)
    }
    

}
