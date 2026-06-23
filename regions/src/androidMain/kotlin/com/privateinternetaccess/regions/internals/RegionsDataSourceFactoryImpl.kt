package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.PlatformInstancesProvider

internal actual class RegionsDataSourceFactoryImpl actual constructor(
    platformProvider: PlatformInstancesProvider?,
    private val logErrors: Boolean
): RegionsDataSourceFactory {
    private val platformProvider: PlatformInstancesProvider = platformProvider ?: throw NullPointerException("platform provider may not be null")

    override fun newInMemoryDataSource(): RegionsCacheDataSource = InMemoryRegionsCacheDataSource()

    override fun newPersistenceRegionsDataSource(
        preferenceName: String?
    ): RegionsCacheDataSource = PersistenceRegionsDataSource(
        provider = platformProvider,
        preferenceName = preferenceName,
        logErrors = logErrors
    )
}
