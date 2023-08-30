#pragma once

#include "regions.h"

#ifdef __cplusplus
extern "C" {
#endif

// These are the known services able to be represented by the regions list.
// Each server can provide some or all of these services.  (All reported servers
// provide at least one service; servers with no known services are ignored.)
//
// Many of these are VPN services, but some other types of services also exist.
// Some service types carry additional information (available ports, etc.), but
// this information can vary per service.
typedef enum KARService
{
    // VPN protocol services
    KARServiceOpenVpnTcp,   // OpenVPN with TCP transport
    KARServiceOpenVpnUdp,   // OpenVPN with UDP transport
    KARServiceWireGuard,    // WireGuard with our custom REST API provisioning
    KARServiceIkev2,    // IKEv2 VPN
    // Other services
    KARServiceShadowsocks,  // Shadowsocks obfuscated proxy; TCP
    KARServiceMeta, // "Meta" API proxies
} KARService;

#ifdef __cplusplus
}
#endif
