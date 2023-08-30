#pragma once

#include <kapps_core/core.h>

#ifdef STATIC_KAPPS_REGIONS
    #define KAPPS_REGIONS_EXPORT
#else
    #ifdef KAPPS_CORE_OS_WINDOWS
        #ifdef BUILD_KAPPS_REGIONS
            #define KAPPS_REGIONS_EXPORT __declspec(dllexport)
        #else
            #define KAPPS_REGIONS_EXPORT __declspec(dllimport)
        #endif
    #else
        #define KAPPS_REGIONS_EXPORT __attribute__((visibility("default")))
    #endif
#endif
