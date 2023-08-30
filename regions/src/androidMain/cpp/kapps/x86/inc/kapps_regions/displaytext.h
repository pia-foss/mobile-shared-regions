#pragma once

#include "regions.h"
#include <kapps_core/stringslice.h>

#ifdef __cplusplus
extern "C" {
#endif

// A display text object contains text for any number of languages, identified
// by BCP-47 language tags.
//
// Support for BCP-47 is simplified to reflect the actual use in our products;
// it supports <language>-<Script>-<RGN>, optionally omitting -<Script> and/or
// -<RGN>, only.
typedef struct KARDisplayText KARDisplayText;

// Get the display text for a particular language.  This will fall back to
// similar languages when possible if the language requested is not present.
//
// The fallback list for a language tag <lang>-<Script>-<RGN> is:
// - Exact match
// - <lang>-<Script>    -> Any <RGN>
// - <lang>-<RGN>       -> Any <Script>
// - <lang>             -> Any <Script> and <RGN>
// - en-US              -> Last resort fallback to en-US
//
// If none of those languages are present, this returns an empty string.
KAPPS_REGIONS_EXPORT
KACStringSlice KARDisplayTextGetLanguageText(const KARDisplayText *pDisplayText,
                                             KACStringSlice language);

#ifdef __cplusplus
}
#endif
