package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.model.RegionsResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal abstract class AbstractPersistenceRegionsDataSource : RegionsCacheDataSource {
    final override fun saveRegion(locale: String, regionsResponse: RegionsResponse) {
        val entry = CacheEntry(
            locale = locale,
            regionsResponse = regionsResponse
        )
        val jsonEntry: String = try {
            Json.encodeToString(entry)
        } catch (t: Throwable) {
            logError(t.stackTraceToString())
            return
        }
        storeJsonEntry(jsonEntry = jsonEntry)
    }

    final override fun getRegion(locale: String?): Result<RegionsResponse> {
        val jsonEntry: String? = retrieveJsonEntry()
        if (jsonEntry.isNullOrBlank()) {
            return Result.failure(CacheError(locale))
        }

        val cacheEntry: CacheEntry? = try {
            Json.decodeFromString(jsonEntry)
        } catch (t: Throwable) {
            logError(t.stackTraceToString())
            null
        }
        return when {
            cacheEntry != null && (locale == null || cacheEntry.locale == locale) -> Result.success(cacheEntry.regionsResponse)
            else -> Result.failure(CacheError(locale))
        }
    }

    protected abstract fun logError(error: String)

    protected abstract fun storeJsonEntry(jsonEntry: String)

    protected abstract fun retrieveJsonEntry(): String?
}
