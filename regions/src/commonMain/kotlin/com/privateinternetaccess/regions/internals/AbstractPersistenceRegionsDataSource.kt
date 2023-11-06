package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.model.ShadowsocksRegionsResponse
import com.privateinternetaccess.regions.model.VpnRegionsResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal abstract class AbstractPersistenceRegionsDataSource : RegionsCacheDataSource {

    companion object {
        private const val VPN_REGIONS_ENTRY_KEY: String = "persist_region_entry"
        private const val SHADOWSOCKS_REGIONS_ENTRY_KEY: String = "shadowsocks-regions-entry-key"
    }

    final override fun saveVpnRegions(locale: String, response: VpnRegionsResponse) {
        val entry = CacheEntry(
            locale = locale,
            regionsResponse = response
        )
        val jsonEntry: String = try {
            Json.encodeToString(entry)
        } catch (t: Throwable) {
            logError(t.stackTraceToString())
            return
        }
        storeJsonEntry(key = VPN_REGIONS_ENTRY_KEY, jsonEntry = jsonEntry)
    }

    final override fun getVpnRegions(locale: String?): Result<VpnRegionsResponse> {
        val jsonEntry: String? = retrieveJsonEntry(key = VPN_REGIONS_ENTRY_KEY)
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

    final override fun saveShadowsocksRegions(
        locale: String,
        response: List<ShadowsocksRegionsResponse>
    ) {
        val jsonEntry: String = try {
            Json.encodeToString(response)
        } catch (t: Throwable) {
            logError(t.stackTraceToString())
            return
        }
        storeJsonEntry(key = SHADOWSOCKS_REGIONS_ENTRY_KEY, jsonEntry = jsonEntry)
    }

    final override fun getShadowsocksRegions(
        locale: String?
    ): Result<List<ShadowsocksRegionsResponse>> {
        val jsonEntry: String? = retrieveJsonEntry(key = SHADOWSOCKS_REGIONS_ENTRY_KEY)
        if (jsonEntry.isNullOrBlank()) {
            return Result.failure(Error("Unknown shadowsocks entry"))
        }

        val shadowsocksRegionsEntry: List<ShadowsocksRegionsResponse> = try {
            Json.decodeFromString(jsonEntry)
        } catch (t: Throwable) {
            logError(t.stackTraceToString())
            emptyList()
        }
        return when {
            shadowsocksRegionsEntry.isNotEmpty() -> Result.success(shadowsocksRegionsEntry)
            else -> Result.failure(Error("Shadowsocks entry is empty"))
        }
    }

    protected abstract fun logError(error: String)

    protected abstract fun storeJsonEntry(key: String, jsonEntry: String)

    protected abstract fun retrieveJsonEntry(key: String): String?
}
