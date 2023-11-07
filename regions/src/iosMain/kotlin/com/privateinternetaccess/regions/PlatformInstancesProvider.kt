package com.privateinternetaccess.regions

/**
 * iOS specific interface to provide platform specific instances to the library.
 *
 * Note: An instance implementing this interface does not need to be provided to a RegionsBuilder instance, because this provider is not used in the iOs implementation yet.
 */
actual interface PlatformInstancesProvider

@Throws(Exception::class)
internal actual fun checkInstancesProvider(provider: PlatformInstancesProvider?) = Unit
