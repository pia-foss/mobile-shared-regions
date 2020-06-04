//
//  ICMPPing.swift
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
import __RegionsLibraryNative

class ICMPPing: NSObject {
    
    private var ipAddress: String!
    private var completion: (Int?) -> ()

    private var pinger: SimplePing?
    private var sentTime: TimeInterval = 0
    private var timeoutTimer: Timer!

    init(ip: String, completionBlock: @escaping (Int?) -> ()) {
        self.completion = completionBlock
        self.ipAddress = ip
    }
    
    func start() {

        let pinger = SimplePing(hostName: self.ipAddress)
        self.pinger = pinger
        pinger.addressStyle = .icmPv4
        pinger.delegate = self
        pinger.start()

    }
    
    func stop() {
        
        self.pinger?.stop()
        self.pinger = nil

    }

    /// Sends a ping.
    ///
    /// Called to send a ping, both directly (as soon as the SimplePing object starts up) and
    /// via a timer (to continue sending pings periodically).
    
    @objc func sendPing() {
        self.pinger!.send(with: nil)
        self.timeoutTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(fireTimer), userInfo: nil, repeats: false)
    }
    
    @objc func fireTimer() {
        self.stop()
        self.timeoutTimer.invalidate()
        self.timeoutTimer = nil
        completion(nil)
    }

}

extension ICMPPing: SimplePingDelegate {
    
    // MARK: pinger delegate callback
    
    func simplePing(_ pinger: SimplePing, didStartWithAddress address: Data) {
        self.sendPing()
    }
    
    func simplePing(_ pinger: SimplePing, didFailWithError error: Error) {
        completion(nil)
        self.stop()
    }
    
    func simplePing(_ pinger: SimplePing, didSendPacket packet: Data, sequenceNumber: UInt16) {
        sentTime = Date().timeIntervalSince1970
    }
    
    func simplePing(_ pinger: SimplePing, didFailToSendPacket packet: Data, sequenceNumber: UInt16, error: Error) {
        completion(nil)
    }
    
    func simplePing(_ pinger: SimplePing, didReceivePingResponsePacket packet: Data, sequenceNumber: UInt16) {
        let latency = Int(((Date().timeIntervalSince1970 - sentTime).truncatingRemainder(dividingBy: 1)) * 1000)
        completion(latency)
    }
    
    func simplePing(_ pinger: SimplePing, didReceiveUnexpectedPacket packet: Data) {
    }
    
}
