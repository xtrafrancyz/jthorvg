#include <jni.h>
#include <stdint.h>

#include "thorvg_capi.h"

static jlong to_jlong(const void* ptr)
{
    return (jlong) (intptr_t) ptr;
}

static void* to_ptr(const jlong value)
{
    return (void*) (intptr_t) value;
}

static void throw_illegal_state(JNIEnv* env, const char* message)
{
    jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalStateException");
    if (exceptionClass != NULL) {
        (*env)->ThrowNew(env, exceptionClass, message);
    }
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_engineInit(JNIEnv* env, jclass clazz, jint threads)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_engine_init((unsigned) threads);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_engineTerm(JNIEnv* env, jclass clazz)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_engine_term();
}

JNIEXPORT jobject JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_engineVersion(JNIEnv* env, jclass clazz)
{
    (void) clazz;

    uint32_t major = 0;
    uint32_t minor = 0;
    uint32_t micro = 0;
    const char* version = NULL;
    Tvg_Result result = tvg_engine_version(&major, &minor, &micro, &version);

    if (result != TVG_RESULT_SUCCESS) {
        throw_illegal_state(env, "tvg_engine_version failed");
        return NULL;
    }

    jclass versionClass = (*env)->FindClass(env, "io/github/xtrafrancyz/jthorvg/EngineVersion");
    if (versionClass == NULL) return NULL;

    jmethodID constructor = (*env)->GetMethodID(env, versionClass, "<init>", "(IIILjava/lang/String;)V");
    if (constructor == NULL) return NULL;

    jstring versionString = (*env)->NewStringUTF(env, version != NULL ? version : "");
    if (versionString == NULL) return NULL;

    return (*env)->NewObject(env, versionClass, constructor, (jint) major, (jint) minor, (jint) micro, versionString);
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_swCanvasNew(JNIEnv* env, jclass clazz, jint engineOptionMask)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_swcanvas_create((Tvg_Engine_Option) engineOptionMask));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_swCanvasSetTarget(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jobject buffer,
    jint stride,
    jint width,
    jint height,
    jint colorspace)
{
    (void) clazz;

    uint32_t* pixels = (uint32_t*) (*env)->GetDirectBufferAddress(env, buffer);
    jlong capacity = (*env)->GetDirectBufferCapacity(env, buffer);
    if (pixels == NULL || capacity < ((jlong) stride * (jlong) height)) {
        return (jint) TVG_RESULT_INVALID_ARGUMENT;
    }

    return (jint) tvg_swcanvas_set_target(
        (Tvg_Canvas) to_ptr(canvasHandle),
        pixels,
        (uint32_t) stride,
        (uint32_t) width,
        (uint32_t) height,
        (Tvg_Colorspace) colorspace
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasAdd(JNIEnv* env, jclass clazz, jlong canvasHandle, jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_add((Tvg_Canvas) to_ptr(canvasHandle), (Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasUpdate(JNIEnv* env, jclass clazz, jlong canvasHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_update((Tvg_Canvas) to_ptr(canvasHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasDraw(JNIEnv* env, jclass clazz, jlong canvasHandle, jboolean clear)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_draw((Tvg_Canvas) to_ptr(canvasHandle), clear == JNI_TRUE);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasSync(JNIEnv* env, jclass clazz, jlong canvasHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_sync((Tvg_Canvas) to_ptr(canvasHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasDestroy(JNIEnv* env, jclass clazz, jlong canvasHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_destroy((Tvg_Canvas) to_ptr(canvasHandle));
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeNew(JNIEnv* env, jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_shape_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeAppendRect(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat x,
    jfloat y,
    jfloat width,
    jfloat height,
    jfloat rx,
    jfloat ry,
    jboolean clockwise)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_append_rect(
        (Tvg_Paint) to_ptr(paintHandle),
        x,
        y,
        width,
        height,
        rx,
        ry,
        clockwise == JNI_TRUE
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetFillColor(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint red,
    jint green,
    jint blue,
    jint alpha)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_fill_color(
        (Tvg_Paint) to_ptr(paintHandle),
        (uint8_t) red,
        (uint8_t) green,
        (uint8_t) blue,
        (uint8_t) alpha
    );
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureNew(JNIEnv* env, jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_picture_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureLoad(JNIEnv* env, jclass clazz, jlong paintHandle, jstring path)
{
    (void) clazz;

    const char* nativePath = (*env)->GetStringUTFChars(env, path, NULL);
    if (nativePath == NULL) {
        return (jint) TVG_RESULT_FAILED_ALLOCATION;
    }

    Tvg_Result result = tvg_picture_load((Tvg_Paint) to_ptr(paintHandle), nativePath);
    (*env)->ReleaseStringUTFChars(env, path, nativePath);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureSetSize(JNIEnv* env, jclass clazz, jlong paintHandle, jfloat width, jfloat height)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_picture_set_size((Tvg_Paint) to_ptr(paintHandle), width, height);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintTranslate(JNIEnv* env, jclass clazz, jlong paintHandle, jfloat x, jfloat y)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_translate((Tvg_Paint) to_ptr(paintHandle), x, y);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintScale(JNIEnv* env, jclass clazz, jlong paintHandle, jfloat factor)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_scale((Tvg_Paint) to_ptr(paintHandle), factor);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintRel(JNIEnv* env, jclass clazz, jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_rel((Tvg_Paint) to_ptr(paintHandle));
}
