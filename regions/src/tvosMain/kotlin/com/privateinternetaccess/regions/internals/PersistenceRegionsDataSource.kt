package com.privateinternetaccess.regions.internals
import platform.Foundation.NSUserDefaults

internal actual class PersistenceRegionsDataSource(
    preferenceName: String? = null,
    private val logErrors: Boolean
) : RegionsCacheDataSource, AbstractPersistenceRegionsDataSource() {

    private val userDefaults: NSUserDefaults

    init {
        userDefaults = NSUserDefaults(suiteName = preferenceName ?: "PersistenceRegionsDataSource")
    }

    override fun logError(error: String) {
        if(logErrors) {
            error("$TAG: $error")
        }
    }

    override fun storeJsonEntry(key: String, jsonEntry: String) {
        userDefaults.setObject(jsonEntry, forKey = key)
        userDefaults.synchronize()
    }

    override fun retrieveJsonEntry(key: String): String? {
        return userDefaults.stringForKey(key)
    }

    companion object {
        private const val TAG: String = "PersistenceRegionsDataSource"
    }
}
