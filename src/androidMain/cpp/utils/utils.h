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

        static jfieldID getGroupsFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "groups", "Ljava/util/Map;");
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

        static void setGroupsValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getGroupsFieldId(env), value);
        }
};

class ProtocolDetails {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse$ProtocolDetails");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getPortsFieldId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env) , "ports", "Ljava/util/List;");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setPortsValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getPortsFieldId(env), value);
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

        static jfieldID getIdMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "id", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getNameMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "name", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getCountryMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "country", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getLatitudeMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "latitude", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getLongitudeMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "longitude", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getPortForwardMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "portForward", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getGeoMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "geo", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getAutoRegionMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "autoRegion", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getOfflineMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "offline", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getServersRegionMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "servers", "Ljava/util/Map;");
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
            env->SetObjectField(object, getIdMethodId(env), value);
        }

        static void setNameValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getNameMethodId(env), value);
        }

        static void setCountryValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getCountryMethodId(env), value);
        }

        static void setLatitudeValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getLatitudeMethodId(env), value);
        }

        static void setLongitudeValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getLongitudeMethodId(env), value);
        }

        static void setPortForwardingValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getPortForwardMethodId(env), value);
        }

        static void setGeoValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getGeoMethodId(env), value);
        }

        static void setAutoRegionValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getAutoRegionMethodId(env), value);
        }

        static void setOfflineValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getOfflineMethodId(env), value);
        }

        static void setServersValue(JNIEnv *env, jobject object, jobject value) {
            env->SetObjectField(object, getServersRegionMethodId(env), value);
        }
};

class ServerDetails {
    private:
        static jclass getClass(JNIEnv *env) {
            jclass jclazz = env->FindClass("com/privateinternetaccess/regions/model/RegionsResponse$Region$ServerDetails");
            assert(jclazz != nullptr);
            return jclazz;
        }

        static jmethodID getInitMethodId(JNIEnv *env) {
            jmethodID methodId = env->GetMethodID(getClass(env), "<init>", "()V");
            assert(methodId != nullptr);
            return methodId;
        }

        static jfieldID getIpMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "ip", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getCnMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "cn", "Ljava/lang/String;");
            assert(fieldId != nullptr);
            return fieldId;
        }

        static jfieldID getVanMethodId(JNIEnv *env) {
            jfieldID fieldId = env->GetFieldID(getClass(env), "usesVanillaOVPN", "Z");
            assert(fieldId != nullptr);
            return fieldId;
        }

    public:
        static jobject getObject(JNIEnv *env) {
            jobject object = env->NewObject(getClass(env), getInitMethodId(env));
            assert(object != nullptr);
            return object;
        }

        static void setIpValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getIpMethodId(env), value);
        }

        static void setCnValue(JNIEnv *env, jobject object, jstring value) {
            env->SetObjectField(object, getCnMethodId(env), value);
        }

        static void setVanValue(JNIEnv *env, jobject object, jboolean value) {
            env->SetBooleanField(object, getVanMethodId(env), value);
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
            assert(result != nullptr);
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

private void advance(KACArraySlice &slice)
{
    slice.data = reinterpret_cast<const void*>((char *)(slice.data) + slice.stride);
    --slice.size;
}

#ifdef __cplusplus
}
#endif
