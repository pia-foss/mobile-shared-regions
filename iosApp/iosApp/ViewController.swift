import UIKit
import app

class ViewController: UIViewController, PingRequest {
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        let region = RegionsFactory.Companion.init().create(pingDependency: self)
        region.fetch { (response, error) in
            print("PIAiOS. fetch response: \(response) error: \(error)")
            
            region.pingRequests(protocol: RegionsProtocol.openvpnUdp) { (response, error) in
                print("PIAiOS. ping response: \(response) error: \(error)")
            }
        }
    }
    
    func platformPingRequest(
        endpoints: [String : [String]],
        callback: @escaping ([String : [PingRequestPlatformPingResult]]) -> Void
    ) {
        print("PIAiOS. Ping everyone!")
        callback([String: [PingRequestPlatformPingResult]]())
    }
}
