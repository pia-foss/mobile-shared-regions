#pragma once

#include "core.h"
#include <stdint.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

// KACIPv4Address specifies an IPv4 address, which is simply a 32-bit integer.
// Addresses are always in host byte order.
typedef uint32_t KACIPv4Address;

// Many parts of the API express a "port array", such as the arrays of known
// service ports in regions and servers.  While C prevents us from defining a
// general "array slice" type for any value, port arrays are common enough to
// be worth a specific type.  This is also similar to KACStringSlice, but for
// port numbers.
typedef struct KACPortArray
{
    const uint16_t *data;   // May be nullptr if size is 0.
    size_t size;
} KACPortArray;

#ifdef __cplusplus
}
#endif
