#ifndef KAPPS_NET_API_NET_H
#define KAPPS_NET_API_NET_H

#include <kapps_core/core.h>

#ifdef STATIC_KAPPS_NET
    #define KAPPS_NET_EXPORT
#else
    #ifdef KAPPS_CORE_OS_WINDOWS
        #ifdef BUILD_KAPPS_NET
            #define KAPPS_NET_EXPORT __declspec(dllexport)
        #else
            #define KAPPS_NET_EXPORT __declspec(dllimport)
        #endif
    #else
        #define KAPPS_NET_EXPORT __attribute__((visibility("default")))
    #endif
#endif

#endif
