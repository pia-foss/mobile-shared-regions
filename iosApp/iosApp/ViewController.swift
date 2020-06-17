import UIKit
import Regions

class ViewController: UIViewController, PingRequest, MessageVerificator {
        
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        let regionTask = RegionsTask()
        regionTask.fetch { regions, error in
            print("Valid response from server")
        }
        
        /* SIMPLE USAGE EXAMPLE
        let region = RegionsBuilder()
            .setPingRequestDependency(pingRequestDependency: self)
            .setMessageVerificatorDependency(messageVerificatorDependency: self)
            .build()
        region.fetch { (response, error) in
            print("PIAiOS. fetch response: \(response) error: \(error)")

            region.pingRequests(protocol: RegionsProtocol.openvpnUdp) { (response, error) in
                print("PIAiOS. ping response: \(response) error: \(error)")
            }
        }
 */
    }
    
    // MARK: - PingRequest
    func platformPingRequest(
        endpoints: [String : [String]],
        callback: @escaping ([String : [PingRequestPlatformPingResult]]) -> Void
    ) {
        print("PIAiOS. Ping everyone!")
        callback([String: [PingRequestPlatformPingResult]]())
    }

    // MARK: - MessageVerificator

    func verifyMessage(message: String, key: String) -> Bool {
        print("PIAiOS. Verified!")
        return true
    }
}
