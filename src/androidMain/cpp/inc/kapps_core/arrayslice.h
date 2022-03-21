#pragma once

#include "core.h"
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

// Many KApps module APIs provide an array of elements as a result, where the
// element type is opaque to the external API (in particular, its size is not
// known).
//
// These arrays are specified with a base pointer (pointer to first element),
// size (count of elements) and stride (byte offset between each element).
// To iterate the list, start with the pointer to the first element, then add
// the stride to reach each subsequent element until reaching the specified
// number of elements.
//
// The base pointer is a const void * because C lacks any mechanism to specify
// it generically; the API documentation will indicate the real type of the
// pointer.
typedef struct KACArraySlice
{
    const void *data; // May be nullptr if size is 0.
    size_t size;
    size_t stride; // Always positive.
} KACArraySlice;

#ifdef __cplusplus
}
#endif
