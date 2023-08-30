#ifndef KAPPS_CORE_API_CORE_H
#define KAPPS_CORE_API_CORE_H

// Detect platform
//                       | Windows |  macOS  |  Linux  | Android  |   iOS   |
//                       |---------|---------|---------|----------|---------|
// KAPPS_CORE_OS_...     | WINDOWS |   MAC   |  LINUX  | ANDROID  |   IOS   |
// KAPPS_CORE_OS_...     |         |          <----- POSIX ----->           |
// KAPPS_CORE_FAMILY_... |       <-- DESKTOP -->       |    <- MOBILE ->    |
// KAPPS_CORE_KERNEL_... |    NT   |   XNU   |   <-- LINUX -->    |   XNU   |
//                                                                     ^-- Includes iOS Sim.
//
// Note that KAPPS_CORE_OS_LINUX means Linux desktop ("GNU/Linux", we require glibc)

#ifdef _WIN32   // Defined for Windows on any architecture, including x64 and ARM64
    #define KAPPS_CORE_OS_WINDOWS 1
    #define KAPPS_CORE_OS_WIN 1
    #define KAPPS_CORE_FAMILY_DESKTOP 1
    // _KERNEL_NT is a bit silly since we only support one NT platform, but may
    // as well have it for contrast with other kernels
    #define KAPPS_CORE_KERNEL_NT 1
// clang targeting Android does define __linux__, check Android first
#elif defined(__ANDROID__)
    #define KAPPS_CORE_OS_ANDROID 1
    #define KAPPS_CORE_OS_POSIX 1
    #define KAPPS_CORE_FAMILY_MOBILE 1
    #define KAPPS_CORE_KERNEL_LINUX 1
#elif defined(__linux__)
    #define KAPPS_CORE_OS_LINUX 1
    #define KAPPS_CORE_OS_POSIX 1
    #define KAPPS_CORE_FAMILY_DESKTOP 1
    #define KAPPS_CORE_KERNEL_LINUX 1
#elif defined(__APPLE__)
    // All Apple platforms are XNU-based and Unix-like
    #define KAPPS_CORE_OS_POSIX 1
    #define KAPPS_CORE_KERNEL_XNU 1
    #include <TargetConditionals.h>
    #if TARGET_OS_OSX == 1
        #define KAPPS_CORE_OS_MACOS 1
        #define KAPPS_CORE_FAMILY_DESKTOP 1
    #elif TARGET_OS_IOS == 1
        #define KAPPS_CORE_OS_IOS 1
        #define KAPPS_CORE_FAMILY_MOBILE 1
        // There is no differentiation for the iOS Simulator, it's treated as iOS
    #else
        #error Unsupported platform - add platform to kapps-core/core.h
    #endif
#else
    #error Unsupported platform - add platform to kapps-core/core.h
#endif

// All KApps modules can be built for static or dynamic linking.  The default is
// dynamic.  This controls the annotations (visibility or DLL import/export)
// applied to exported APIs.
//
// The possible configurations are:
// - Dynamic build - downstream (importing this module) - define nothing.
//   Exports are given default visibility (GCC/clang) or dllimport (MSVC).
// - Dynamic build - module itself - define BUILD_KAPPS_<MODULE>.
//   Exports are given default visibility (GCC/clang) or dllexport (MSVC).
// - Static build - define STATIC_KAPPS_<MODULE>.
//   No annotations are applied; suitable for static linking only.
#ifdef STATIC_KAPPS_CORE
    #define KAPPS_CORE_EXPORT   // No annotations for static build
#else
    #ifdef KAPPS_CORE_OS_WINDOWS
        #ifdef BUILD_KAPPS_CORE
            #define KAPPS_CORE_EXPORT __declspec(dllexport)
        #else
            #define KAPPS_CORE_EXPORT __declspec(dllimport)
        #endif
    #else
        #define KAPPS_CORE_EXPORT __attribute__((visibility("default")))
    #endif
#endif

#endif
