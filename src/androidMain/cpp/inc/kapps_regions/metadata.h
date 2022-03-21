#pragma once

#include "regions.h"
#include "dynamicgroup.h"
#include "countrydisplay.h"
#include "regiondisplay.h"
#include <kapps_core/stringslice.h>
#include <kapps_core/arrayslice.h>

#ifdef __cplusplus
extern "C" {
#endif

// The region metadata provides metadata about the available regions, countries,
// etc.  Unlike the regions list, this information is mostly static.
typedef struct KARMetadata KARMetadata;
// Build the metadata from metadata JSON.  Remove any attached signature if
// present.
//
// Like the regions list, semantic errors in individual countries, regions, etc.
// are traced and ignored.  If the entire JSON is unusable, this returns
// nullptr.
KAPPS_REGIONS_EXPORT
const KARMetadata *KARMetadataCreate(KACStringSlice metadataJson);
// Build the metadata from legacy PIA regions v6 and metadata v2 JSON.  Regions
// is required in addition to metadata, because some metadata fields were in
// the regions JSON in the legacy format.
//
// This has some caveats since the legacy metadata lacks some information
// present in the new format - namely, complete country name+prefix information
// for all countries, and complete city/region names for all regions.
//
// The legacy format gave a single "name" for each region, which was either a
// country name for a single-region country or a prefixed region ("US Chicago",
// "DE Frankfurt") for multiple-region countries.  Country names are listed
// separately for multiple-region countries only.
//
// As a result, there are several caveats, but this should generally work for
// legacy clients using this compatibility mechanism:
//
// 1. Regions in a single-region country give the country name for both country
//    and region; country prefixes are not provided.
// 2. Regions in a multiple-region country give a country name, country prefix,
//    and region name.  However, the country prefix and region name are split
//    heuristically from the prefixed names present in the metadata.  This works
//    for the current set of region names but has some potential corner cases
//    if some combinations of names were to appear.
KAPPS_REGIONS_EXPORT
const KARMetadata *KARMetadataCreatePiav6v2(KACStringSlice regionsv6Json,
                                            KACStringSlice metadatav2Json);
KAPPS_REGIONS_EXPORT
void KARMetadataDestroy(const KARMetadata *pMetadata);

// Get a dynamic server group by ID.  Note that there also may be static groups
// known to the app at compile time.
KAPPS_REGIONS_EXPORT
const KARDynamicGroup *KARMetadataGetDynamicGroup(const KARMetadata *pMetadata,
                                                 KACStringSlice id);
// Get all dynamic server groups - an array slice with KARDynamicGroup* elements.
KAPPS_REGIONS_EXPORT
KACArraySlice KARMetadataDynamicGroups(const KARMetadata *pMetadata);

// Get a country's display information by ISO 3166-1 alpha-2 code.
KAPPS_REGIONS_EXPORT
const KARCountryDisplay *KARMetadataGetCountryDisplay(const KARMetadata *pMetadata,
                                                      KACStringSlice id);
// Get all country display information - array slice with KARCountryDisplay* elements
KAPPS_REGIONS_EXPORT
KACArraySlice KARMetadataCountryDisplays(const KARMetadata *pMetadata);

// Get a region's display information by ID.
KAPPS_REGIONS_EXPORT
const KARRegionDisplay *KARMetadataGetRegionDisplay(const KARMetadata *pMetadata,
                                                    KACStringSlice id);
// Get all region display information - array slice with KARRegionDisplay elements
KAPPS_REGIONS_EXPORT
KACArraySlice KARMetadataRegionDisplays(const KARMetadata *pMetadata);

#ifdef __cplusplus
}
#endif
