#pragma once

#include "regions.h"
#include <kapps_core/stringslice.h>
#include <kapps_core/ipaddress.h>

#ifdef __cplusplus
extern "C" {
#endif

// This structure specifies a Dedicated IP to be added to the regions list as a
// "DIP region", which can be selected for connection like other regions.
typedef struct KARDedicatedIP
{
    // The client's artifical "region ID" for the generated DIP region.  This
    // must not collide with any actual region IDs.  The suggested format is
    //    dip-########
    // where ######## is a random 32-bit integer in hexadecimal.  (This is not
    // strictly required but is recommended.)  A unique identifier is
    // recommended even if the client only allows one DIP at a time to ensure
    // that removing a DIP and adding a different one does not unexpectedly
    // carry over favorites, selections, etc.
    KACStringSlice dipRegionId;

    // The IPv4 address of the VPN server for this dedicated IP
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
    // "offline".  The API should provide this.
    KACStringSliceArray serviceGroups;
    // The region ID for the corresponding region - required for DIP regions.
    // Metadata information such as country name, region name, geo flag, etc.
    // are taken from this region, and auxiliary services such as
    // meta/Shadowsocks are used from this region when needed.
    KACStringSlice correspondingRegionId;
} KARDedicatedIP;

#ifdef __cplusplus
}
#endif
