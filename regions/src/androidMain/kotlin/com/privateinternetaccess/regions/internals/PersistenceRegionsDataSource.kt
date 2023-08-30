@file:SuppressLint("LongLogTag")

package com.privateinternetaccess.regions.internals

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.privateinternetaccess.regions.PlatformInstancesProvider


internal actual class PersistenceRegionsDataSource(
    provider: PlatformInstancesProvider,
    preferenceName: String?,
    private val logErrors: Boolean
) : RegionsCacheDataSource, AbstractPersistenceRegionsDataSource() {
    private val cache: SharedPreferences

    init {
        val app: Application = provider.provideApplicationContext()
        cache = when {
            preferenceName.isNullOrBlank() -> app.getSharedPreferences("${app.packageName}_preferences", Context.MODE_PRIVATE)
            else -> app.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        }
    }

    override fun logError(error: String) {
        if (logErrors) {
            Log.e(TAG, error)
        }
    }

    override fun storeJsonEntry(jsonEntry: String) = cache.edit()
        .putString(RegionsDataSourceFactory.DEFAULT_CACHE_ENTRY_KEY, jsonEntry)
        .apply()

    override fun retrieveJsonEntry(): String? = cache.getString(RegionsDataSourceFactory.DEFAULT_CACHE_ENTRY_KEY, null)

    companion object {
        private const val TAG: String = "PersistenceRegionsDataSource"
        private const val DEFAULT_CACHE_ENTRY_KEY: String = "persist_region_entry"
    }
}
