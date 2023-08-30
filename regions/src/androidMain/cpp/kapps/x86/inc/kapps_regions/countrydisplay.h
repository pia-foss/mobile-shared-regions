#pragma once

#include "regions.h"
#include "displaytext.h"
#include <kapps_core/stringslice.h>

#ifdef __cplusplus
extern "C" {
#endif

// Country display information - display names.
typedef struct KARCountryDisplay KARCountryDisplay;
KAPPS_REGIONS_EXPORT
void KARCountryDisplayRetain(const KARCountryDisplay *pCountry);
KAPPS_REGIONS_EXPORT
void KARCountryDisplayRelease(const KARCountryDisplay *pCountry);

// Get the country's ISO 3166-1 alpha-2 code.
KAPPS_REGIONS_EXPORT
KACStringSlice KARCountryDisplayCode(const KARCountryDisplay *pCountry);
// Get the display text for the country's name.
KAPPS_REGIONS_EXPORT
const KARDisplayText *KARCountryDisplayName(const KARCountryDisplay *pCountry);
// Get the display text for the country's "prefix" - used in some UIs when
// multiple regions are provided for a country.  (This is the "DE - " in
// "DE - Frankfurt", "DE - Berlin", etc.  Although it is the country code in
// en-US, other languages use other abbreviations.)
//
// Some brands do not provide this when the clients do not use it; in that case
// this KARDisplayText contains no language texts.
KAPPS_REGIONS_EXPORT
const KARDisplayText *KARCountryDisplayPrefix(const KARCountryDisplay *pCountry);

#ifdef __cplusplus
}
#endif
