@file:Suppress(names = ["CanSealedSubClassBeObject"])

package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.PlatformInstancesProvider
import com.privateinternetaccess.regions.model.RegionsResponse
import kotlinx.serialization.Serializable


internal class CacheError(locale: String?) : Throwable(message = if (locale.isNullOrBlank().not()) { "cache miss for locale = '$locale'" } else { "cache miss" })

internal interface RegionsDataSourceFactory {
    fun newInMemoryDataSource(): RegionsCacheDataSource
    fun newPersistenceRegionsDataSource(
        preferenceName: String?
    ): RegionsCacheDataSource

    companion object {
        const val DEFAULT_CACHE_ENTRY_KEY: String = "persist_region_entry"
    }
}

internal interface RegionsCacheDataSource {
    fun saveRegion(locale: String, regionsResponse: RegionsResponse)
    fun getRegion(locale: String?): Result<RegionsResponse>
}

@Serializable
internal data class CacheEntry(
    val locale: String,
    val regionsResponse: RegionsResponse
)

internal expect class RegionsDataSourceFactoryImpl(
    platformProvider: PlatformInstancesProvider?,
    logErrors: Boolean
): RegionsDataSourceFactory

internal expect class PersistenceRegionsDataSource : RegionsCacheDataSource
