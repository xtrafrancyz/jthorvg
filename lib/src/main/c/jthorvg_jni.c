#include <jni.h>
#include <stdint.h>
#include <stdlib.h>

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

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_glCanvasCreate(JNIEnv* env, jclass clazz, jint engineOptionMask)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_glcanvas_create((Tvg_Engine_Option) engineOptionMask));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_glCanvasSetTarget(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jlong display,
    jlong surface,
    jlong context,
    jint id,
    jint width,
    jint height,
    jint colorspace)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_glcanvas_set_target(
        (Tvg_Canvas) to_ptr(canvasHandle),
        to_ptr(display),
        to_ptr(surface),
        to_ptr(context),
        (int32_t) id,
        (uint32_t) width,
        (uint32_t) height,
        (Tvg_Colorspace) colorspace
    );
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_wgCanvasCreate(JNIEnv* env, jclass clazz, jint engineOptionMask)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_wgcanvas_create((Tvg_Engine_Option) engineOptionMask));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_wgCanvasSetTarget(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jlong device,
    jlong instance,
    jlong target,
    jint width,
    jint height,
    jint colorspace,
    jint type)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_wgcanvas_set_target(
        (Tvg_Canvas) to_ptr(canvasHandle),
        to_ptr(device),
        to_ptr(instance),
        to_ptr(target),
        (uint32_t) width,
        (uint32_t) height,
        (Tvg_Colorspace) colorspace,
        (int) type
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_wgCanvasSetTargetWithContext(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jlong instance,
    jlong adapter,
    jlong device,
    jlong target,
    jint width,
    jint height,
    jint colorspace,
    jint type)
{
    (void) env;
    (void) clazz;
    Tvg_WgContext context;
    context.instance = to_ptr(instance);
    context.adapter = to_ptr(adapter);
    context.device = to_ptr(device);
    return (jint) tvg_wgcanvas_set_target_with_context(
        (Tvg_Canvas) to_ptr(canvasHandle),
        &context,
        to_ptr(target),
        (uint32_t) width,
        (uint32_t) height,
        (Tvg_Colorspace) colorspace,
        (int) type
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasInsert(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jlong targetHandle,
    jlong atHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_insert(
        (Tvg_Canvas) to_ptr(canvasHandle),
        (Tvg_Paint) to_ptr(targetHandle),
        (Tvg_Paint) to_ptr(atHandle)
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasRemove(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_remove(
        (Tvg_Canvas) to_ptr(canvasHandle),
        (Tvg_Paint) to_ptr(paintHandle)
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_canvasSetViewport(
    JNIEnv* env,
    jclass clazz,
    jlong canvasHandle,
    jint x,
    jint y,
    jint w,
    jint h)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_canvas_set_viewport(
        (Tvg_Canvas) to_ptr(canvasHandle),
        (int32_t) x,
        (int32_t) y,
        (int32_t) w,
        (int32_t) h
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintRef(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_ref((Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintUnref(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jboolean free)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_unref((Tvg_Paint) to_ptr(paintHandle), free == JNI_TRUE);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetRef(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_get_ref((Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetVisible(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jboolean visible)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_set_visible((Tvg_Paint) to_ptr(paintHandle), visible == JNI_TRUE);
}

JNIEXPORT jboolean JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetVisible(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return tvg_paint_get_visible((Tvg_Paint) to_ptr(paintHandle)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetId(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_get_id((Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetId(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint id)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_set_id((Tvg_Paint) to_ptr(paintHandle), (uint32_t) id);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintRotate(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat degree)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_rotate((Tvg_Paint) to_ptr(paintHandle), degree);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetTransform(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray matrix)
{
    (void) clazz;
    if (matrix == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    jfloat elems[9];
    (*env)->GetFloatArrayRegion(env, matrix, 0, 9, elems);
    Tvg_Matrix m = {
        elems[0], elems[1], elems[2],
        elems[3], elems[4], elems[5],
        elems[6], elems[7], elems[8]
    };
    return (jint) tvg_paint_set_transform((Tvg_Paint) to_ptr(paintHandle), &m);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetTransform(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray matrixOut)
{
    (void) clazz;
    if (matrixOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Matrix m;
    Tvg_Result result = tvg_paint_get_transform((Tvg_Paint) to_ptr(paintHandle), &m);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[9] = {
            m.e11, m.e12, m.e13,
            m.e21, m.e22, m.e23,
            m.e31, m.e32, m.e33
        };
        (*env)->SetFloatArrayRegion(env, matrixOut, 0, 9, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetOpacity(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint opacity)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_set_opacity((Tvg_Paint) to_ptr(paintHandle), (uint8_t) opacity);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetOpacity(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray opacityOut)
{
    (void) clazz;
    uint8_t opacity = 0;
    Tvg_Result result = tvg_paint_get_opacity((Tvg_Paint) to_ptr(paintHandle), &opacity);
    if (result == TVG_RESULT_SUCCESS && opacityOut != NULL) {
        jint val = opacity;
        (*env)->SetIntArrayRegion(env, opacityOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintDuplicate(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_paint_duplicate((Tvg_Paint) to_ptr(paintHandle)));
}

JNIEXPORT jboolean JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintIntersects(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint x,
    jint y,
    jint w,
    jint h)
{
    (void) env;
    (void) clazz;
    return tvg_paint_intersects((Tvg_Paint) to_ptr(paintHandle), (int32_t) x, (int32_t) y, (int32_t) w, (int32_t) h) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetAabb(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray ltrbOut)
{
    (void) clazz;
    if (ltrbOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float x = 0, y = 0, w = 0, h = 0;
    Tvg_Result result = tvg_paint_get_aabb((Tvg_Paint) to_ptr(paintHandle), &x, &y, &w, &h);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[4] = { x, y, w, h };
        (*env)->SetFloatArrayRegion(env, ltrbOut, 0, 4, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetObb(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray pointsOut)
{
    (void) clazz;
    if (pointsOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Point pts[4];
    Tvg_Result result = tvg_paint_get_obb((Tvg_Paint) to_ptr(paintHandle), pts);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[8] = {
            pts[0].x, pts[0].y,
            pts[1].x, pts[1].y,
            pts[2].x, pts[2].y,
            pts[3].x, pts[3].y
        };
        (*env)->SetFloatArrayRegion(env, pointsOut, 0, 8, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetMaskMethod(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlong targetHandle,
    jint method)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_set_mask_method(
        (Tvg_Paint) to_ptr(paintHandle),
        (Tvg_Paint) to_ptr(targetHandle),
        (Tvg_Mask_Method) method
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetMaskMethod(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlongArray targetOut,
    jintArray methodOut)
{
    (void) clazz;
    Tvg_Paint target = NULL;
    Tvg_Mask_Method method = TVG_MASK_METHOD_NONE;
    Tvg_Result result = tvg_paint_get_mask_method((Tvg_Paint) to_ptr(paintHandle), (const Tvg_Paint) (void*) &target, &method);
    if (result == TVG_RESULT_SUCCESS) {
        if (targetOut != NULL) {
            jlong tgtVal = to_jlong(target);
            (*env)->SetLongArrayRegion(env, targetOut, 0, 1, &tgtVal);
        }
        if (methodOut != NULL) {
            jint mthVal = (jint) method;
            (*env)->SetIntArrayRegion(env, methodOut, 0, 1, &mthVal);
        }
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetClip(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlong clipperHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_set_clip(
        (Tvg_Paint) to_ptr(paintHandle),
        (Tvg_Paint) to_ptr(clipperHandle)
    );
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetClip(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_paint_get_clip((Tvg_Paint) to_ptr(paintHandle)));
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetParent(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_paint_get_parent((Tvg_Paint) to_ptr(paintHandle)));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintGetType(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray typeOut)
{
    (void) clazz;
    if (typeOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Type type;
    Tvg_Result result = tvg_paint_get_type((Tvg_Paint) to_ptr(paintHandle), &type);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) type;
        (*env)->SetIntArrayRegion(env, typeOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_paintSetBlendMethod(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint method)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_paint_set_blend_method((Tvg_Paint) to_ptr(paintHandle), (Tvg_Blend_Method) method);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeReset(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_reset((Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeMoveTo(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat x,
    jfloat y)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_move_to((Tvg_Paint) to_ptr(paintHandle), x, y);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeLineTo(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat x,
    jfloat y)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_line_to((Tvg_Paint) to_ptr(paintHandle), x, y);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeCubicTo(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat cx1,
    jfloat cy1,
    jfloat cx2,
    jfloat cy2,
    jfloat x,
    jfloat y)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_cubic_to((Tvg_Paint) to_ptr(paintHandle), cx1, cy1, cx2, cy2, x, y);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeClose(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_close((Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeAppendCircle(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat cx,
    jfloat cy,
    jfloat rx,
    jfloat ry,
    jboolean cw)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_append_circle(
        (Tvg_Paint) to_ptr(paintHandle),
        cx,
        cy,
        rx,
        ry,
        cw == JNI_TRUE
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeAppendPath(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jbyteArray cmds,
    jint cmdCnt,
    jfloatArray pts,
    jint ptsCnt)
{
    (void) clazz;
    if (cmds == NULL || pts == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    jbyte* nativeCmds = (*env)->GetByteArrayElements(env, cmds, NULL);
    jfloat* nativePts = (*env)->GetFloatArrayElements(env, pts, NULL);

    Tvg_Result result = tvg_shape_append_path(
        (Tvg_Paint) to_ptr(paintHandle),
        (const Tvg_Path_Command*) nativeCmds,
        (uint32_t) cmdCnt,
        (const Tvg_Point*) nativePts,
        (uint32_t) ptsCnt
    );

    (*env)->ReleaseByteArrayElements(env, cmds, nativeCmds, JNI_ABORT);
    (*env)->ReleaseFloatArrayElements(env, pts, nativePts, JNI_ABORT);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetPath(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jobjectArray outCmds,
    jobjectArray outPts)
{
    (void) clazz;
    const Tvg_Path_Command* cmds = NULL;
    uint32_t cmdsCnt = 0;
    const Tvg_Point* pts = NULL;
    uint32_t ptsCnt = 0;

    Tvg_Result result = tvg_shape_get_path(
        (Tvg_Paint) to_ptr(paintHandle),
        &cmds,
        &cmdsCnt,
        &pts,
        &ptsCnt
    );

    if (result == TVG_RESULT_SUCCESS) {
        if (outCmds != NULL && cmds != NULL && cmdsCnt > 0) {
            jbyteArray cmdsArr = (*env)->NewByteArray(env, (jsize) cmdsCnt);
            if (cmdsArr != NULL) {
                (*env)->SetByteArrayRegion(env, cmdsArr, 0, (jsize) cmdsCnt, (const jbyte*) cmds);
                (*env)->SetObjectArrayElement(env, outCmds, 0, cmdsArr);
            }
        }
        if (outPts != NULL && pts != NULL && ptsCnt > 0) {
            jfloatArray ptsArr = (*env)->NewFloatArray(env, (jsize) (ptsCnt * 2));
            if (ptsArr != NULL) {
                (*env)->SetFloatArrayRegion(env, ptsArr, 0, (jsize) (ptsCnt * 2), (const jfloat*) pts);
                (*env)->SetObjectArrayElement(env, outPts, 0, ptsArr);
            }
        }
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeWidth(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat width)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_stroke_width((Tvg_Paint) to_ptr(paintHandle), width);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeWidth(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray widthOut)
{
    (void) clazz;
    if (widthOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float w = 0.0f;
    Tvg_Result result = tvg_shape_get_stroke_width((Tvg_Paint) to_ptr(paintHandle), &w);
    if (result == TVG_RESULT_SUCCESS) {
        (*env)->SetFloatArrayRegion(env, widthOut, 0, 1, &w);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeColor(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint r,
    jint g,
    jint b,
    jint a)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_stroke_color(
        (Tvg_Paint) to_ptr(paintHandle),
        (uint8_t) r,
        (uint8_t) g,
        (uint8_t) b,
        (uint8_t) a
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeColor(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray rgbaOut)
{
    (void) clazz;
    if (rgbaOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    uint8_t r = 0, g = 0, b = 0, a = 0;
    Tvg_Result result = tvg_shape_get_stroke_color((Tvg_Paint) to_ptr(paintHandle), &r, &g, &b, &a);
    if (result == TVG_RESULT_SUCCESS) {
        jint elems[4] = { r, g, b, a };
        (*env)->SetIntArrayRegion(env, rgbaOut, 0, 4, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeGradient(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlong gradHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_stroke_gradient(
        (Tvg_Paint) to_ptr(paintHandle),
        (Tvg_Gradient) to_ptr(gradHandle)
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeGradient(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlongArray gradHandleOut)
{
    (void) clazz;
    if (gradHandleOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Gradient grad = NULL;
    Tvg_Result result = tvg_shape_get_stroke_gradient((Tvg_Paint) to_ptr(paintHandle), &grad);
    if (result == TVG_RESULT_SUCCESS) {
        jlong val = to_jlong(grad);
        (*env)->SetLongArrayRegion(env, gradHandleOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeDash(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray dashPattern,
    jint cnt,
    jfloat offset)
{
    (void) clazz;
    jfloat* nativeDash = NULL;
    if (dashPattern != NULL && cnt > 0) {
        nativeDash = (*env)->GetFloatArrayElements(env, dashPattern, NULL);
    }
    Tvg_Result result = tvg_shape_set_stroke_dash(
        (Tvg_Paint) to_ptr(paintHandle),
        (const float*) nativeDash,
        (uint32_t) cnt,
        offset
    );
    if (nativeDash != NULL) {
        (*env)->ReleaseFloatArrayElements(env, dashPattern, nativeDash, JNI_ABORT);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeDash(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jobjectArray outDashPattern,
    jfloatArray outOffset)
{
    (void) clazz;
    const float* dashPattern = NULL;
    uint32_t cnt = 0;
    float offset = 0.0f;

    Tvg_Result result = tvg_shape_get_stroke_dash(
        (Tvg_Paint) to_ptr(paintHandle),
        &dashPattern,
        &cnt,
        &offset
    );

    if (result == TVG_RESULT_SUCCESS) {
        if (outDashPattern != NULL && dashPattern != NULL && cnt > 0) {
            jfloatArray dashArr = (*env)->NewFloatArray(env, (jsize) cnt);
            if (dashArr != NULL) {
                (*env)->SetFloatArrayRegion(env, dashArr, 0, (jsize) cnt, (const jfloat*) dashPattern);
                (*env)->SetObjectArrayElement(env, outDashPattern, 0, dashArr);
            }
        }
        if (outOffset != NULL) {
            (*env)->SetFloatArrayRegion(env, outOffset, 0, 1, &offset);
        }
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeCap(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint cap)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_stroke_cap((Tvg_Paint) to_ptr(paintHandle), (Tvg_Stroke_Cap) cap);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeCap(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray capOut)
{
    (void) clazz;
    if (capOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Stroke_Cap cap;
    Tvg_Result result = tvg_shape_get_stroke_cap((Tvg_Paint) to_ptr(paintHandle), &cap);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) cap;
        (*env)->SetIntArrayRegion(env, capOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeJoin(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint join)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_stroke_join((Tvg_Paint) to_ptr(paintHandle), (Tvg_Stroke_Join) join);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeJoin(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray joinOut)
{
    (void) clazz;
    if (joinOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Stroke_Join join;
    Tvg_Result result = tvg_shape_get_stroke_join((Tvg_Paint) to_ptr(paintHandle), &join);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) join;
        (*env)->SetIntArrayRegion(env, joinOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetStrokeMiterlimit(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat miterlimit)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_stroke_miterlimit((Tvg_Paint) to_ptr(paintHandle), miterlimit);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetStrokeMiterlimit(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray miterlimitOut)
{
    (void) clazz;
    if (miterlimitOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float miterlimit = 0.0f;
    Tvg_Result result = tvg_shape_get_stroke_miterlimit((Tvg_Paint) to_ptr(paintHandle), &miterlimit);
    if (result == TVG_RESULT_SUCCESS) {
        (*env)->SetFloatArrayRegion(env, miterlimitOut, 0, 1, &miterlimit);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetTrimpath(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat begin,
    jfloat end,
    jboolean simultaneous)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_trimpath(
        (Tvg_Paint) to_ptr(paintHandle),
        begin,
        end,
        simultaneous == JNI_TRUE
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetFillColor(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray rgbaOut)
{
    (void) clazz;
    if (rgbaOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    uint8_t r = 0, g = 0, b = 0, a = 0;
    Tvg_Result result = tvg_shape_get_fill_color((Tvg_Paint) to_ptr(paintHandle), &r, &g, &b, &a);
    if (result == TVG_RESULT_SUCCESS) {
        jint elems[4] = { r, g, b, a };
        (*env)->SetIntArrayRegion(env, rgbaOut, 0, 4, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetFillRule(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint rule)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_fill_rule((Tvg_Paint) to_ptr(paintHandle), (Tvg_Fill_Rule) rule);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetFillRule(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray ruleOut)
{
    (void) clazz;
    if (ruleOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Fill_Rule rule;
    Tvg_Result result = tvg_shape_get_fill_rule((Tvg_Paint) to_ptr(paintHandle), &rule);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) rule;
        (*env)->SetIntArrayRegion(env, ruleOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetPaintOrder(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jboolean strokeFirst)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_paint_order((Tvg_Paint) to_ptr(paintHandle), strokeFirst == JNI_TRUE);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeSetGradient(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlong gradHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_shape_set_gradient(
        (Tvg_Paint) to_ptr(paintHandle),
        (Tvg_Gradient) to_ptr(gradHandle)
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_shapeGetGradient(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlongArray gradHandleOut)
{
    (void) clazz;
    if (gradHandleOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Gradient grad = NULL;
    Tvg_Result result = tvg_shape_get_gradient((Tvg_Paint) to_ptr(paintHandle), &grad);
    if (result == TVG_RESULT_SUCCESS) {
        jlong val = to_jlong(grad);
        (*env)->SetLongArrayRegion(env, gradHandleOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_linearGradientNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_linear_gradient_new());
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_radialGradientNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_radial_gradient_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_linearGradientSet(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloat x1,
    jfloat y1,
    jfloat x2,
    jfloat y2)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_linear_gradient_set((Tvg_Gradient) to_ptr(gradHandle), x1, y1, x2, y2);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_linearGradientGet(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloatArray x1y1x2y2Out)
{
    (void) clazz;
    if (x1y1x2y2Out == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    Tvg_Result result = tvg_linear_gradient_get((Tvg_Gradient) to_ptr(gradHandle), &x1, &y1, &x2, &y2);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[4] = { x1, y1, x2, y2 };
        (*env)->SetFloatArrayRegion(env, x1y1x2y2Out, 0, 4, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_radialGradientSet(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloat cx,
    jfloat cy,
    jfloat r,
    jfloat fx,
    jfloat fy,
    jfloat fr)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_radial_gradient_set((Tvg_Gradient) to_ptr(gradHandle), cx, cy, r, fx, fy, fr);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_radialGradientGet(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloatArray cxcyrFxfyfrOut)
{
    (void) clazz;
    if (cxcyrFxfyfrOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float cx = 0, cy = 0, r = 0, fx = 0, fy = 0, fr = 0;
    Tvg_Result result = tvg_radial_gradient_get((Tvg_Gradient) to_ptr(gradHandle), &cx, &cy, &r, &fx, &fy, &fr);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[6] = { cx, cy, r, fx, fy, fr };
        (*env)->SetFloatArrayRegion(env, cxcyrFxfyfrOut, 0, 6, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientSetColorStops(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloatArray offsets,
    jintArray colors,
    jint cnt)
{
    (void) clazz;
    if (offsets == NULL || colors == NULL || cnt <= 0) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    jfloat* nativeOffsets = (*env)->GetFloatArrayElements(env, offsets, NULL);
    jint* nativeColors = (*env)->GetIntArrayElements(env, colors, NULL);

    Tvg_Color_Stop* stops = (Tvg_Color_Stop*) malloc(cnt * sizeof(Tvg_Color_Stop));
    if (stops == NULL) {
        (*env)->ReleaseFloatArrayElements(env, offsets, nativeOffsets, JNI_ABORT);
        (*env)->ReleaseIntArrayElements(env, colors, nativeColors, JNI_ABORT);
        return (jint) TVG_RESULT_FAILED_ALLOCATION;
    }

    for (int i = 0; i < cnt; i++) {
        stops[i].offset = nativeOffsets[i];
        uint32_t color = (uint32_t) nativeColors[i];
        stops[i].r = (uint8_t) ((color >> 24) & 0xFF);
        stops[i].g = (uint8_t) ((color >> 16) & 0xFF);
        stops[i].b = (uint8_t) ((color >> 8) & 0xFF);
        stops[i].a = (uint8_t) (color & 0xFF);
    }

    Tvg_Result result = tvg_gradient_set_color_stops((Tvg_Gradient) to_ptr(gradHandle), stops, (uint32_t) cnt);

    free(stops);
    (*env)->ReleaseFloatArrayElements(env, offsets, nativeOffsets, JNI_ABORT);
    (*env)->ReleaseIntArrayElements(env, colors, nativeColors, JNI_ABORT);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientGetColorStops(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jobjectArray outOffsets,
    jobjectArray outColors)
{
    (void) clazz;
    const Tvg_Color_Stop* color_stops = NULL;
    uint32_t cnt = 0;

    Tvg_Result result = tvg_gradient_get_color_stops((Tvg_Gradient) to_ptr(gradHandle), &color_stops, &cnt);
    if (result == TVG_RESULT_SUCCESS) {
        if (outOffsets != NULL && color_stops != NULL && cnt > 0) {
            jfloatArray offsetsArr = (*env)->NewFloatArray(env, (jsize) cnt);
            if (offsetsArr != NULL) {
                jfloat* tempOffsets = (jfloat*) malloc(cnt * sizeof(jfloat));
                for (uint32_t i = 0; i < cnt; i++) {
                    tempOffsets[i] = color_stops[i].offset;
                }
                (*env)->SetFloatArrayRegion(env, offsetsArr, 0, (jsize) cnt, tempOffsets);
                free(tempOffsets);
                (*env)->SetObjectArrayElement(env, outOffsets, 0, offsetsArr);
            }
        }
        if (outColors != NULL && color_stops != NULL && cnt > 0) {
            jintArray colorsArr = (*env)->NewIntArray(env, (jsize) cnt);
            if (colorsArr != NULL) {
                jint* tempColors = (jint*) malloc(cnt * sizeof(jint));
                for (uint32_t i = 0; i < cnt; i++) {
                    tempColors[i] = ((jint) color_stops[i].r << 24) |
                                    ((jint) color_stops[i].g << 16) |
                                    ((jint) color_stops[i].b << 8) |
                                    (jint) color_stops[i].a;
                }
                (*env)->SetIntArrayRegion(env, colorsArr, 0, (jsize) cnt, tempColors);
                free(tempColors);
                (*env)->SetObjectArrayElement(env, outColors, 0, colorsArr);
            }
        }
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientSetSpread(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jint spread)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_gradient_set_spread((Tvg_Gradient) to_ptr(gradHandle), (const Tvg_Stroke_Fill) spread);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientGetSpread(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jintArray spreadOut)
{
    (void) clazz;
    if (spreadOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Stroke_Fill spread;
    Tvg_Result result = tvg_gradient_get_spread((Tvg_Gradient) to_ptr(gradHandle), &spread);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) spread;
        (*env)->SetIntArrayRegion(env, spreadOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientSetTransform(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloatArray matrix)
{
    (void) clazz;
    if (matrix == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    jfloat elems[9];
    (*env)->GetFloatArrayRegion(env, matrix, 0, 9, elems);
    Tvg_Matrix m = {
        elems[0], elems[1], elems[2],
        elems[3], elems[4], elems[5],
        elems[6], elems[7], elems[8]
    };
    return (jint) tvg_gradient_set_transform((Tvg_Gradient) to_ptr(gradHandle), &m);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientGetTransform(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jfloatArray matrixOut)
{
    (void) clazz;
    if (matrixOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Matrix m;
    Tvg_Result result = tvg_gradient_get_transform((const Tvg_Gradient) to_ptr(gradHandle), &m);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[9] = {
            m.e11, m.e12, m.e13,
            m.e21, m.e22, m.e23,
            m.e31, m.e32, m.e33
        };
        (*env)->SetFloatArrayRegion(env, matrixOut, 0, 9, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientGetType(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle,
    jintArray typeOut)
{
    (void) clazz;
    if (typeOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Type type;
    Tvg_Result result = tvg_gradient_get_type((const Tvg_Gradient) to_ptr(gradHandle), &type);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) type;
        (*env)->SetIntArrayRegion(env, typeOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientDuplicate(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_gradient_duplicate((Tvg_Gradient) to_ptr(gradHandle)));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_gradientDel(
    JNIEnv* env,
    jclass clazz,
    jlong gradHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_gradient_del((Tvg_Gradient) to_ptr(gradHandle));
}

static JavaVM* g_vm = NULL;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    (void) reserved;
    g_vm = vm;
    return JNI_VERSION_1_6;
}

static bool native_asset_resolver_wrapper(Tvg_Paint paint, const char* src, void* data) {
    if (g_vm == NULL || data == NULL) return false;
    JNIEnv* env = NULL;
    jint get_env_res = (*g_vm)->GetEnv(g_vm, (void**)&env, JNI_VERSION_1_6);
    bool is_attached = false;
    if (get_env_res == JNI_EDETACHED) {
        if ((*g_vm)->AttachCurrentThread(g_vm, (void**)&env, NULL) != 0) {
            return false;
        }
        is_attached = true;
    } else if (get_env_res != JNI_OK) {
        return false;
    }

    jobject resolver_obj = (jobject) data;
    jclass clazz = (*env)->GetObjectClass(env, resolver_obj);
    jmethodID method = (*env)->GetMethodID(env, clazz, "resolve", "(JLjava/lang/String;)Z");
    if (method == NULL) {
        (*env)->DeleteLocalRef(env, clazz);
        if (is_attached) (*g_vm)->DetachCurrentThread(g_vm);
        return false;
    }

    jstring src_str = (*env)->NewStringUTF(env, src != NULL ? src : "");
    jboolean res = (*env)->CallBooleanMethod(env, resolver_obj, method, to_jlong(paint), src_str);

    (*env)->DeleteLocalRef(env, src_str);
    (*env)->DeleteLocalRef(env, clazz);
    if (is_attached) (*g_vm)->DetachCurrentThread(g_vm);
    return res == JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureLoadRaw(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jintArray data,
    jint w,
    jint h,
    jint colorspace,
    jboolean copy)
{
    (void) clazz;
    if (data == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    jint* nativeData = (*env)->GetIntArrayElements(env, data, NULL);
    Tvg_Result result = tvg_picture_load_raw(
        (Tvg_Paint) to_ptr(paintHandle),
        (const uint32_t*) nativeData,
        (uint32_t) w,
        (uint32_t) h,
        (Tvg_Colorspace) colorspace,
        copy == JNI_TRUE
    );
    (*env)->ReleaseIntArrayElements(env, data, nativeData, JNI_ABORT);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureLoadRawBuffer(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jobject buffer,
    jint w,
    jint h,
    jint colorspace,
    jboolean copy)
{
    (void) clazz;
    if (buffer == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    uint32_t* nativeData = (uint32_t*) (*env)->GetDirectBufferAddress(env, buffer);
    return (jint) tvg_picture_load_raw(
        (Tvg_Paint) to_ptr(paintHandle),
        nativeData,
        (uint32_t) w,
        (uint32_t) h,
        (Tvg_Colorspace) colorspace,
        copy == JNI_TRUE
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureLoadData(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jbyteArray data,
    jint size,
    jstring mimetype,
    jstring rpath,
    jboolean copy)
{
    (void) clazz;
    if (data == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    jbyte* nativeData = (*env)->GetByteArrayElements(env, data, NULL);
    const char* nativeMime = mimetype != NULL ? (*env)->GetStringUTFChars(env, mimetype, NULL) : NULL;
    const char* nativeRpath = rpath != NULL ? (*env)->GetStringUTFChars(env, rpath, NULL) : NULL;

    Tvg_Result result = tvg_picture_load_data(
        (Tvg_Paint) to_ptr(paintHandle),
        (const char*) nativeData,
        (uint32_t) size,
        nativeMime,
        nativeRpath,
        copy == JNI_TRUE
    );

    if (nativeRpath != NULL) (*env)->ReleaseStringUTFChars(env, rpath, nativeRpath);
    if (nativeMime != NULL) (*env)->ReleaseStringUTFChars(env, mimetype, nativeMime);
    (*env)->ReleaseByteArrayElements(env, data, nativeData, JNI_ABORT);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureSetAssetResolver(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jobject resolver)
{
    (void) clazz;
    void* user_data = NULL;
    if (resolver != NULL) {
        user_data = (void*) (*env)->NewGlobalRef(env, resolver);
    }
    return (jint) tvg_picture_set_asset_resolver(
        (Tvg_Paint) to_ptr(paintHandle),
        resolver != NULL ? native_asset_resolver_wrapper : NULL,
        user_data
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureGetSize(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray sizeOut)
{
    (void) clazz;
    if (sizeOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float w = 0.0f, h = 0.0f;
    Tvg_Result result = tvg_picture_get_size((const Tvg_Paint) to_ptr(paintHandle), &w, &h);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[2] = { w, h };
        (*env)->SetFloatArrayRegion(env, sizeOut, 0, 2, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureSetOrigin(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat x,
    jfloat y)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_picture_set_origin((Tvg_Paint) to_ptr(paintHandle), x, y);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureGetOrigin(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray originOut)
{
    (void) clazz;
    if (originOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float x = 0.0f, y = 0.0f;
    Tvg_Result result = tvg_picture_get_origin((const Tvg_Paint) to_ptr(paintHandle), &x, &y);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[2] = { x, y };
        (*env)->SetFloatArrayRegion(env, originOut, 0, 2, elems);
    }
    return (jint) result;
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureGetPaint(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint id)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_picture_get_paint((Tvg_Paint) to_ptr(paintHandle), (uint32_t) id));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureSetFilter(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint method)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_picture_set_filter((Tvg_Paint) to_ptr(paintHandle), (Tvg_Filter_Method) method);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_pictureSetAccessible(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jboolean accessible)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_picture_set_accessible((Tvg_Paint) to_ptr(paintHandle), accessible == JNI_TRUE);
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_scene_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneAdd(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_add((Tvg_Paint) to_ptr(sceneHandle), (Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneInsert(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jlong targetHandle,
    jlong atHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_insert(
        (Tvg_Paint) to_ptr(sceneHandle),
        (Tvg_Paint) to_ptr(targetHandle),
        (Tvg_Paint) to_ptr(atHandle)
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneRemove(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_remove((Tvg_Paint) to_ptr(sceneHandle), (Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneClearEffects(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_clear_effects((Tvg_Paint) to_ptr(sceneHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneAddEffectGaussianBlur(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jdouble sigma,
    jint direction,
    jint border,
    jint quality)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_add_effect_gaussian_blur(
        (Tvg_Paint) to_ptr(sceneHandle),
        (double) sigma,
        (int) direction,
        (int) border,
        (int) quality
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneAddEffectDropShadow(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jint r,
    jint g,
    jint b,
    jint a,
    jdouble angle,
    jdouble distance,
    jdouble sigma,
    jint quality)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_add_effect_drop_shadow(
        (Tvg_Paint) to_ptr(sceneHandle),
        (int) r,
        (int) g,
        (int) b,
        (int) a,
        (double) angle,
        (double) distance,
        (double) sigma,
        (int) quality
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneAddEffectFill(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jint r,
    jint g,
    jint b,
    jint a)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_add_effect_fill(
        (Tvg_Paint) to_ptr(sceneHandle),
        (int) r,
        (int) g,
        (int) b,
        (int) a
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneAddEffectTint(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jint black_r,
    jint black_g,
    jint black_b,
    jint white_r,
    jint white_g,
    jint white_b,
    jdouble intensity)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_add_effect_tint(
        (Tvg_Paint) to_ptr(sceneHandle),
        (int) black_r,
        (int) black_g,
        (int) black_b,
        (int) white_r,
        (int) white_g,
        (int) white_b,
        (double) intensity
    );
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_sceneAddEffectTritone(
    JNIEnv* env,
    jclass clazz,
    jlong sceneHandle,
    jint shadow_r,
    jint shadow_g,
    jint shadow_b,
    jint midtone_r,
    jint midtone_g,
    jint midtone_b,
    jint highlight_r,
    jint highlight_g,
    jint highlight_b,
    jint blend)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_scene_add_effect_tritone(
        (Tvg_Paint) to_ptr(sceneHandle),
        (int) shadow_r,
        (int) shadow_g,
        (int) shadow_b,
        (int) midtone_r,
        (int) midtone_g,
        (int) midtone_b,
        (int) highlight_r,
        (int) highlight_g,
        (int) highlight_b,
        (int) blend
    );
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_text_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetFont(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jstring name)
{
    (void) clazz;
    const char* nativeName = name != NULL ? (*env)->GetStringUTFChars(env, name, NULL) : NULL;
    Tvg_Result result = tvg_text_set_font((Tvg_Paint) to_ptr(paintHandle), nativeName);
    if (nativeName != NULL) (*env)->ReleaseStringUTFChars(env, name, nativeName);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetSize(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat size)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_set_size((Tvg_Paint) to_ptr(paintHandle), size);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetText(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jstring utf8)
{
    (void) clazz;
    const char* nativeUtf8 = utf8 != NULL ? (*env)->GetStringUTFChars(env, utf8, NULL) : NULL;
    Tvg_Result result = tvg_text_set_text((Tvg_Paint) to_ptr(paintHandle), nativeUtf8);
    if (nativeUtf8 != NULL) (*env)->ReleaseStringUTFChars(env, utf8, nativeUtf8);
    return (jint) result;
}

JNIEXPORT jstring JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textGetText(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) clazz;
    const char* utf8 = tvg_text_get_text((const Tvg_Paint) to_ptr(paintHandle));
    return utf8 != NULL ? (*env)->NewStringUTF(env, utf8) : NULL;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textAlign(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat x,
    jfloat y)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_align((Tvg_Paint) to_ptr(paintHandle), x, y);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textLayout(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat w,
    jfloat h)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_layout((Tvg_Paint) to_ptr(paintHandle), w, h);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textWrapMode(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint mode)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_wrap_mode((Tvg_Paint) to_ptr(paintHandle), (Tvg_Text_Wrap) mode);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textLineCount(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_line_count((Tvg_Paint) to_ptr(paintHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSpacing(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat letter,
    jfloat line)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_spacing((Tvg_Paint) to_ptr(paintHandle), letter, line);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetItalic(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat shear)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_set_italic((Tvg_Paint) to_ptr(paintHandle), shear);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetOutline(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloat width,
    jint r,
    jint g,
    jint b)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_set_outline((Tvg_Paint) to_ptr(paintHandle), width, (uint8_t) r, (uint8_t) g, (uint8_t) b);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetColor(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jint r,
    jint g,
    jint b)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_set_color((Tvg_Paint) to_ptr(paintHandle), (uint8_t) r, (uint8_t) g, (uint8_t) b);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textSetGradient(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jlong gradHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_text_set_gradient((Tvg_Paint) to_ptr(paintHandle), (Tvg_Gradient) to_ptr(gradHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textGetTextMetrics(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jfloatArray metricsOut)
{
    (void) clazz;
    if (metricsOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    Tvg_Text_Metrics m;
    Tvg_Result result = tvg_text_get_text_metrics((const Tvg_Paint) to_ptr(paintHandle), &m);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[4] = { m.ascent, m.descent, m.linegap, m.advance };
        (*env)->SetFloatArrayRegion(env, metricsOut, 0, 4, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_textGetGlyphMetrics(
    JNIEnv* env,
    jclass clazz,
    jlong paintHandle,
    jstring ch,
    jfloatArray metricsOut,
    jintArray nextIndexOut)
{
    (void) clazz;
    if (ch == NULL || metricsOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativeCh = (*env)->GetStringUTFChars(env, ch, NULL);
    const char* next = NULL;
    Tvg_Glyph_Metrics m;
    Tvg_Result result = tvg_text_get_glyph_metrics((const Tvg_Paint) to_ptr(paintHandle), nativeCh, &m, &next);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[6] = { m.advance, m.bearing, m.min.x, m.min.y, m.max.x, m.max.y };
        (*env)->SetFloatArrayRegion(env, metricsOut, 0, 6, elems);
        if (nextIndexOut != NULL && next != NULL) {
            jint offset = (jint) (next - nativeCh);
            (*env)->SetIntArrayRegion(env, nextIndexOut, 0, 1, &offset);
        }
    }
    (*env)->ReleaseStringUTFChars(env, ch, nativeCh);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_fontLoad(
    JNIEnv* env,
    jclass clazz,
    jstring path)
{
    (void) clazz;
    if (path == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativePath = (*env)->GetStringUTFChars(env, path, NULL);
    Tvg_Result result = tvg_font_load(nativePath);
    (*env)->ReleaseStringUTFChars(env, path, nativePath);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_fontLoadData(
    JNIEnv* env,
    jclass clazz,
    jstring name,
    jbyteArray data,
    jint size,
    jstring mimetype,
    jboolean copy)
{
    (void) clazz;
    if (name == NULL || data == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativeName = (*env)->GetStringUTFChars(env, name, NULL);
    jbyte* nativeData = (*env)->GetByteArrayElements(env, data, NULL);
    const char* nativeMime = mimetype != NULL ? (*env)->GetStringUTFChars(env, mimetype, NULL) : NULL;

    Tvg_Result result = tvg_font_load_data(
        nativeName,
        (const char*) nativeData,
        (uint32_t) size,
        nativeMime,
        copy == JNI_TRUE
    );

    if (nativeMime != NULL) (*env)->ReleaseStringUTFChars(env, mimetype, nativeMime);
    (*env)->ReleaseByteArrayElements(env, data, nativeData, JNI_ABORT);
    (*env)->ReleaseStringUTFChars(env, name, nativeName);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_fontUnload(
    JNIEnv* env,
    jclass clazz,
    jstring path)
{
    (void) clazz;
    if (path == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativePath = (*env)->GetStringUTFChars(env, path, NULL);
    Tvg_Result result = tvg_font_unload(nativePath);
    (*env)->ReleaseStringUTFChars(env, path, nativePath);
    return (jint) result;
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_saverNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_saver_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_saverSavePaint(
    JNIEnv* env,
    jclass clazz,
    jlong saverHandle,
    jlong paintHandle,
    jstring path,
    jint quality)
{
    (void) clazz;
    if (path == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativePath = (*env)->GetStringUTFChars(env, path, NULL);
    Tvg_Result result = tvg_saver_save_paint(
        (Tvg_Saver) to_ptr(saverHandle),
        (Tvg_Paint) to_ptr(paintHandle),
        nativePath,
        (uint32_t) quality
    );
    (*env)->ReleaseStringUTFChars(env, path, nativePath);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_saverSaveAnimation(
    JNIEnv* env,
    jclass clazz,
    jlong saverHandle,
    jlong animHandle,
    jstring path,
    jint quality,
    jint fps)
{
    (void) clazz;
    if (path == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativePath = (*env)->GetStringUTFChars(env, path, NULL);
    Tvg_Result result = tvg_saver_save_animation(
        (Tvg_Saver) to_ptr(saverHandle),
        (Tvg_Animation) to_ptr(animHandle),
        nativePath,
        (uint32_t) quality,
        (uint32_t) fps
    );
    (*env)->ReleaseStringUTFChars(env, path, nativePath);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_saverSync(
    JNIEnv* env,
    jclass clazz,
    jlong saverHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_saver_sync((Tvg_Saver) to_ptr(saverHandle));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_saverDel(
    JNIEnv* env,
    jclass clazz,
    jlong saverHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_saver_del((Tvg_Saver) to_ptr(saverHandle));
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_animation_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationSetFrame(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloat no)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_animation_set_frame((Tvg_Animation) to_ptr(animHandle), no);
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationGetPicture(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_animation_get_picture((Tvg_Animation) to_ptr(animHandle)));
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationGetFrame(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloatArray noOut)
{
    (void) clazz;
    if (noOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float no = 0.0f;
    Tvg_Result result = tvg_animation_get_frame((Tvg_Animation) to_ptr(animHandle), &no);
    if (result == TVG_RESULT_SUCCESS) {
        (*env)->SetFloatArrayRegion(env, noOut, 0, 1, &no);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationGetTotalFrame(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloatArray cntOut)
{
    (void) clazz;
    if (cntOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float cnt = 0.0f;
    Tvg_Result result = tvg_animation_get_total_frame((Tvg_Animation) to_ptr(animHandle), &cnt);
    if (result == TVG_RESULT_SUCCESS) {
        (*env)->SetFloatArrayRegion(env, cntOut, 0, 1, &cnt);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationGetDuration(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloatArray durationOut)
{
    (void) clazz;
    if (durationOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float duration = 0.0f;
    Tvg_Result result = tvg_animation_get_duration((Tvg_Animation) to_ptr(animHandle), &duration);
    if (result == TVG_RESULT_SUCCESS) {
        (*env)->SetFloatArrayRegion(env, durationOut, 0, 1, &duration);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationSetSegment(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloat begin,
    jfloat end)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_animation_set_segment((Tvg_Animation) to_ptr(animHandle), begin, end);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationGetSegment(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloatArray beginEndOut)
{
    (void) clazz;
    if (beginEndOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    float begin = 0.0f, end = 0.0f;
    Tvg_Result result = tvg_animation_get_segment((Tvg_Animation) to_ptr(animHandle), &begin, &end);
    if (result == TVG_RESULT_SUCCESS) {
        jfloat elems[2] = { begin, end };
        (*env)->SetFloatArrayRegion(env, beginEndOut, 0, 2, elems);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_animationDel(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_animation_del((Tvg_Animation) to_ptr(animHandle));
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_accessorNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_accessor_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_accessorDel(
    JNIEnv* env,
    jclass clazz,
    jlong accessorHandle)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_accessor_del((Tvg_Accessor) to_ptr(accessorHandle));
}

static bool native_accessor_callback_wrapper(Tvg_Paint paint, void* data) {
    if (g_vm == NULL || data == NULL) return false;
    JNIEnv* env = NULL;
    jint get_env_res = (*g_vm)->GetEnv(g_vm, (void**)&env, JNI_VERSION_1_6);
    bool is_attached = false;
    if (get_env_res == JNI_EDETACHED) {
        if ((*g_vm)->AttachCurrentThread(g_vm, (void**)&env, NULL) != 0) {
            return false;
        }
        is_attached = true;
    } else if (get_env_res != JNI_OK) {
        return false;
    }

    jobject callback_obj = (jobject) data;
    jclass clazz = (*env)->GetObjectClass(env, callback_obj);
    jmethodID method = (*env)->GetMethodID(env, clazz, "onVisit", "(J)Z");
    if (method == NULL) {
        (*env)->DeleteLocalRef(env, clazz);
        if (is_attached) (*g_vm)->DetachCurrentThread(g_vm);
        return false;
    }

    jboolean res = (*env)->CallBooleanMethod(env, callback_obj, method, to_jlong(paint));

    (*env)->DeleteLocalRef(env, clazz);
    if (is_attached) (*g_vm)->DetachCurrentThread(g_vm);
    return res == JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_accessorSet(
    JNIEnv* env,
    jclass clazz,
    jlong accessorHandle,
    jlong paintHandle,
    jobject callback)
{
    (void) clazz;
    void* user_data = NULL;
    if (callback != NULL) {
        user_data = (void*) (*env)->NewGlobalRef(env, callback);
    }
    Tvg_Result result = tvg_accessor_set(
        (Tvg_Accessor) to_ptr(accessorHandle),
        (Tvg_Paint) to_ptr(paintHandle),
        callback != NULL ? native_accessor_callback_wrapper : NULL,
        user_data
    );
    if (user_data != NULL) {
        (*env)->DeleteGlobalRef(env, (jobject) user_data);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_accessorGenerateId(
    JNIEnv* env,
    jclass clazz,
    jstring name)
{
    (void) clazz;
    if (name == NULL) return 0;
    const char* nativeName = (*env)->GetStringUTFChars(env, name, NULL);
    uint32_t id = tvg_accessor_generate_id(nativeName);
    (*env)->ReleaseStringUTFChars(env, name, nativeName);
    return (jint) id;
}

JNIEXPORT jstring JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_accessorGetName(
    JNIEnv* env,
    jclass clazz,
    jlong accessorHandle,
    jint id)
{
    (void) clazz;
    const char* name = tvg_accessor_get_name((Tvg_Accessor) to_ptr(accessorHandle), (uint32_t) id);
    return name != NULL ? (*env)->NewStringUTF(env, name) : NULL;
}

JNIEXPORT jlong JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationNew(
    JNIEnv* env,
    jclass clazz)
{
    (void) env;
    (void) clazz;
    return to_jlong(tvg_lottie_animation_new());
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationGenSlot(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jstring slot)
{
    (void) clazz;
    if (slot == NULL) return 0;
    const char* nativeSlot = (*env)->GetStringUTFChars(env, slot, NULL);
    uint32_t id = tvg_lottie_animation_gen_slot((Tvg_Animation) to_ptr(animHandle), nativeSlot);
    (*env)->ReleaseStringUTFChars(env, slot, nativeSlot);
    return (jint) id;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationApplySlot(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jint id)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_lottie_animation_apply_slot((Tvg_Animation) to_ptr(animHandle), (uint32_t) id);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationDelSlot(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jint id)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_lottie_animation_del_slot((Tvg_Animation) to_ptr(animHandle), (uint32_t) id);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationSetMarker(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jstring marker)
{
    (void) clazz;
    if (marker == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    const char* nativeMarker = (*env)->GetStringUTFChars(env, marker, NULL);
    Tvg_Result result = tvg_lottie_animation_set_marker((Tvg_Animation) to_ptr(animHandle), nativeMarker);
    (*env)->ReleaseStringUTFChars(env, marker, nativeMarker);
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationGetMarkersCnt(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jintArray cntOut)
{
    (void) clazz;
    if (cntOut == NULL) return (jint) TVG_RESULT_INVALID_ARGUMENT;
    uint32_t cnt = 0;
    Tvg_Result result = tvg_lottie_animation_get_markers_cnt((Tvg_Animation) to_ptr(animHandle), &cnt);
    if (result == TVG_RESULT_SUCCESS) {
        jint val = (jint) cnt;
        (*env)->SetIntArrayRegion(env, cntOut, 0, 1, &val);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationGetMarker(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jint idx,
    jobjectArray nameOut)
{
    (void) clazz;
    const char* name = NULL;
    Tvg_Result result = tvg_lottie_animation_get_marker((Tvg_Animation) to_ptr(animHandle), (uint32_t) idx, &name);
    if (result == TVG_RESULT_SUCCESS && nameOut != NULL && name != NULL) {
        jstring str = (*env)->NewStringUTF(env, name);
        (*env)->SetObjectArrayElement(env, nameOut, 0, str);
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationGetMarkerInfo(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jint idx,
    jobjectArray nameOut,
    jfloatArray beginEndOut)
{
    (void) clazz;
    const char* name = NULL;
    float begin = 0.0f, end = 0.0f;
    Tvg_Result result = tvg_lottie_animation_get_marker_info((Tvg_Animation) to_ptr(animHandle), (uint32_t) idx, &name, &begin, &end);
    if (result == TVG_RESULT_SUCCESS) {
        if (nameOut != NULL && name != NULL) {
            jstring str = (*env)->NewStringUTF(env, name);
            (*env)->SetObjectArrayElement(env, nameOut, 0, str);
        }
        if (beginEndOut != NULL) {
            jfloat elems[2] = { begin, end };
            (*env)->SetFloatArrayRegion(env, beginEndOut, 0, 2, elems);
        }
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationTween(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jfloat from,
    jfloat to,
    jfloat progress)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_lottie_animation_tween((Tvg_Animation) to_ptr(animHandle), from, to, progress);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationSetQuality(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jint value)
{
    (void) env;
    (void) clazz;
    return (jint) tvg_lottie_animation_set_quality((Tvg_Animation) to_ptr(animHandle), (uint8_t) value);
}

static void native_audio_resolver_wrapper(const Tvg_Audio_Info* info, void* data) {
    if (g_vm == NULL || data == NULL || info == NULL) return;
    JNIEnv* env = NULL;
    jint get_env_res = (*g_vm)->GetEnv(g_vm, (void**)&env, JNI_VERSION_1_6);
    bool is_attached = false;
    if (get_env_res == JNI_EDETACHED) {
        if ((*g_vm)->AttachCurrentThread(g_vm, (void**)&env, NULL) != 0) {
            return;
        }
        is_attached = true;
    } else if (get_env_res != JNI_OK) {
        return;
    }

    jobject resolver_obj = (jobject) data;
    jclass clazz = (*env)->GetObjectClass(env, resolver_obj);
    jmethodID method = (*env)->GetMethodID(env, clazz, "onAudio", "(Ljava/lang/String;Ljava/lang/String;IFFZZ)V");
    if (method != NULL) {
        jstring src_str = info->src != NULL ? (*env)->NewStringUTF(env, info->src) : NULL;
        jstring mime_str = info->mimeType != NULL ? (*env)->NewStringUTF(env, info->mimeType) : NULL;

        (*env)->CallVoidMethod(
            env,
            resolver_obj,
            method,
            src_str,
            mime_str,
            (jint) info->size,
            (jfloat) info->offset,
            (jfloat) info->volume,
            (jboolean) (info->active ? JNI_TRUE : JNI_FALSE),
            (jboolean) (info->embedded ? JNI_TRUE : JNI_FALSE)
        );
        if (src_str != NULL) (*env)->DeleteLocalRef(env, src_str);
        if (mime_str != NULL) (*env)->DeleteLocalRef(env, mime_str);
    }
    (*env)->DeleteLocalRef(env, clazz);
    if (is_attached) (*g_vm)->DetachCurrentThread(g_vm);
}

JNIEXPORT jint JNICALL Java_io_github_xtrafrancyz_jthorvg_ThorvgNative_lottieAnimationSetAudioResolver(
    JNIEnv* env,
    jclass clazz,
    jlong animHandle,
    jobject resolver)
{
    (void) clazz;
    void* user_data = NULL;
    if (resolver != NULL) {
        user_data = (void*) (*env)->NewGlobalRef(env, resolver);
    }
    return (jint) tvg_lottie_animation_set_audio_resolver(
        (Tvg_Animation) to_ptr(animHandle),
        resolver != NULL ? native_audio_resolver_wrapper : NULL,
        user_data
    );
}
