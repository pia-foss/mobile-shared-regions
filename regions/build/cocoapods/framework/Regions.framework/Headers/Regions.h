#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class RegionsPingRequestPlatformPingResult, RegionsRegionLowerLatencyInformation, RegionsRegionsResponse, RegionsKotlinError, RegionsRegionsProtocol, RegionsRegionsCommonBuilder, RegionsKotlinEnum, RegionsRegionsResponseProtocolDetails, RegionsRegionsResponseRegion, RegionsRegionsResponseRegionServerDetails, RegionsKotlinThrowable, RegionsKotlinArray, RegionsKotlinx_serialization_runtimeSerialKind, RegionsKotlinNothing, RegionsKotlinx_serialization_runtimeUpdateMode;

@protocol RegionsRegionsAPI, RegionsMessageVerificator, RegionsPingRequest, RegionsKotlinComparable, RegionsKotlinCoroutineContext, RegionsKotlinx_coroutines_coreCoroutineScope, RegionsKotlinx_serialization_runtimeKSerializer, RegionsKotlinCoroutineContextElement, RegionsKotlinCoroutineContextKey, RegionsKotlinx_serialization_runtimeEncoder, RegionsKotlinx_serialization_runtimeSerialDescriptor, RegionsKotlinx_serialization_runtimeSerializationStrategy, RegionsKotlinx_serialization_runtimeDecoder, RegionsKotlinx_serialization_runtimeDeserializationStrategy, RegionsKotlinIterator, RegionsKotlinx_serialization_runtimeCompositeEncoder, RegionsKotlinx_serialization_runtimeSerialModule, RegionsKotlinAnnotation, RegionsKotlinx_serialization_runtimeCompositeDecoder, RegionsKotlinx_serialization_runtimeSerialModuleCollector, RegionsKotlinKClass, RegionsKotlinKDeclarationContainer, RegionsKotlinKAnnotatedElement, RegionsKotlinKClassifier;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wnullability"

__attribute__((swift_name("KotlinBase")))
@interface RegionsBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface RegionsBase (RegionsBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface RegionsMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface RegionsMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorRegionsKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface RegionsNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface RegionsByte : RegionsNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface RegionsUByte : RegionsNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface RegionsShort : RegionsNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface RegionsUShort : RegionsNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface RegionsInt : RegionsNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface RegionsUInt : RegionsNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface RegionsLong : RegionsNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface RegionsULong : RegionsNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface RegionsFloat : RegionsNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface RegionsDouble : RegionsNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface RegionsBoolean : RegionsNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((swift_name("MessageVerificator")))
@protocol RegionsMessageVerificator
@required
- (BOOL)verifyMessageMessage:(NSString *)message key:(NSString *)key __attribute__((swift_name("verifyMessage(message:key:)")));
@end;

__attribute__((swift_name("PingRequest")))
@protocol RegionsPingRequest
@required
- (void)platformPingRequestEndpoints:(NSDictionary<NSString *, NSArray<NSString *> *> *)endpoints callback:(void (^)(NSDictionary<NSString *, NSArray<RegionsPingRequestPlatformPingResult *> *> *))callback __attribute__((swift_name("platformPingRequest(endpoints:callback:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PingRequestPlatformPingResult")))
@interface RegionsPingRequestPlatformPingResult : RegionsBase
- (instancetype)initWithEndpoint:(NSString *)endpoint latency:(int64_t)latency __attribute__((swift_name("init(endpoint:latency:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (int64_t)component2 __attribute__((swift_name("component2()")));
- (RegionsPingRequestPlatformPingResult *)doCopyEndpoint:(NSString *)endpoint latency:(int64_t)latency __attribute__((swift_name("doCopy(endpoint:latency:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *endpoint __attribute__((swift_name("endpoint")));
@property (readonly) int64_t latency __attribute__((swift_name("latency")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionLowerLatencyInformation")))
@interface RegionsRegionLowerLatencyInformation : RegionsBase
- (instancetype)initWithRegion:(NSString *)region endpoint:(NSString *)endpoint latency:(int64_t)latency __attribute__((swift_name("init(region:endpoint:latency:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (int64_t)component3 __attribute__((swift_name("component3()")));
- (RegionsRegionLowerLatencyInformation *)doCopyRegion:(NSString *)region endpoint:(NSString *)endpoint latency:(int64_t)latency __attribute__((swift_name("doCopy(region:endpoint:latency:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *endpoint __attribute__((swift_name("endpoint")));
@property (readonly) int64_t latency __attribute__((swift_name("latency")));
@property (readonly) NSString *region __attribute__((swift_name("region")));
@end;

__attribute__((swift_name("RegionsAPI")))
@protocol RegionsRegionsAPI
@required
- (void)fetchCallback:(void (^)(RegionsRegionsResponse * _Nullable, RegionsKotlinError * _Nullable))callback __attribute__((swift_name("fetch(callback:)")));
- (void)pingRequestsProtocol:(RegionsRegionsProtocol *)protocol callback:(void (^)(NSArray<RegionsRegionLowerLatencyInformation *> *, RegionsKotlinError * _Nullable))callback __attribute__((swift_name("pingRequests(protocol:callback:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsCommonBuilder")))
@interface RegionsRegionsCommonBuilder : RegionsBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (id<RegionsRegionsAPI>)build __attribute__((swift_name("build()")));
- (RegionsRegionsCommonBuilder *)setMessageVerificatorDependencyMessageVerificatorDependency:(id<RegionsMessageVerificator>)messageVerificatorDependency __attribute__((swift_name("setMessageVerificatorDependency(messageVerificatorDependency:)")));
- (RegionsRegionsCommonBuilder *)setPingRequestDependencyPingRequestDependency:(id<RegionsPingRequest>)pingRequestDependency __attribute__((swift_name("setPingRequestDependency(pingRequestDependency:)")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol RegionsKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface RegionsKotlinEnum : RegionsBase <RegionsKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(RegionsKotlinEnum *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsProtocol")))
@interface RegionsRegionsProtocol : RegionsKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) RegionsRegionsProtocol *openvpnTcp __attribute__((swift_name("openvpnTcp")));
@property (class, readonly) RegionsRegionsProtocol *openvpnUdp __attribute__((swift_name("openvpnUdp")));
@property (class, readonly) RegionsRegionsProtocol *wireguard __attribute__((swift_name("wireguard")));
- (int32_t)compareToOther:(RegionsRegionsProtocol *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) NSString *protocol __attribute__((swift_name("protocol")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsUtils")))
@interface RegionsRegionsUtils : RegionsBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)regionsUtils __attribute__((swift_name("init()")));
- (RegionsRegionsResponse *)parseRegionsResponseString:(NSString *)regionsResponseString __attribute__((swift_name("parse(regionsResponseString:)")));
- (NSString *)stringifyRegionsResponse:(RegionsRegionsResponse *)regionsResponse __attribute__((swift_name("stringify(regionsResponse:)")));
@property (readonly) NSString *GEN4_DEFAULT_RESPONSE __attribute__((swift_name("GEN4_DEFAULT_RESPONSE")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreCoroutineScope")))
@protocol RegionsKotlinx_coroutines_coreCoroutineScope
@required
@property (readonly) id<RegionsKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsCommon")))
@interface RegionsRegionsCommon : RegionsBase <RegionsRegionsAPI, RegionsKotlinx_coroutines_coreCoroutineScope>
- (instancetype)initWithPingDependency:(id<RegionsPingRequest>)pingDependency messageVerificator:(id<RegionsMessageVerificator>)messageVerificator __attribute__((swift_name("init(pingDependency:messageVerificator:)"))) __attribute__((objc_designated_initializer));
- (void)fetchCallback:(void (^)(RegionsRegionsResponse * _Nullable, RegionsKotlinError * _Nullable))callback __attribute__((swift_name("fetch(callback:)")));
- (void)pingRequestsProtocol:(RegionsRegionsProtocol *)protocol callback:(void (^)(NSArray<RegionsRegionLowerLatencyInformation *> *, RegionsKotlinError * _Nullable))callback __attribute__((swift_name("pingRequests(protocol:callback:)")));
- (RegionsRegionsResponse *)serializeJsonResponse:(NSString *)jsonResponse __attribute__((swift_name("serialize(jsonResponse:)")));
@property (readonly) id<RegionsKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsCommon.Companion")))
@interface RegionsRegionsCommonCompanion : RegionsBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse")))
@interface RegionsRegionsResponse : RegionsBase
- (instancetype)initWithGroups:(NSDictionary<NSString *, NSArray<RegionsRegionsResponseProtocolDetails *> *> *)groups regions:(NSArray<RegionsRegionsResponseRegion *> *)regions __attribute__((swift_name("init(groups:regions:)"))) __attribute__((objc_designated_initializer));
- (NSDictionary<NSString *, NSArray<RegionsRegionsResponseProtocolDetails *> *> *)component1 __attribute__((swift_name("component1()")));
- (NSArray<RegionsRegionsResponseRegion *> *)component2 __attribute__((swift_name("component2()")));
- (RegionsRegionsResponse *)doCopyGroups:(NSDictionary<NSString *, NSArray<RegionsRegionsResponseProtocolDetails *> *> *)groups regions:(NSArray<RegionsRegionsResponseRegion *> *)regions __attribute__((swift_name("doCopy(groups:regions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSDictionary<NSString *, NSArray<RegionsRegionsResponseProtocolDetails *> *> *groups __attribute__((swift_name("groups")));
@property (readonly) NSArray<RegionsRegionsResponseRegion *> *regions __attribute__((swift_name("regions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.Companion")))
@interface RegionsRegionsResponseCompanion : RegionsBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.ProtocolDetails")))
@interface RegionsRegionsResponseProtocolDetails : RegionsBase
- (instancetype)initWithName:(NSString *)name ports:(NSArray<RegionsInt *> *)ports __attribute__((swift_name("init(name:ports:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<RegionsInt *> *)component2 __attribute__((swift_name("component2()")));
- (RegionsRegionsResponseProtocolDetails *)doCopyName:(NSString *)name ports:(NSArray<RegionsInt *> *)ports __attribute__((swift_name("doCopy(name:ports:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSArray<RegionsInt *> *ports __attribute__((swift_name("ports")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.ProtocolDetailsCompanion")))
@interface RegionsRegionsResponseProtocolDetailsCompanion : RegionsBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.Region")))
@interface RegionsRegionsResponseRegion : RegionsBase
- (instancetype)initWithId:(NSString *)id name:(NSString *)name country:(NSString *)country dns:(NSString *)dns geo:(BOOL)geo autoRegion:(BOOL)autoRegion portForward:(BOOL)portForward proxy:(NSArray<NSString *> *)proxy servers:(NSDictionary<NSString *, NSArray<RegionsRegionsResponseRegionServerDetails *> *> *)servers __attribute__((swift_name("init(id:name:country:dns:geo:autoRegion:portForward:proxy:servers:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString *)component4 __attribute__((swift_name("component4()")));
- (BOOL)component5 __attribute__((swift_name("component5()")));
- (BOOL)component6 __attribute__((swift_name("component6()")));
- (BOOL)component7 __attribute__((swift_name("component7()")));
- (NSArray<NSString *> *)component8 __attribute__((swift_name("component8()")));
- (NSDictionary<NSString *, NSArray<RegionsRegionsResponseRegionServerDetails *> *> *)component9 __attribute__((swift_name("component9()")));
- (RegionsRegionsResponseRegion *)doCopyId:(NSString *)id name:(NSString *)name country:(NSString *)country dns:(NSString *)dns geo:(BOOL)geo autoRegion:(BOOL)autoRegion portForward:(BOOL)portForward proxy:(NSArray<NSString *> *)proxy servers:(NSDictionary<NSString *, NSArray<RegionsRegionsResponseRegionServerDetails *> *> *)servers __attribute__((swift_name("doCopy(id:name:country:dns:geo:autoRegion:portForward:proxy:servers:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL autoRegion __attribute__((swift_name("autoRegion")));
@property (readonly) NSString *country __attribute__((swift_name("country")));
@property (readonly) NSString *dns __attribute__((swift_name("dns")));
@property (readonly) BOOL geo __attribute__((swift_name("geo")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) BOOL portForward __attribute__((swift_name("portForward")));
@property (readonly) NSArray<NSString *> *proxy __attribute__((swift_name("proxy")));
@property (readonly) NSDictionary<NSString *, NSArray<RegionsRegionsResponseRegionServerDetails *> *> *servers __attribute__((swift_name("servers")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.RegionCompanion")))
@interface RegionsRegionsResponseRegionCompanion : RegionsBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.RegionServerDetails")))
@interface RegionsRegionsResponseRegionServerDetails : RegionsBase
- (instancetype)initWithIp:(NSString *)ip cn:(NSString *)cn __attribute__((swift_name("init(ip:cn:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (RegionsRegionsResponseRegionServerDetails *)doCopyIp:(NSString *)ip cn:(NSString *)cn __attribute__((swift_name("doCopy(ip:cn:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *cn __attribute__((swift_name("cn")));
@property (readonly) NSString *ip __attribute__((swift_name("ip")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegionsResponse.RegionServerDetailsCompanion")))
@interface RegionsRegionsResponseRegionServerDetailsCompanion : RegionsBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("KotlinThrowable")))
@interface RegionsKotlinThrowable : RegionsBase
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(RegionsKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(RegionsKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (RegionsKotlinArray *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) RegionsKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
@end;

__attribute__((swift_name("KotlinError")))
@interface RegionsKotlinError : RegionsKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(RegionsKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(RegionsKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinCoroutineContext")))
@protocol RegionsKotlinCoroutineContext
@required
- (id _Nullable)foldInitial:(id _Nullable)initial operation:(id _Nullable (^)(id _Nullable, id<RegionsKotlinCoroutineContextElement>))operation __attribute__((swift_name("fold(initial:operation:)")));
- (id<RegionsKotlinCoroutineContextElement> _Nullable)getKey:(id<RegionsKotlinCoroutineContextKey>)key __attribute__((swift_name("get(key:)")));
- (id<RegionsKotlinCoroutineContext>)minusKeyKey:(id<RegionsKotlinCoroutineContextKey>)key __attribute__((swift_name("minusKey(key:)")));
- (id<RegionsKotlinCoroutineContext>)plusContext:(id<RegionsKotlinCoroutineContext>)context __attribute__((swift_name("plus(context:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerializationStrategy")))
@protocol RegionsKotlinx_serialization_runtimeSerializationStrategy
@required
- (void)serializeEncoder:(id<RegionsKotlinx_serialization_runtimeEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<RegionsKotlinx_serialization_runtimeSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeDeserializationStrategy")))
@protocol RegionsKotlinx_serialization_runtimeDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<RegionsKotlinx_serialization_runtimeDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (id _Nullable)patchDecoder:(id<RegionsKotlinx_serialization_runtimeDecoder>)decoder old:(id _Nullable)old __attribute__((swift_name("patch(decoder:old:)")));
@property (readonly) id<RegionsKotlinx_serialization_runtimeSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeKSerializer")))
@protocol RegionsKotlinx_serialization_runtimeKSerializer <RegionsKotlinx_serialization_runtimeSerializationStrategy, RegionsKotlinx_serialization_runtimeDeserializationStrategy>
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface RegionsKotlinArray : RegionsBase
+ (instancetype)arrayWithSize:(int32_t)size init:(id _Nullable (^)(RegionsInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (id _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<RegionsKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(id _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("KotlinCoroutineContextElement")))
@protocol RegionsKotlinCoroutineContextElement <RegionsKotlinCoroutineContext>
@required
@property (readonly) id<RegionsKotlinCoroutineContextKey> key __attribute__((swift_name("key")));
@end;

__attribute__((swift_name("KotlinCoroutineContextKey")))
@protocol RegionsKotlinCoroutineContextKey
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeEncoder")))
@protocol RegionsKotlinx_serialization_runtimeEncoder
@required
- (id<RegionsKotlinx_serialization_runtimeCompositeEncoder>)beginCollectionDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize typeSerializers:(RegionsKotlinArray *)typeSerializers __attribute__((swift_name("beginCollection(descriptor:collectionSize:typeSerializers:)")));
- (id<RegionsKotlinx_serialization_runtimeCompositeEncoder>)beginStructureDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor typeSerializers:(RegionsKotlinArray *)typeSerializers __attribute__((swift_name("beginStructure(descriptor:typeSerializers:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));
- (void)encodeNull __attribute__((swift_name("encodeNull()")));
- (void)encodeNullableSerializableValueSerializer:(id<RegionsKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<RegionsKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
- (void)encodeUnit __attribute__((swift_name("encodeUnit()")));
@property (readonly) id<RegionsKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialDescriptor")))
@protocol RegionsKotlinx_serialization_runtimeSerialDescriptor
@required
- (NSArray<id<RegionsKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));
- (id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));
- (NSArray<id<RegionsKotlinAnnotation>> *)getEntityAnnotations __attribute__((swift_name("getEntityAnnotations()"))) __attribute__((deprecated("Deprecated in the favour of 'annotations' property")));
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));
@property (readonly) NSArray<id<RegionsKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));
@property (readonly) RegionsKotlinx_serialization_runtimeSerialKind *kind __attribute__((swift_name("kind")));
@property (readonly) NSString *name __attribute__((swift_name("name"))) __attribute__((unavailable("name property deprecated in the favour of serialName")));
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeDecoder")))
@protocol RegionsKotlinx_serialization_runtimeDecoder
@required
- (id<RegionsKotlinx_serialization_runtimeCompositeDecoder>)beginStructureDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor typeParams:(RegionsKotlinArray *)typeParams __attribute__((swift_name("beginStructure(descriptor:typeParams:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));
- (RegionsKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
- (void)decodeUnit __attribute__((swift_name("decodeUnit()")));
- (id _Nullable)updateNullableSerializableValueDeserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateNullableSerializableValue(deserializer:old:)")));
- (id _Nullable)updateSerializableValueDeserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateSerializableValue(deserializer:old:)")));
@property (readonly) id<RegionsKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@property (readonly) RegionsKotlinx_serialization_runtimeUpdateMode *updateMode __attribute__((swift_name("updateMode")));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol RegionsKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeCompositeEncoder")))
@protocol RegionsKotlinx_serialization_runtimeCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (void)encodeIntElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));
- (void)encodeNonSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(id)value __attribute__((swift_name("encodeNonSerializableElement(descriptor:index:value:)"))) __attribute__((unavailable("This method is deprecated for removal. Please remove it from your implementation and delegate to default method instead")));
- (void)encodeNullableSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<RegionsKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<RegionsKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)encodeUnitElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeUnitElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) id<RegionsKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialModule")))
@protocol RegionsKotlinx_serialization_runtimeSerialModule
@required
- (void)dumpToCollector:(id<RegionsKotlinx_serialization_runtimeSerialModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer> _Nullable)getContextualKclass:(id<RegionsKotlinKClass>)kclass __attribute__((swift_name("getContextual(kclass:)")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer> _Nullable)getPolymorphicBaseClass:(id<RegionsKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));
- (id<RegionsKotlinx_serialization_runtimeKSerializer> _Nullable)getPolymorphicBaseClass:(id<RegionsKotlinKClass>)baseClass serializedClassName:(NSString *)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end;

__attribute__((swift_name("KotlinAnnotation")))
@protocol RegionsKotlinAnnotation
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialKind")))
@interface RegionsKotlinx_serialization_runtimeSerialKind : RegionsBase
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeCompositeDecoder")))
@protocol RegionsKotlinx_serialization_runtimeCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:)")));
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:)")));
- (int16_t)decodeShortElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)decodeUnitElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeUnitElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
- (id _Nullable)updateNullableSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateNullableSerializableElement(descriptor:index:deserializer:old:)")));
- (id _Nullable)updateSerializableElementDescriptor:(id<RegionsKotlinx_serialization_runtimeSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<RegionsKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateSerializableElement(descriptor:index:deserializer:old:)")));
@property (readonly) id<RegionsKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@property (readonly) RegionsKotlinx_serialization_runtimeUpdateMode *updateMode __attribute__((swift_name("updateMode")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface RegionsKotlinNothing : RegionsBase
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_runtimeUpdateMode")))
@interface RegionsKotlinx_serialization_runtimeUpdateMode : RegionsKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) RegionsKotlinx_serialization_runtimeUpdateMode *banned __attribute__((swift_name("banned")));
@property (class, readonly) RegionsKotlinx_serialization_runtimeUpdateMode *overwrite __attribute__((swift_name("overwrite")));
@property (class, readonly) RegionsKotlinx_serialization_runtimeUpdateMode *update __attribute__((swift_name("update")));
- (int32_t)compareToOther:(RegionsKotlinx_serialization_runtimeUpdateMode *)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialModuleCollector")))
@protocol RegionsKotlinx_serialization_runtimeSerialModuleCollector
@required
- (void)contextualKClass:(id<RegionsKotlinKClass>)kClass serializer:(id<RegionsKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<RegionsKotlinKClass>)baseClass actualClass:(id<RegionsKotlinKClass>)actualClass actualSerializer:(id<RegionsKotlinx_serialization_runtimeKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
@end;

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol RegionsKotlinKDeclarationContainer
@required
@end;

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol RegionsKotlinKAnnotatedElement
@required
@end;

__attribute__((swift_name("KotlinKClassifier")))
@protocol RegionsKotlinKClassifier
@required
@end;

__attribute__((swift_name("KotlinKClass")))
@protocol RegionsKotlinKClass <RegionsKotlinKDeclarationContainer, RegionsKotlinKAnnotatedElement, RegionsKotlinKClassifier>
@required
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end;

#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
