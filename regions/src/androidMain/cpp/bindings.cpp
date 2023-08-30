#include <jni.h>
#include <regionlist.h>
#include <metadata.h>
#include <utils.h>
#include <arpa/inet.h>

#include <android/log.h>
#define TAG "regions_bindings"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__))\

#ifdef __cplusplus
extern "C" {
#endif

#define OVPNUDP_KEY "openvpn_udp"
#define OVPNTCP_KEY "openvpn_tcp"
#define WIREGUARD_KEY "wireguard"
#define IKEV2_KEY "ikev2"
#define META_KEY "meta"

JNIEXPORT jobject JNICALL
Java_com_privateinternetaccess_regions_internals_RegionsArtifact_decodeRegions(
    JNIEnv *env,
    jclass clazz,
    jstring regionsMetadataJson,
    jstring regionsJson,
    jstring locale
) {
    // Transform locale
	const char *localeStr = env->GetStringUTFChars(locale, 0);
	size_t localeLen = env->GetStringUTFLength(locale);

    // Transform regions json
	const char *regionsJsonStr = env->GetStringUTFChars(regionsJson, 0);
	size_t regionsJsonLen = env->GetStringUTFLength(regionsJson);
    const KARRegionList *decodedRegionList = KARRegionListCreatePiav6(
        (struct KACStringSlice){
            regionsJsonStr,
            regionsJsonLen
        },
        (struct KACStringSlice){
            nullptr,
            0
        },
        nullptr,
        0,
        nullptr,
        0
    );
    if (decodedRegionList == nullptr) {
        LOGI("Invalid list of regions");
        return nullptr;
    }

    // Transform metadata json
	const char *regionsMetadataStr = env->GetStringUTFChars(regionsMetadataJson, 0);
	size_t regionsMetadataLen = env->GetStringUTFLength(regionsMetadataJson);
    const KARMetadata *regionsMetadata = KARMetadataCreatePiav6v2(
        (struct KACStringSlice){
            regionsJsonStr,
            regionsJsonLen
        },
        (struct KACStringSlice){
            regionsMetadataStr,
            regionsMetadataLen
        },
        nullptr,
        0,
        nullptr,
        0
    );
    if (regionsMetadata == nullptr) {
        LOGI("Invalid regionsMetadata");
        return nullptr;
    }

    // Prepare the base structure of the object we are mapping to
    jobject regionsResponseObject = RegionsResponse::getObject(env);

    // Iterate and adapt the region details
    jobject regionsArrayListObject = ListUtils::getObject(env);
    KACArraySlice regions = KARRegionListRegions(decodedRegionList);
    while (regions.size) {
        jobject regionObject = Region::getObject(env);
        const KARRegion *region = *reinterpret_cast<const KARRegion * const *>(regions.data);
        if (region == nullptr) {
            LOGI("Invalid region");
            continue;
        }

        const KACStringSlice regionId = KARRegionId(region);
        const KARRegionDisplay *regionDisplay = KARMetadataGetRegionDisplay(regionsMetadata, regionId);
        const KACStringSlice regionDisplayName = KARDisplayTextGetLanguageText(
            KARRegionDisplayName(regionDisplay),
            (struct KACStringSlice){
                localeStr,
                localeLen
            }
        );
        const KACStringSlice regionDisplayCountry = KARRegionDisplayCountry(regionDisplay);
        const double latitude = KARRegionDisplayGeoLatitude(regionDisplay);
        const double longitude = KARRegionDisplayGeoLongitude(regionDisplay);
        const bool regionPortForward = KARRegionPortForward(region);
        const bool regionGeoLocated = KARRegionGeoLocated(region);
        const bool regionAutoSafe = KARRegionAutoSafe(region);
        const bool regionOffline = KARRegionOffline(region);

        Region::setIdValue(env, regionObject, env->NewStringUTF(regionId.data));
        Region::setNameValue(env, regionObject, env->NewStringUTF(regionDisplayName.data));
        Region::setCountryValue(env, regionObject, env->NewStringUTF(regionDisplayCountry.data));
        Region::setLatitudeValue(env, regionObject, DoubleUtils::toString(env, latitude));
        Region::setLongitudeValue(env, regionObject, DoubleUtils::toString(env, longitude));
        Region::setPortForwardingValue(env, regionObject, regionPortForward);
        Region::setGeoValue(env, regionObject, regionGeoLocated);
        Region::setAutoRegionValue(env, regionObject, regionAutoSafe);
        Region::setOfflineValue(env, regionObject, regionOffline);

        // Iterate and adapt the region's server details
        jobject serversArrayListObject = ListUtils::getObject(env);
        KACArraySlice servers = KARRegionServers(region);
        while (servers.size) {
            jobject serverObject = Server::getObject(env);
            const KARServer *server = *reinterpret_cast<const KARServer * const *>(servers.data);
            if (server == nullptr) {
                LOGI("Invalid server");
                continue;
            }

            const KACIPv4Address ipv4 = ntohl(KARServerIpAddress(server));
            char ipv4Address[INET_ADDRSTRLEN];
            if (inet_ntop(AF_INET, &ipv4, ipv4Address, sizeof(ipv4Address)) == nullptr) {
                LOGI("Error obtaining server ip");
                continue;
            }
            const KACStringSlice serverCn = KARServerCommonName(server);
            const KACStringSlice serverFqdn = KARServerFqdn(server);

            Server::setIpValue(env, serverObject, env->NewStringUTF(ipv4Address));
            Server::setCnValue(env, serverObject, env->NewStringUTF(serverCn.data));
            Server::setFqdnValue(env, serverObject, env->NewStringUTF(serverFqdn.data));

            // Get the services supported by the server
            jobject servicesArrayListObject = ListUtils::getObject(env);
            const bool serverHasOpenVpnUdp = KARServerHasOpenVpnUdp(server);
            const bool serverHasOpenVpnTcp = KARServerHasOpenVpnTcp(server);
            const bool serverHasWireguard = KARServerHasWireGuard(server);
            const bool serverHasIkev2 = KARServerHasIkev2(server);
            const bool serverHasMeta = KARServerHasMeta(server);

            jobject ovpnUdpPortsArrayListObject = ListUtils::getObject(env);
            jobject ovpnTcpPortsArrayListObject = ListUtils::getObject(env);
            jobject ikev2PortsArrayListObject = ListUtils::getObject(env);
            jobject wgPortsArrayListObject = ListUtils::getObject(env);
            jobject metaPortsArrayListObject = ListUtils::getObject(env);

            if (serverHasOpenVpnUdp) {
                jobject service = Service::getObject(env);
                KACPortArray ovpnUdpPorts = KARServerOpenVpnUdpPorts(server);
                for (size_t portIdx = 0; portIdx < ovpnUdpPorts.size; portIdx++) {
                    jobject portObject = IntegerUtils::getObject(env, (jint)ovpnUdpPorts.data[portIdx]);
                    ListUtils::addValue(env, ovpnUdpPortsArrayListObject, portObject);
                }
                Service::setServiceValue(env, service, env->NewStringUTF(OVPNUDP_KEY));
                Service::setPortsValue(env, service, ovpnUdpPortsArrayListObject);
                Service::setNcpValue(env, service, KARServerOpenVpnUdpNcp(server));
                ListUtils::addValue(env, servicesArrayListObject, service);
            }

            if (serverHasOpenVpnTcp) {
                jobject service = Service::getObject(env);
                KACPortArray ovpnTcpPorts = KARServerOpenVpnTcpPorts(server);
                for (size_t portIdx = 0; portIdx < ovpnTcpPorts.size; portIdx++) {
                    jobject portObject = IntegerUtils::getObject(env, (jint)ovpnTcpPorts.data[portIdx]);
                    ListUtils::addValue(env, ovpnTcpPortsArrayListObject, portObject);
                }
                Service::setServiceValue(env, service, env->NewStringUTF(OVPNTCP_KEY));
                Service::setPortsValue(env, service, ovpnTcpPortsArrayListObject);
                Service::setNcpValue(env, service, KARServerOpenVpnTcpNcp(server));
                ListUtils::addValue(env, servicesArrayListObject, service);
            }

            if (serverHasWireguard) {
                jobject service = Service::getObject(env);
                KACPortArray wgPorts = KARServerWireGuardPorts(server);
                for (size_t portIdx = 0; portIdx < wgPorts.size; portIdx++) {
                    jobject portObject = IntegerUtils::getObject(env, (jint)wgPorts.data[portIdx]);
                    ListUtils::addValue(env, wgPortsArrayListObject, portObject);
                }
                Service::setServiceValue(env, service, env->NewStringUTF(WIREGUARD_KEY));
                Service::setPortsValue(env, service, wgPortsArrayListObject);
                Service::setNcpValue(env, service, false);
                ListUtils::addValue(env, servicesArrayListObject, service);
            }

            if (serverHasMeta) {
                jobject service = Service::getObject(env);
                KACPortArray metaPorts = KARServerMetaPorts(server);
                for (size_t portIdx = 0; portIdx < metaPorts.size; portIdx++) {
                    jobject portObject = IntegerUtils::getObject(env, (jint)metaPorts.data[portIdx]);
                    ListUtils::addValue(env, metaPortsArrayListObject, portObject);
                }
                Service::setServiceValue(env, service, env->NewStringUTF(META_KEY));
                Service::setPortsValue(env, service, metaPortsArrayListObject);
                Service::setNcpValue(env, service, false);
                ListUtils::addValue(env, servicesArrayListObject, service);
            }

            if (serverHasIkev2) {
                jobject service = Service::getObject(env);
                Service::setServiceValue(env, service, env->NewStringUTF(IKEV2_KEY));
                Service::setPortsValue(env, service, ikev2PortsArrayListObject);
                Service::setNcpValue(env, service, false);
                ListUtils::addValue(env, servicesArrayListObject, service);
            }

            Server::setServicesValue(env, serverObject, servicesArrayListObject);
            ListUtils::addValue(env, serversArrayListObject, serverObject);

            advance(servers);
        }

        Region::setServersValue(env, regionObject, serversArrayListObject);
        ListUtils::addValue(env, regionsArrayListObject, regionObject);

        advance(regions);
    }

    // Get the dynamic roles
    KACArraySlice dynamicRoles = KARMetadataDynamicRoles(regionsMetadata);
    jobject dynamicRolesArrayListObject = ListUtils::getObject(env);
    while (dynamicRoles.size) {
        jobject dynamicRoleObject = DynamicRole::getObject(env);
        const KARDynamicRole *dynamicRole = *reinterpret_cast<const KARDynamicRole * const *>(dynamicRoles.data);
        if (dynamicRole == nullptr) {
            LOGI("Invalid dynamic group");
            continue;
        }

        const KACStringSlice dynamicRoleId = KARDynamicRoleId(dynamicRole);
        const KACStringSlice dynamicRoleDisplayName = KARDisplayTextGetLanguageText(
            KARDynamicRoleName(dynamicRole),
            (struct KACStringSlice){
                localeStr,
                localeLen
            }
        );
        const KACStringSlice dynamicRoleResource = KARDynamicRoleResource(dynamicRole);
        const KACStringSlice dynamicRoleWinIcon = KARDynamicRoleWinIcon(dynamicRole);

        DynamicRole::setIdValue(env, dynamicRoleObject, env->NewStringUTF(dynamicRoleId.data));
        DynamicRole::setNameValue(env, dynamicRoleObject, env->NewStringUTF(dynamicRoleDisplayName.data));
        DynamicRole::setResourceValue(env, dynamicRoleObject, env->NewStringUTF(dynamicRoleResource.data));
        DynamicRole::setWinIconValue(env, dynamicRoleObject, env->NewStringUTF(dynamicRoleWinIcon.data));
        ListUtils::addValue(env, dynamicRolesArrayListObject, dynamicRoleObject);

        advance(dynamicRoles);
    }

    // Get the public dns servers
    KACArraySlice pubDnsServers = KARRegionListPublicDnsServers(decodedRegionList);
    jobject pubDnsServersArrayListObject = ListUtils::getObject(env);
    while (pubDnsServers.size) {
        const KACIPv4Address ipv4 = ntohl(*reinterpret_cast<const KACIPv4Address*>(pubDnsServers.data));
        char ipv4Address[INET_ADDRSTRLEN];
        if (inet_ntop(AF_INET, &ipv4, ipv4Address, sizeof(ipv4Address)) == nullptr) {
            LOGI("Error obtaining public dns server ip");
            continue;
        }
        ListUtils::addValue(env, pubDnsServersArrayListObject, env->NewStringUTF(ipv4Address));

        advance(pubDnsServers);
    }

    // Add the public dns servers to the response
    RegionsResponse::setPubDnsValue(env, regionsResponseObject, pubDnsServersArrayListObject);

    // Add the regions to the response
    RegionsResponse::setRegionsValue(env, regionsResponseObject, regionsArrayListObject);

    // Add the dynamic roles to the response
    RegionsResponse::setDynamicRolesValue(env, regionsResponseObject, dynamicRolesArrayListObject);

    KARRegionListDestroy(decodedRegionList);
    KARMetadataDestroy(regionsMetadata);
	env->ReleaseStringUTFChars(regionsJson, regionsJsonStr);
	env->ReleaseStringUTFChars(regionsMetadataJson, regionsMetadataStr);
	return regionsResponseObject;
}

#ifdef __cplusplus
}
#endif
