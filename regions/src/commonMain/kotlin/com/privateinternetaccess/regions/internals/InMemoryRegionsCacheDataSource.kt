package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.model.ShadowsocksRegionsResponse
import com.privateinternetaccess.regions.model.VpnRegionsResponse


internal class InMemoryRegionsCacheDataSource: RegionsCacheDataSource {
    private var vpnRegionsCacheEntry: CacheEntry? = null
    private var shadowsocksRegionsEntry: List<ShadowsocksRegionsResponse> = emptyList()

    override fun saveVpnRegions(locale: String, regionsResponse: VpnRegionsResponse) {
        this.vpnRegionsCacheEntry = CacheEntry(
            locale = locale,
            regionsResponse = regionsResponse
        )
    }

    override fun getVpnRegions(locale: String?): Result<VpnRegionsResponse> {
        val cacheEntry: CacheEntry? = this.vpnRegionsCacheEntry
        return when {
            cacheEntry != null && (locale == null || cacheEntry.locale == locale) -> Result.success(cacheEntry.regionsResponse)
            else -> Result.failure(CacheError(locale))
        }
    }

    override fun saveShadowsocksRegions(
        locale: String,
        response: List<ShadowsocksRegionsResponse>
    ) {
        this.shadowsocksRegionsEntry = response
    }

    override fun getShadowsocksRegions(
        locale: String?
    ): Result<List<ShadowsocksRegionsResponse>> {
        return when {
            shadowsocksRegionsEntry.isNotEmpty() -> Result.success(shadowsocksRegionsEntry)
            else -> Result.failure(Error("Shadowsocks entry is empty"))
        }
    }
}
