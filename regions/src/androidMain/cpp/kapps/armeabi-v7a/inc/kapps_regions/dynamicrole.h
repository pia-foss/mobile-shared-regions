#pragma once

#include "regions.h"
#include "displaytext.h"
#include <kapps_core/stringslice.h>

#ifdef __cplusplus
extern "C" {
#endif

// Dynamic server groups allow the creation of new user-facing region groups
// without shipping app updates.  The dynamic groups are reported in region
// metadata, which causes the app to start fetching the group's region list;
// etc.
typedef struct KARDynamicRole KARDynamicRole;
KAPPS_REGIONS_EXPORT
void KARDynamicRoleRetain(const KARDynamicRole *pDynGroup);
KAPPS_REGIONS_EXPORT
void KARDynamicRoleRelease(const KARDynamicRole *pDynGroup);

// Get the dynamic group's ID.
KAPPS_REGIONS_EXPORT
KACStringSlice KARDynamicRoleId(const KARDynamicRole *pDynGroup);

// Get the dynamic group's display name
KAPPS_REGIONS_EXPORT
const KARDisplayText *KARDynamicRoleName(const KARDynamicRole *pDynGroup);

// Get the dynamic group's resource name - this determines the URI where the
// regions list can be found, using the application's known region list endpoint
// prefix(es)
KAPPS_REGIONS_EXPORT
KACStringSlice KARDynamicRoleResource(const KARDynamicRole *pDynGroup);

// Get the display icon for the Windows platform.  The API allows specifying
// other platforms too, but none exist currently.  This should be a URI where
// the icon can be downloaded, but KARDynamicRole does not validate it.
KAPPS_REGIONS_EXPORT
KACStringSlice KARDynamicRoleWinIcon(const KARDynamicRole *pDynGroup);

#ifdef __cplusplus
}
#endif
