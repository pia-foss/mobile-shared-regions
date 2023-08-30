package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.model.RegionsResponse

internal class InMemoryRegionsCacheDataSource: RegionsCacheDataSource {
    private var cacheEntry: CacheEntry? = null

    override fun saveRegion(locale: String, regionsResponse: RegionsResponse) {
        this.cacheEntry = CacheEntry(
            locale = locale,
            regionsResponse = regionsResponse
        )
    }

    override fun getRegion(locale: String?): Result<RegionsResponse> {
        val cacheEntry: CacheEntry? = this.cacheEntry
        return when {
            cacheEntry != null && (locale == null || cacheEntry.locale == locale) -> Result.success(cacheEntry.regionsResponse)
            else -> Result.failure(CacheError(locale))
        }
    }
}