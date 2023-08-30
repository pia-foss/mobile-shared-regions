cmake_minimum_required(VERSION 3.10.2)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
set(CMAKE_SHARED_LINKER_FLAGS "-fuse-ld=lld")

# libc++
add_library(libc++_shared SHARED IMPORTED)
set_target_properties(libc++_shared PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/lib/libc++_shared.so"
)

# regions
add_library(lib_kapps_regions SHARED IMPORTED)
set_target_properties(lib_kapps_regions PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/lib/libkapps_regions.so"
    INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/inc/kapps_regions"
)

# net
add_library(lib_kapps_net SHARED IMPORTED)
set_target_properties(lib_kapps_net PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/lib/libkapps_net.so"
    INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/inc/kapps_net"
)

# core
add_library(lib_kapps_core SHARED IMPORTED)
set_target_properties(lib_kapps_core PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/lib/libkapps_core.so"
    INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/inc/kapps_core"
)

# bindings
add_library(kapps_regions_bindings SHARED "${CMAKE_SOURCE_DIR}/bindings.cpp")
target_include_directories(kapps_regions_bindings PRIVATE "${CMAKE_SOURCE_DIR}/kapps/${ANDROID_ABI}/inc")
target_include_directories(kapps_regions_bindings PRIVATE "${CMAKE_SOURCE_DIR}/utils")

# linking
target_link_libraries(kapps_regions_bindings android libc++_shared lib_kapps_regions lib_kapps_net lib_kapps_core log)
