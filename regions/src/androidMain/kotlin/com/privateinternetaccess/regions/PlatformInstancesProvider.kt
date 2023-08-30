package com.privateinternetaccess.regions

import android.app.Application

/**
 * Android specific interface to provide platform specific instances to the library.
 *
 * Note: An instance implementing this interface has to be provided to a RegionsBuilder instance, because this provider is used to obtain an Android application context instance.
 */
actual interface PlatformInstancesProvider {
    fun provideApplicationContext(): Application
}

@Throws(Exception::class)
internal actual fun checkInstancesProvider(provider: PlatformInstancesProvider?) {
    provider ?: throw Exception("Platform instances provider missing.")
}