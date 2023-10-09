package com.privateinternetaccess.regions.internals

import com.privateinternetaccess.regions.PlatformInstancesProvider

internal actual class RegionsDataSourceFactoryImpl actual constructor(
    platformProvider: PlatformInstancesProvider?,
    private val logErrors: Boolean
): RegionsDataSourceFactory {

    override fun newInMemoryDataSource(): RegionsCacheDataSource =
        InMemoryRegionsCacheDataSource()

    override fun newPersistenceRegionsDataSource(
        preferenceName: String?
    ): RegionsCacheDataSource = PersistenceRegionsDataSource(
        preferenceName = preferenceName,
        logErrors = logErrors
    )
}
