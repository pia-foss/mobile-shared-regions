#pragma once

#include <jni.h>
#include <assert.h>

#ifdef __cplusplus
extern "C" {
#endif

class RegionsResponse {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getRegionsFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "regions", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getDynamicRolesFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "dynamicRoles", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getPubDnsFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "pubdns", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setRegionsValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getRegionsFieldId(env), value);
        }

        static void setDynamicRolesValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getDynamicRolesFieldId(env), value);
        }

        static void setPubDnsValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getPubDnsFieldId(env), value);
        }
};

class DynamicRole {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse$DynamicRole");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getIdFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "id", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getNameFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "name", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getResourceFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "resource", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getWinIconFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "winIcon", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setIdValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getIdFieldId(env), value);
        }

        static void setNameValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getNameFieldId(env), value);
        }

        static void setResourceValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getResourceFieldId(env), value);
        }

        static void setWinIconValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getWinIconFieldId(env), value);
        }
};

class Region {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse$Region");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getIdFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "id", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getNameFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "name", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getCountryFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "country", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getLatitudeFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "latitude", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getLongitudeFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "longitude", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getPortForwardFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "portForward", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getGeoFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "geo", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getAutoRegionFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "autoRegion", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getOfflineFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "offline", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getServersRegionFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "servers", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setIdValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getIdFieldId(env), value);
        }

        static void setNameValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getNameFieldId(env), value);
        }

        static void setCountryValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getCountryFieldId(env), value);
        }

        static void setLatitudeValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getLatitudeFieldId(env), value);
        }

        static void setLongitudeValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getLongitudeFieldId(env), value);
        }

        static void setPortForwardingValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getPortForwardFieldId(env), value);
        }

        static void setGeoValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getGeoFieldId(env), value);
        }

        static void setAutoRegionValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getAutoRegionFieldId(env), value);
        }

        static void setOfflineValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getOfflineFieldId(env), value);
        }

        static void setServersValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getServersRegionFieldId(env), value);
        }
};

class Server {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse$Region$Server");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getIpFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "ip", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getCnFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "cn", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getFqdnFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "fqdn", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getServicesFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "services", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setIpValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getIpFieldId(env), value);
        }

        static void setCnValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getCnFieldId(env), value);
        }

        static void setFqdnValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getFqdnFieldId(env), value);
        }

        static void setServicesValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getServicesFieldId(env), value);
        }
};

class Service {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse$Region$Server$Service");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getServiceFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "service", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getPortsFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "ports", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getNcpFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "ncp", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setServiceValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getServiceFieldId(env), value);
        }

        static void setPortsValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getPortsFieldId(env), value);
        }

        static void setNcpValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getNcpFieldId(env), value);
        }
};

class ListUtils {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("java/util/ArrayList");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "(I)V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getAddMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "add", "(Ljava/lang/Object;)Z");
            assert(methodId != nullptr);
            return methodId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env), 0);
            assert(object != nullptr);
            return object;
        }

        static void addValue(JNIEnv *env, jobject object, jobject value) {
            env->CallBooleanMethod(object, getAddMethodId(env), value);
        }
};

class IteratorUtils {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("java/util/Iterator");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getHasNextMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "hasNext", "()Z");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getNextMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "next", "()Ljava/lang/Object;");
            assert(methodId != nullptr);
            return methodId;
        }

    public:
        static jboolean getHasNextValue(JNIEnv *env, jobject object) {
            jboolean result = env->CallBooleanMethod(object, getHasNextMethodId(env));
            return result;
        }

        static jobject getNextValue(JNIEnv *env, jobject object) {
            jobject result = env->CallObjectMethod(object, getNextMethodId(env));
            assert(result != nullptr);
            return result;
        }
};

class SetUtils {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("java/util/HashSet");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getAddMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "add", "(Ljava/lang/Object;)Z");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getIteratorMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "iterator", "()Ljava/util/Iterator;");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getSizeMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "size", "()I");
            assert(methodId != nullptr);
            return methodId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static jobject getIterator(JNIEnv *env, jobject object) {
            jobject result = env->CallObjectMethod(object, getIteratorMethodId(env));
            assert(result != nullptr);
            return result;
        }

        static void addValue(JNIEnv *env, jobject object, jobject value) {
            env->CallBooleanMethod(object, getAddMethodId(env), value);
        }
};

class MapUtils {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("java/util/HashMap");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "(I)V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getPutMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getGetMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
            assert(methodId != nullptr);
            return methodId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env), 0);
            assert(object != nullptr);
            return object;
        }

        static void putObject(JNIEnv *env, jobject object, jobject key, jobject value) {
            env->CallObjectMethod(object, getPutMethodId(env), key, value);
        }
};

class IntegerUtils {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("java/lang/Integer");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "(I)V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jmethodID getIntValueMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "intValue", "()I");
            assert(methodId != nullptr);
            return methodId;
        }

    public:
        static jobject getObject(JNIEnv *env, jint value) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env), value);
            assert(object != nullptr);
            return object;
        }
};

class DoubleUtils {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("java/lang/Double");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getToStringMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetStaticMethodID(getClass(env), "toString","(D)Ljava/lang/String;");
            assert(methodId != nullptr);
            return methodId;
        }

    public:
        static jstring toString(JNIEnv *env, jdouble value) {
            jstring result = (jstring)env->CallStaticObjectMethod(getClass(env), getToStringMethodId(env), value);
            assert(result != nullptr);
            return result;
        }
};

void advance(KACArraySlice &slice)
{
    slice.data = reinterpret_cast<const void*>((char *)(slice.data) + slice.stride);
    --slice.size;
}

#ifdef __cplusplus
}
#endif
