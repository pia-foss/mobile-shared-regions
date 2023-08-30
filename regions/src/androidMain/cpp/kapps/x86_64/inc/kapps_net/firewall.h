#ifndef KAPPS_NET_API_FIREWALL_H
#define KAPPS_NET_API_FIREWALL_H

#include "net.h"
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

// Public C-linkage API to the firewall component
typedef struct KANFirewallConfig
{
    void(*pAboutToApplyRules)();
    void(*pDidApplyRules)();
} KANFirewallConfig;

KAPPS_NET_EXPORT void KANConfigureFirewall(const struct KANFirewallConfig *pConfig);
KAPPS_NET_EXPORT void KANApplyFirewallRules();

#ifdef __cplusplus
}
#endif

#endif
