#pragma once

#include "core.h"
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

// Many KApps module APIs use string slices to describe string data.  This is a
// weaker constraint than requiring a null-terminated string - a null-terminated
// string can always be adapted to a string slice, but not vice-versa.  Narrow
// string data is always in UTF-8.
//
// Various platforms have different conventions for strings; using a string
// slice as the fundamental API model promotes interoperability without
// incurring expensive copies of potentially large string data.
//
// To be clear, string slice data is _not_ always null-terminated.  The core
// logger in particular uses this to slice parts of file paths, etc., to
// generate context strings for log messages.
typedef struct KACStringSlice
{
    const char *data;   // May be nullptr if size is 0.
    size_t size;
} KACStringSlice;

// An array of non-owning string references can also be represented.
typedef struct KACStringSliceArray
{
    KACStringSlice *data;
    size_t size;
} KACStringSliceArray;

#ifdef __cplusplus
}
#endif
