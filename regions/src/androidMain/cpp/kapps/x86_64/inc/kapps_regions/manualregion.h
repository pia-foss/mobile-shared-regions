#pragma once

#include "regions.h"
#include <kapps_core/stringslice.h>
#include <kapps_core/ipaddress.h>
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

// This structure specifies a manual region to be added to the regions list.
// This is generally used for development to test specific servers.
typedef struct KARManualRegion
{
    // The client's artifical "region ID" for the manual region.  Like DIPs,
    // this must not collide with a real region.  If the client only provides
    // one manual region, it can be a fixed string like "manual".  For more than
    // one, it can have a random identifier, or a sequential counter, etc.
    KACStringSlice manualRegionId;

    // The IPv4 address of the single VPN server for this region.  There is no
    // way to create a manual region with more than one server (create more than
    // one manual region instead).
    KACIPv4Address address;
    // The certificate common name for this server
    KACStringSlice commonName;
    // FQDN of the server - only used on some platforms for IKEv2, empty if not
    // needed
    KACStringSlice fqdn;

    // The service group(s) from the general regions list to apply to this
    // region - indicates the available protocols, ports, etc.  Represents the
    // deployment configuration applied by Ops.
    //
    // At least one service group should be specified, or the region will be
    // "offline".  Since this is a dev tool, the product should usually default
    // this if the user does not specify one using some standard service group
    // from the brand's region list.
    KACStringSliceArray serviceGroups;
    // The region ID for the corresponding region.  Unlike DIPs, this is
    // optional for manual regions.  Manual regions get a fixed
    // name/flag/geo/etc.; this is only used for auxiliary services like 'meta'.
    // If not specified, the region does not offer the 'meta' service.
    KACStringSlice correspondingRegionId;

    // Various aspects of the server can be overridden for testing - this
    // replaces corresponding config from the server group.

    // Force NCP use instead of legacy pia-signal-settings.  (If false, the
    // value is taken from the server group; there is no way to force
    // pia-signal-settings.)
    bool forceOpenVpnNcp;
    // Override the possible ports for OpenVPN TCP or UDP.  If either list is
    // empty, the ports are taken from the server group.
    KACPortArray openVpnUdpOverridePorts;
    KACPortArray openVpnTcpOverridePorts;
} KARManualRegion;

#ifdef __cplusplus
}
#endif
