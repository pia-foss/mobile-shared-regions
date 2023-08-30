#pragma once

#include "regions.h"
#include <kapps_core/stringslice.h>

#ifdef __cplusplus
extern "C" {
#endif

// Region display information - dots, map coordinates, etc.
typedef struct KARRegionDisplay KARRegionDisplay;
KAPPS_REGIONS_EXPORT
void KARRegionDisplayRetain(const KARRegionDisplay *pRegion);
KAPPS_REGIONS_EXPORT
void KARRegionDisplayRelease(const KARRegionDisplay *pRegion);

// Get the region's ID.
KAPPS_REGIONS_EXPORT
KACStringSlice KARRegionDisplayId(const KARRegionDisplay *pRegion);
// Get the region's country code (ISO 3166-1 alpha-2)
KAPPS_REGIONS_EXPORT
KACStringSlice KARRegionDisplayCountry(const KARRegionDisplay *pRegion);
// Get the region's geographic latitude - [-90, 90] corresponds to [90 deg S,
// 90 deg N].  Returns NaN if the coordinates are not available.
KAPPS_REGIONS_EXPORT
double KARRegionDisplayGeoLatitude(const KARRegionDisplay *pRegion);
// Get the region's geographic longitude - [-180, 180] corresponds to
// [180 deg W, 180 deg E].  Returns NaN if the coordinates are not available.
KAPPS_REGIONS_EXPORT
double KARRegionDisplayGeoLongitude(const KARRegionDisplay *pRegion);
// Get the region's display name translations.
KAPPS_REGIONS_EXPORT
const KARDisplayText *KARRegionDisplayName(const KARRegionDisplay *pRegion);

#ifdef __cplusplus
}
#endif
