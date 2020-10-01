//
//  VerifyTests.swift
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

import XCTest
@testable import iosApp

class VerifyTests: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testVerifySignatureWithError() throws {
        PublicKeyKeychainStore().remove(publicKeyWithIdentifier: "PIAPublicKey")
        let verify = VerifySignature(json: "abs", signature: Data())
        XCTAssert(verify.verify() == false, "Verification failed with invalid json")
    }

    func testVerifySignatureFromJSON() throws {
        
        PublicKeyKeychainStore().remove(publicKeyWithIdentifier: "PIAPublicKey")
        let expServers = expectation(description: "retrieveServerResponse")

        let url = URL(string: "https://serverlist.piaservers.net/vpninfo/servers/v4")!

        let task = URLSession.shared.dataTask(with: url) {(data, response, error) in
            guard let data = data else { return }

            let signature = self.signatureFrom(data: data)
            let json = self.jsonFrom(data: data)
            
            let verify = VerifySignature(json: json!, signature: signature!)
            XCTAssert(verify.verify() == true, "Verification was valid making the request to the region endpoint")

            expServers.fulfill()

        }

        task.resume()
        waitForExpectations(timeout: 5.0, handler: nil)

    }
    
    private func signatureFrom(data: Data) -> Data? {
        
        guard let dirtyString = String(data: data, encoding: .utf8) else {
            return nil
        }
        let dirtyLines = dirtyString.components(separatedBy: "\n\n")
        guard let signatureString = dirtyLines.last else {
            return nil
        }
        guard let signature = Data(base64Encoded: signatureString, options: .ignoreUnknownCharacters) else {
            return nil
        }
        
        return signature

    }
    
    private func jsonFrom(data: Data) -> String? {
        
        guard let dirtyString = String(data: data, encoding: .utf8) else {
            return nil
        }
        let dirtyLines = dirtyString.components(separatedBy: "\n\n")
        guard let jsonString = dirtyLines.first else {
            return nil
        }
        
        return jsonString

    }


}

