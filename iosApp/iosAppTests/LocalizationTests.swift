//
//  LocalizationTests.swift
//  iosApp
//  
//  Created by Jose Blaya on 26/08/2020.
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

class LocalizationTests: XCTestCase {

    private var api: RegionsTask?
    
    override func setUpWithError() throws {
        api = RegionsTask()
    }

    override func tearDownWithError() throws {
        api = nil
    }

    func testLocalizationEndpoint() throws {
    
        let expLocalization = expectation(description: "localizationAndGeoData")

        api?.fetchLocalization({ (response, error) in

            XCTAssertNotNil(response)
            XCTAssertNil(error)
            expLocalization.fulfill()

        })

        waitForExpectations(timeout: 5.0, handler: nil)

    }
    
    func testLocalizationResponseIsCorrect() throws {
    
        let expLocalization = expectation(description: "localizationAndGeoData")

        api?.fetchLocalization({ (response, error) in

            if let response = response {
                let spainCoordinates = response.gps["spain"]
                XCTAssertNotNil(spainCoordinates)

                let spainLocalization = response.translations["Spain"]
                XCTAssertNotNil(spainLocalization)

            }

            expLocalization.fulfill()

        })

        waitForExpectations(timeout: 5.0, handler: nil)

    }

}
