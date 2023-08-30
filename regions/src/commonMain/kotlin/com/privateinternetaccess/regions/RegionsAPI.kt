package com.privateinternetaccess.regions

/*
 *  Copyright (c) 2020 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Mobile Client.
 *
 *  The Private Internet Access Mobile Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Mobile Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Mobile Client.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.privateinternetaccess.regions.internals.Regions
import com.privateinternetaccess.regions.internals.RegionsCacheDataSource
import com.privateinternetaccess.regions.internals.RegionsDataSourceFactory
import com.privateinternetaccess.regions.internals.RegionsDataSourceFactoryImpl
import com.privateinternetaccess.regions.model.RegionsResponse


public enum class RegionsProtocol(val protocol: String) {
    OPENVPN_TCP("ovpntcp"),
    OPENVPN_UDP("ovpnudp"),
    WIREGUARD("wg"),
    META("meta")
}

/**
 * Const defining the timeout being used on the ping requests
 */
public const val REGIONS_PING_TIMEOUT: Int = 1500

/**
 * Interface defining the API to be offered by the common module.
 */
public interface RegionsAPI {

    /**
     * Fetch all regions information for next-gen.
     *
     * @param locale `String`. Regions locale. If unknown defaults to en-us.
     * @param callback `(response: RegionsResponse?, error: List<Error>)`. Invoked on the main thread.
     */
    fun fetchRegions(locale: String, callback: (response: RegionsResponse?, error: List<Error>) -> Unit)

    /**
     * Starts the process of ping requests and return the updated `ServerResponse` object as a
     * callback parameter.
     *
     * @param callback `(response: RegionsResponse?, error: List<Error>)`. Invoked on the main thread.
     */
    fun pingRequests(callback: (response: List<RegionLowerLatencyInformation>, error: List<Error>) -> Unit)
}

/**
 * Interface defining the client's endpoint provider.
 */
public interface IRegionEndpointProvider {

    /**
     * It returns the list of endpoints to try to reach when performing a request. Order is relevant.
     *
     * @return `List<RegionEndpoint>`
     */
    fun regionEndpoints(): List<RegionEndpoint>
}

/**
 * Platform specific interface to provide platform specific instances to the library.
 */
public expect interface PlatformInstancesProvider

@Throws(Exception::class)
internal expect fun checkInstancesProvider(provider: PlatformInstancesProvider?)

/**
 * Builder class responsible for creating an instance of an object conforming to
 * the `RegionsAPI` interface.
 */
public class RegionsBuilder {
    private var endpointsProvider: IRegionEndpointProvider? = null
    private var certificate: String? = null
    private var userAgent: String? = null
    private var regionsListRequestPath: String? = null
    private var metadataRequestPath: String? = null
    private var regionJsonFallback: RegionJsonFallback? = null
    private var platformInstancesProvider: PlatformInstancesProvider? = null
    private var persistencePreferenceName: String? = null
    private var logErrors: Boolean = false

    /**
     * It sets the endpoints provider, that is queried for the current endpoint list. Required.
     *
     * @param endpointsProvider `IEndPointProvider`.
     */
    fun setEndpointProvider(endpointsProvider: IRegionEndpointProvider): RegionsBuilder = apply {
        this.endpointsProvider = endpointsProvider
    }

    /**
     * It sets the certificate to use when using an endpoint with pinning enabled. Optional.
     *
     * @param certificate `String`.
     */
    fun setCertificate(certificate: String?): RegionsBuilder = apply {
        this.certificate = certificate
    }

    /**
     * It sets the User-Agent value to be used in the requests.
     *
     * @param userAgent `String`.
     */
    fun setUserAgent(userAgent: String): RegionsBuilder = apply {
        this.userAgent = userAgent
    }

    /**
     * It set the path to be used by the requests retrieving the regions list.
     *
     * @param regionsListRequestPath `String`.
     */
    fun setRegionsListRequestPath(regionsListRequestPath: String): RegionsBuilder = apply {
        this.regionsListRequestPath = regionsListRequestPath
    }

    /**
     * It set the path to be used by the requests retrieving the metadata.
     *
     * @param metadataRequestPath `String`.
     */
    fun setMetadataRequestPath(metadataRequestPath: String): RegionsBuilder = apply {
        this.metadataRequestPath = metadataRequestPath
    }

    /**
     * It sets the region json fallback object. Upon failure of the requests, cache and persistence.
     * This fallback will be used instead if defined.
     *
     * @param regionJsonFallback `String`.
     */
    fun setRegionJsonFallback(regionJsonFallback: RegionJsonFallback): RegionsBuilder = apply {
        this.regionJsonFallback = regionJsonFallback
    }

    /**
     * Provide platform specific instances to the library (e.g. provide the application context).
     */
    fun setPlatformInstancesProvider(provider: PlatformInstancesProvider) = apply {
        this.platformInstancesProvider = provider
    }

    /**
     * It defines the name to use by the persistence framework. Optional. If undefined, a default will be used instead.
     */
    fun setPersistencePreferenceName(name: String?) = apply {
        this.persistencePreferenceName = if (name.isNullOrBlank()) null else name.trim()
    }

    /**
     * It enables the internal logging. `false` by default.
     */
    fun setLogErrors(logErrors: Boolean) = apply {
        this.logErrors = logErrors
    }

    /**
     * @return `RegionsAPI` instance.
     */
    fun build(): RegionsAPI {
        val endpointsProvider = this.endpointsProvider
            ?: throw Exception("Endpoints provider missing.")
        val userAgent = this.userAgent
            ?: throw Exception("User-Agent value missing.")
        val regionsListRequestPath = this.regionsListRequestPath
            ?: throw Exception("Regions list request path missing.")
        val metadataRequestPath = this.metadataRequestPath
            ?: throw Exception("Metadata request path missing.")
        val platformInstancesProvider = this.platformInstancesProvider
        checkInstancesProvider(platformInstancesProvider)

        val regionsDataSourceFactory: RegionsDataSourceFactory = RegionsDataSourceFactoryImpl(
            platformProvider = platformInstancesProvider,
            logErrors = this.logErrors
        )
        val persistencePreferenceName: String? = this.persistencePreferenceName

        val inMemory: RegionsCacheDataSource = regionsDataSourceFactory.newInMemoryDataSource()
        val persistence: RegionsCacheDataSource = regionsDataSourceFactory.newPersistenceRegionsDataSource(
            preferenceName = persistencePreferenceName,
        )

        return Regions(
            userAgent = userAgent,
            regionsListRequestPath = regionsListRequestPath,
            metadataRequestPath = metadataRequestPath,
            regionJsonFallback = regionJsonFallback,
            endpointsProvider = endpointsProvider,
            certificate = certificate,
            inMemory = inMemory,
            persistence = persistence
        )
    }
}

/**
 * Data class defining the required json information for a successful fallback response.
 *
 * @param regionsJson `String`.
 * @param metadataJson `String`.
 */
public data class RegionJsonFallback(
    val regionsJson: String,
    val metadataJson: String
)

/**
 * Data class defining the response object for a ping request. @see `fun pingRequests(...)`.
 *
 * @param region `String`.
 * @param endpoint `String`.
 * @param latency `String`.
 */
public data class RegionLowerLatencyInformation(
    val region: String,
    val endpoint: String,
    val latency: Long
)

/**
 * Data class defining the endpoints data needed when performing a request on it.
 */
public data class RegionEndpoint(
    val endpoint: String,
    val isProxy: Boolean,
    val usePinnedCertificate: Boolean = false,
    val certificateCommonName: String? = null
)