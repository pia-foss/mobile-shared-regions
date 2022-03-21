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
typedef struct KARDynamicGroup KARDynamicGroup;
KAPPS_REGIONS_EXPORT
void KARDynamicGroupRetain(const KARDynamicGroup *pDynGroup);
KAPPS_REGIONS_EXPORT
void KARDynamicGroupRelease(const KARDynamicGroup *pDynGroup);

// Get the dynamic group's ID.
KAPPS_REGIONS_EXPORT
KACStringSlice KARDynamicGroupId(const KARDynamicGroup *pDynGroup);

// Get the dynamic group's display name
KAPPS_REGIONS_EXPORT
const KARDisplayText *KARDynamicGroupName(const KARDynamicGroup *pDynGroup);

// Get the dynamic group's resource name - this determines the URI where the
// regions list can be found, using the application's known region list endpoint
// prefix(es)
KAPPS_REGIONS_EXPORT
KACStringSlice KARDynamicGroupResource(const KARDynamicGroup *pDynGroup);

// Get the display icon for the Windows platform.  The API allows specifying
// other platforms too, but none exist currently.  This should be a URI where
// the icon can be downloaded, but KARDynamicGroup does not validate it.
KAPPS_REGIONS_EXPORT
KACStringSlice KARDynamicGroupWinIcon(const KARDynamicGroup *pDynGroup);

#ifdef __cplusplus
}
#endif
