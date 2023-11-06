package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.PlatformInstancesProvider
import com.privateinternetaccess.regions.model.ShadowsocksRegionsResponse
import com.privateinternetaccess.regions.model.VpnRegionsResponse
import kotlinx.serialization.Serializable


internal class CacheError(locale: String?) : Throwable(message = if (locale.isNullOrBlank().not()) { "cache miss for locale = '$locale'" } else { "cache miss" })

internal interface RegionsDataSourceFactory {
    fun newInMemoryDataSource(): RegionsCacheDataSource
    fun newPersistenceRegionsDataSource(preferenceName: String?): RegionsCacheDataSource
}

internal interface RegionsCacheDataSource {
    fun saveVpnRegions(locale: String, response: VpnRegionsResponse)
    fun getVpnRegions(locale: String?): Result<VpnRegionsResponse>
    fun saveShadowsocksRegions(locale: String, response: List<ShadowsocksRegionsResponse>)
    fun getShadowsocksRegions(locale: String?): Result<List<ShadowsocksRegionsResponse>>
}

@Serializable
internal data class CacheEntry(
    val locale: String,
    val regionsResponse: VpnRegionsResponse
)

internal expect class RegionsDataSourceFactoryImpl(
    platformProvider: PlatformInstancesProvider?,
    logErrors: Boolean
): RegionsDataSourceFactory

internal expect class PersistenceRegionsDataSource : RegionsCacheDataSource
