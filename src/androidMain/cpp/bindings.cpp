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
        }
    );
    if (regionsMetadata == nullptr) {
        LOGI("Invalid regionsMetadata");
        return nullptr;
    }

    // Prepare the base structure of the object we are mapping to
    jobject regionsResponseObject = RegionsResponse::getObject(env);

    jobject groupsHashMapObject = MapUtils::getObject(env);
    jobject regionsArrayListObject = ListUtils::getObject(env);

    jobject ovpnUdpProtocolDetailsPortsHashSetObject = SetUtils::getObject(env);
    jobject ovpnTcpProtocolDetailsPortsHashSetObject = SetUtils::getObject(env);
    jobject ikev2ProtocolDetailsPortsHashSetObject = SetUtils::getObject(env);
    jobject wgProtocolDetailsPortsHashSetObject = SetUtils::getObject(env);
    jobject metaProtocolDetailsPortsHashSetObject = SetUtils::getObject(env);

    // Iterate and adapt the region details
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

        jobject serversHashMapObject = MapUtils::getObject(env);
        jobject ovpnUdpServersArrayListObject = ListUtils::getObject(env);
        jobject ovpnTcpServersArrayListObject = ListUtils::getObject(env);
        jobject ikev2ServersArrayListObject = ListUtils::getObject(env);
        jobject wgServersArrayListObject = ListUtils::getObject(env);
        jobject metaServersArrayListObject = ListUtils::getObject(env);

        MapUtils::putObject(env, serversHashMapObject, env->NewStringUTF("ovpnudp"), ovpnUdpServersArrayListObject);
        MapUtils::putObject(env, serversHashMapObject, env->NewStringUTF("ovpntcp"), ovpnTcpServersArrayListObject);
        MapUtils::putObject(env, serversHashMapObject, env->NewStringUTF("ikev2"), ikev2ServersArrayListObject);
        MapUtils::putObject(env, serversHashMapObject, env->NewStringUTF("wg"), wgServersArrayListObject);
        MapUtils::putObject(env, serversHashMapObject, env->NewStringUTF("meta"), metaServersArrayListObject);

        // Iterate and adapt the region's server details
        KACArraySlice servers = KARRegionServers(region);
        while (servers.size) {
            jobject serverDetailsObject = ServerDetails::getObject(env);
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
            const KACStringSlice cn = KARServerCommonName(server);

            ServerDetails::setIpValue(env, serverDetailsObject, env->NewStringUTF(ipv4Address));
            ServerDetails::setCnValue(env, serverDetailsObject, env->NewStringUTF(cn.data));
            ServerDetails::setVanValue(env, serverDetailsObject, false);

            // Store the protocols/port supported by the server in order to put together the legacy "groups" structure
            const bool serverHasOpenVpnUdp = KARServerHasOpenVpnUdp(server);
            const bool serverHasOpenVpnTcp = KARServerHasOpenVpnTcp(server);
            const bool serverHasWireguard = KARServerHasWireGuard(server);
            const bool serverHasIkev2 = KARServerHasIkev2(server);
            const bool serverHasMeta = KARServerHasMeta(server);

            if (serverHasOpenVpnUdp) {
                ServerDetails::setVanValue(env, serverDetailsObject, KARServerOpenVpnUdpNcp(server));
                ListUtils::addValue(env, ovpnUdpServersArrayListObject, serverDetailsObject);
            } else if (serverHasOpenVpnTcp) {
                ServerDetails::setVanValue(env, serverDetailsObject, KARServerOpenVpnTcpNcp(server));
                ListUtils::addValue(env, ovpnTcpServersArrayListObject, serverDetailsObject);
            } else if (serverHasWireguard) {
                ListUtils::addValue(env, wgServersArrayListObject, serverDetailsObject);
            } else if (serverHasIkev2) {
                ListUtils::addValue(env, ikev2ServersArrayListObject, serverDetailsObject);
            } else if (serverHasMeta) {
                ListUtils::addValue(env, metaServersArrayListObject, serverDetailsObject);
            }

            KACPortArray ovpnUdpPorts = KARServerOpenVpnUdpPorts(server);
            for (size_t portIdx = 0; portIdx < ovpnUdpPorts.size; portIdx++) {
                jobject portObject = IntegerUtils::getObject(env, (jint)ovpnUdpPorts.data[portIdx]);
                SetUtils::addValue(env, ovpnUdpProtocolDetailsPortsHashSetObject, portObject);
            }

            KACPortArray ovpnTcpPorts = KARServerOpenVpnTcpPorts(server);
            for (size_t portIdx = 0; portIdx < ovpnTcpPorts.size; portIdx++) {
                jobject portObject = IntegerUtils::getObject(env, (jint)ovpnTcpPorts.data[portIdx]);
                SetUtils::addValue(env, ovpnTcpProtocolDetailsPortsHashSetObject, portObject);
            }

            KACPortArray wgPorts = KARServerWireGuardPorts(server);
            for (size_t portIdx = 0; portIdx < wgPorts.size; portIdx++) {
                jobject portObject = IntegerUtils::getObject(env, (jint)wgPorts.data[portIdx]);
                SetUtils::addValue(env, wgProtocolDetailsPortsHashSetObject, portObject);
            }

            KACPortArray metaPorts = KARServerMetaPorts(server);
            for (size_t portIdx = 0; portIdx < metaPorts.size; portIdx++) {
                jobject portObject = IntegerUtils::getObject(env, (jint)metaPorts.data[portIdx]);
                SetUtils::addValue(env, metaProtocolDetailsPortsHashSetObject, portObject);
            }

            advance(servers);
        }

        Region::setServersValue(env, regionObject, serversHashMapObject);
        ListUtils::addValue(env, regionsArrayListObject, regionObject);

        advance(regions);
    }

    // Iterate and adapt the groups details
    jobject ovpnUdpProtocolDetailsObject = ProtocolDetails::getObject(env);
    jobject ovpnTcpProtocolDetailsObject = ProtocolDetails::getObject(env);
    jobject ikev2ProtocolDetailsObject = ProtocolDetails::getObject(env);
    jobject wgProtocolDetailsObject = ProtocolDetails::getObject(env);
    jobject metaProtocolDetailsObject = ProtocolDetails::getObject(env);

    jobject ovpnUdpGroupArrayListObject = ListUtils::getObject(env);
    jobject ovpnTcpGroupArrayListObject = ListUtils::getObject(env);
    jobject ikev2GroupArrayListObject = ListUtils::getObject(env);
    jobject wgGroupArrayListObject = ListUtils::getObject(env);
    jobject metaGroupArrayListObject = ListUtils::getObject(env);

    jobject ovpnUdpProtocolDetailsPortsArrayListObject = ListUtils::getObject(env);
    jobject ovpnTcpProtocolDetailsPortsArrayListObject = ListUtils::getObject(env);
    jobject ikev2ProtocolDetailsPortsArrayListObject = ListUtils::getObject(env);
    jobject wgProtocolDetailsPortsArrayListObject = ListUtils::getObject(env);
    jobject metaProtocolDetailsPortsArrayListObject = ListUtils::getObject(env);

    ListUtils::addValue(env, ovpnUdpGroupArrayListObject, ovpnUdpProtocolDetailsObject);
    ListUtils::addValue(env, ovpnTcpGroupArrayListObject, ovpnTcpProtocolDetailsObject);
    ListUtils::addValue(env, ikev2GroupArrayListObject, ikev2ProtocolDetailsObject);
    ListUtils::addValue(env, wgGroupArrayListObject, wgProtocolDetailsObject);
    ListUtils::addValue(env, metaGroupArrayListObject, metaProtocolDetailsObject);

    MapUtils::putObject(env, groupsHashMapObject, env->NewStringUTF("ovpnudp"), ovpnUdpGroupArrayListObject);
    MapUtils::putObject(env, groupsHashMapObject, env->NewStringUTF("ovpntcp"), ovpnTcpGroupArrayListObject);
    MapUtils::putObject(env, groupsHashMapObject, env->NewStringUTF("ikev2"), ikev2GroupArrayListObject);
    MapUtils::putObject(env, groupsHashMapObject, env->NewStringUTF("wg"), wgGroupArrayListObject);
    MapUtils::putObject(env, groupsHashMapObject, env->NewStringUTF("meta"), metaGroupArrayListObject);

    jobject ovpnUdpProtocolDetailsPortsHashSetIteratorObject = SetUtils::getIterator(env, ovpnUdpProtocolDetailsPortsHashSetObject);
    while (IteratorUtils::getHasNextValue(env, ovpnUdpProtocolDetailsPortsHashSetIteratorObject)) {
        jobject ovpnUdpProtocolDetailsPortIntegerObject = IteratorUtils::getNextValue(env, ovpnUdpProtocolDetailsPortsHashSetIteratorObject);
        ListUtils::addValue(env, ovpnUdpProtocolDetailsPortsArrayListObject, ovpnUdpProtocolDetailsPortIntegerObject);
        ProtocolDetails::setPortsValue(env, ovpnUdpProtocolDetailsObject, ovpnUdpProtocolDetailsPortsArrayListObject);
    }

    jobject ovpnTcpProtocolDetailsPortsHashSetIteratorObject = SetUtils::getIterator(env, ovpnTcpProtocolDetailsPortsHashSetObject);
    while (IteratorUtils::getHasNextValue(env, ovpnTcpProtocolDetailsPortsHashSetIteratorObject)) {
        jobject ovpnTcpProtocolDetailsPortIntegerObject = IteratorUtils::getNextValue(env, ovpnTcpProtocolDetailsPortsHashSetIteratorObject);
        ListUtils::addValue(env, ovpnTcpProtocolDetailsPortsArrayListObject, ovpnTcpProtocolDetailsPortIntegerObject);
        ProtocolDetails::setPortsValue(env, ovpnTcpProtocolDetailsObject, ovpnTcpProtocolDetailsPortsArrayListObject);
    }

    jobject wgProtocolDetailsPortsHashSetIteratorObject = SetUtils::getIterator(env, wgProtocolDetailsPortsHashSetObject);
    while (IteratorUtils::getHasNextValue(env, wgProtocolDetailsPortsHashSetIteratorObject)) {
        jobject wgProtocolDetailsPortIntegerObject = IteratorUtils::getNextValue(env, wgProtocolDetailsPortsHashSetIteratorObject);
        ListUtils::addValue(env, wgProtocolDetailsPortsArrayListObject, wgProtocolDetailsPortIntegerObject);
        ProtocolDetails::setPortsValue(env, wgProtocolDetailsObject, wgProtocolDetailsPortsArrayListObject);
    }

    jobject metaProtocolDetailsPortsHashSetIteratorObject = SetUtils::getIterator(env, metaProtocolDetailsPortsHashSetObject);
    while (IteratorUtils::getHasNextValue(env, metaProtocolDetailsPortsHashSetIteratorObject)) {
        jobject metaProtocolDetailsPortIntegerObject = IteratorUtils::getNextValue(env, metaProtocolDetailsPortsHashSetIteratorObject);
        ListUtils::addValue(env, metaProtocolDetailsPortsArrayListObject, metaProtocolDetailsPortIntegerObject);
        ProtocolDetails::setPortsValue(env, metaProtocolDetailsObject, metaProtocolDetailsPortsArrayListObject);
    }

    // Add the regions to the response
    RegionsResponse::setRegionsValue(env, regionsResponseObject, regionsArrayListObject);

    // Add the groups to the response
    RegionsResponse::setGroupsValue(env, regionsResponseObject, groupsHashMapObject);

    KARRegionListDestroy(decodedRegionList);
    KARMetadataDestroy(regionsMetadata);
	env->ReleaseStringUTFChars(regionsJson, regionsJsonStr);
	env->ReleaseStringUTFChars(regionsMetadataJson, regionsMetadataStr);
	return regionsResponseObject;
}

#ifdef __cplusplus
}
#endif
