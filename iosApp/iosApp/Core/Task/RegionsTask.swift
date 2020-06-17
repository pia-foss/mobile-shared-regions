//
//  RegionsTask.swift
//  iosApp
//  
//  Created by Jose Antonio Blaya Garcia on 05/06/2020.
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
import Regions
 
public class RegionsTask {

    public init() {}
    private var verifiedResponse = ""
    
    /// Makes the request against the API and return the list of regions
    public func fetch(_ callback: @escaping (([RegionsResponse.Region], KotlinError?) -> Void)) {
        
        let regionBuilder = RegionsCommonBuilder()
            .setPingRequestDependency(pingRequestDependency: self)
            .setMessageVerificatorDependency(messageVerificatorDependency: self)
            .build()
        
        regionBuilder.fetch { [weak self] (response, error) in
            
            guard let response = response else {
                callback([], error)
                return
            }
            
            callback(response.regions, error)
            
        }

    }
    
    /// Makes the request against the API and return the raw json data
    /// TODO: Temp method to return the raw data. Once the legacy servers are removed from the App, we will use the Server object provided by the library
    public func fetchRawData(_ callback: @escaping ((String, KotlinError?) -> Void)) {
        
        let regionBuilder = RegionsCommonBuilder()
            .setPingRequestDependency(pingRequestDependency: self)
            .setMessageVerificatorDependency(messageVerificatorDependency: self)
            .build()
        
        regionBuilder.fetch { [weak self] (response, error) in
            
            guard let response = response else {
                callback("", error)
                return
            }
            
            guard let rawData = self?.verifiedResponse else {
                callback("", error)
                return
            }
            
            callback(rawData, error)
            
        }

    }

    
}

extension RegionsTask: PingRequest, MessageVerificator {
    
    // MARK: - PingRequest

    public func platformPingRequest(
        endpoints: [String : [String]],
        callback: @escaping ([String : [PingRequestPlatformPingResult]]) -> Void
    ) {
        print("PIAiOS. Ping everyone!")
        callback([String: [PingRequestPlatformPingResult]]())
    }

    // MARK: - MessageVerificator

    public func verifyMessage(message: String, key: String) -> Bool {
        
        guard let signature = Data(base64Encoded: key, options: .ignoreUnknownCharacters) else {
            return false
        }

        let verifySignature = VerifySignature(json: message, signature: signature)
        let response = verifySignature.verify()
        
        if response {
            verifiedResponse = message
        } else {
            verifiedResponse = ""
        }
        
        return response
        
    }

}
