//
//  PublicKeyKeychainStore.swift
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

/// Handled keychain errors.
public enum PublicKeyKeychainError: Error {

    /// Couldn't add entry.
    case add
    
    /// Couldn't find entry.
    case notFound
    
}

class PublicKeyKeychainStore: PublicKeySecureStore {

    private struct Entries {
        static let publicKey = "PIAPublicKey"
    }

    var publicKey: SecKey?
    
    func publicKeyEntry() -> SecKey? {
        guard let publicKey = try? publicKey(withIdentifier: Entries.publicKey) else {
            return nil
        }
        self.publicKey = publicKey
        return publicKey
    }
    
    func setPublicKey(withData data: Data) -> SecKey? {
        clearPubKey()
        guard let publicKey = try? add(publicKeyWithIdentifier: Entries.publicKey, data: data) else {
            return nil
        }
        self.publicKey = publicKey
        return publicKey
    }

    /// :nodoc:
    public func add(publicKeyWithIdentifier identifier: String, data: Data) throws -> SecKey {
        var query = [String: Any]()
        query[kSecClass as String] = kSecClassKey
        query[kSecAttrApplicationTag as String] = identifier
        query[kSecAttrKeyType as String] = kSecAttrKeyTypeRSA
        query[kSecAttrKeyClass as String] = kSecAttrKeyClassPublic
        query[kSecValueData as String] = data

        query.removeValue(forKey: kSecAttrService as String)

        let status = SecItemAdd(query as CFDictionary, nil)
        guard (status == errSecSuccess) else {
            throw PublicKeyKeychainError.add
        }
        return try publicKey(withIdentifier: identifier)
    }
    
    /// :nodoc:
    public func publicKey(withIdentifier identifier: String) throws -> SecKey {
        var query = [String: Any]()
        query[kSecClass as String] = kSecClassKey
        query[kSecAttrApplicationTag as String] = identifier
        query[kSecAttrKeyType as String] = kSecAttrKeyTypeRSA
        query[kSecAttrKeyClass as String] = kSecAttrKeyClassPublic
        query[kSecReturnRef as String] = true

        query.removeValue(forKey: kSecAttrService as String)

        var result: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &result)
        guard (status == errSecSuccess) else {
            throw PublicKeyKeychainError.notFound
        }
        return result as! SecKey
    }
    
    /// :nodoc:
    @discardableResult public func remove(publicKeyWithIdentifier identifier: String) -> Bool {
        var query = [String: Any]()
        query[kSecClass as String] = kSecClassKey
        query[kSecAttrApplicationTag as String] = identifier
        query[kSecAttrKeyType as String] = kSecAttrKeyTypeRSA
        query[kSecAttrKeyClass as String] = kSecAttrKeyClassPublic

        query.removeValue(forKey: kSecAttrService as String)

        let status = SecItemDelete(query as CFDictionary)
        return (status == errSecSuccess)
    }
    
    /// Clear the public key
    public func clearPubKey() {
        remove(publicKeyWithIdentifier: Entries.publicKey)
    }
    
}
