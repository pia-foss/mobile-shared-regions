//
//  PingTests.swift
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
import Regions
@testable import iosApp

class PingTests: XCTestCase {

    private var pinger: ICMPPing!
    
    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testPingExistingIP() throws {
        
        let expPing = expectation(description: "pingHost")

        self.pinger = ICMPPing(ip: "8.8.8.8", completionBlock: { (latency) in
            if let _ = latency {
                expPing.fulfill()
                XCTAssert(true)
            }
        })
        self.pinger.start()

        waitForExpectations(timeout: 5.0, handler: nil)

    }
    
    func testPingInvalidIPTimeoutMax1second() throws {
        
        let expPing = expectation(description: "pingHost")
        let currentDate = Date()
        
        self.pinger = ICMPPing(ip: "10.10.10.10", completionBlock: { (latency) in
            if let _ = latency {
                expPing.fulfill()
                XCTAssert(true)
            } else {
                expPing.fulfill()
                XCTAssert(true, "Timeout after 1 second, firing date \(currentDate), now \(Date())")
            }
        })
        self.pinger.start()

        waitForExpectations(timeout: 1.1, handler: nil)

    }

}
