package com.privateinternetaccess.regions.internals

internal actual class PersistenceRegionsDataSource(
    preferenceName: String? = null,
    private val logErrors: Boolean
) : RegionsCacheDataSource, AbstractPersistenceRegionsDataSource() {

    init {
        throw UnsupportedOperationException("not yet implemented")
    }

    override fun logError(error: String) {
        if(logErrors) {
            error("$TAG: $error")
        }
    }

    override fun storeJsonEntry(jsonEntry: String) {
        throw UnsupportedOperationException("not yet implemented")
    }

    override fun retrieveJsonEntry(): String? {
        throw UnsupportedOperationException("not yet implemented")
    }

    companion object {
        private const val TAG: String = "PersistenceRegionsDataSource"
    }
}
