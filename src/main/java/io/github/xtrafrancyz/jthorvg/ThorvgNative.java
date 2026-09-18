package io.github.xtrafrancyz.jthorvg;

import java.nio.Buffer;

final class ThorvgNative {
    private ThorvgNative() {
    }

    static native int engineInit(int threads);

    static native int engineTerm();

    static native EngineVersion engineVersion();

    static native long swCanvasNew(int engineOptionMask);

    static native int swCanvasSetTarget(long canvasHandle, Buffer buffer, int stride, int width, int height, int colorspace);

    static native int canvasAdd(long canvasHandle, long paintHandle);

    static native int canvasUpdate(long canvasHandle);

    static native int canvasDraw(long canvasHandle, boolean clear);

    static native int canvasSync(long canvasHandle);

    static native int canvasDestroy(long canvasHandle);

    static native long shapeNew();

    static native int shapeAppendRect(long paintHandle, float x, float y, float width, float height, float rx, float ry, boolean clockwise);

    static native int shapeSetFillColor(long paintHandle, int red, int green, int blue, int alpha);

    static native long pictureNew();

    static native int pictureLoad(long paintHandle, String path);

    static native int pictureSetSize(long paintHandle, float width, float height);

    static native int paintTranslate(long paintHandle, float x, float y);

    static native int paintScale(long paintHandle, float factor);

    static native int paintRel(long paintHandle);

    // Canvas & Paint APIs
    static native long glCanvasCreate(int engineOptionMask);

    static native int glCanvasSetTarget(long canvasHandle, long display, long surface, long context, int id, int width, int height, int colorspace);

    static native long wgCanvasCreate(int engineOptionMask);

    static native int wgCanvasSetTarget(long canvasHandle, long device, long instance, long target, int width, int height, int colorspace, int type);

    static native int wgCanvasSetTargetWithContext(long canvasHandle, long instance, long adapter, long device, long target, int width, int height, int colorspace, int type);

    static native int canvasInsert(long canvasHandle, long targetHandle, long atHandle);

    static native int canvasRemove(long canvasHandle, long paintHandle);

    static native int canvasSetViewport(long canvasHandle, int x, int y, int width, int height);

    static native int paintRef(long paintHandle);

    static native int paintUnref(long paintHandle, boolean free);

    static native int paintGetRef(long paintHandle);

    static native int paintSetVisible(long paintHandle, boolean visible);

    static native boolean paintGetVisible(long paintHandle);

    static native long paintGetData(long paintHandle);

    static native int paintSetData(long paintHandle, long data);

    static native int paintGetId(long paintHandle);

    static native int paintSetId(long paintHandle, int id);

    static native int paintRotate(long paintHandle, float degree);

    static native int paintSetTransform(long paintHandle, float[] matrix);

    static native int paintGetTransform(long paintHandle, float[] matrixOut);

    static native int paintSetOpacity(long paintHandle, int opacity);

    static native int paintGetOpacity(long paintHandle, int[] opacityOut);

    static native long paintDuplicate(long paintHandle);

    static native boolean paintIntersects(long paintHandle, int x, int y, int width, int height);

    static native int paintGetAabb(long paintHandle, float[] ltrbOut);

    static native int paintGetObb(long paintHandle, float[] pointsOut);

    static native int paintSetMaskMethod(long paintHandle, long targetHandle, int method);

    static native int paintGetMaskMethod(long paintHandle, long[] targetOut, int[] methodOut);

    static native int paintSetClip(long paintHandle, long clipperHandle);

    static native long paintGetClip(long paintHandle);

    static native long paintGetParent(long paintHandle);

    static native int paintGetType(long paintHandle, int[] typeOut);

    static native int paintSetBlendMethod(long paintHandle, int method);

    // Shape & Gradient APIs
    static native int shapeReset(long paintHandle);

    static native int shapeMoveTo(long paintHandle, float x, float y);

    static native int shapeLineTo(long paintHandle, float x, float y);

    static native int shapeCubicTo(long paintHandle, float cx1, float cy1, float cx2, float cy2, float x, float y);

    static native int shapeClose(long paintHandle);

    static native int shapeAppendCircle(long paintHandle, float cx, float cy, float rx, float ry, boolean cw);

    static native int shapeAppendPath(long paintHandle, byte[] cmds, int cmdCnt, float[] pts, int ptsCnt);

    static native int shapeGetPath(long paintHandle, byte[][] outCmds, float[][] outPts);

    static native int shapeSetStrokeWidth(long paintHandle, float width);

    static native int shapeGetStrokeWidth(long paintHandle, float[] widthOut);

    static native int shapeSetStrokeColor(long paintHandle, int r, int g, int b, int a);

    static native int shapeGetStrokeColor(long paintHandle, int[] rgbaOut);

    static native int shapeSetStrokeGradient(long paintHandle, long gradHandle);

    static native int shapeGetStrokeGradient(long paintHandle, long[] gradHandleOut);

    static native int shapeSetStrokeDash(long paintHandle, float[] dashPattern, int cnt, float offset);

    static native int shapeGetStrokeDash(long paintHandle, float[][] outDashPattern, float[] outOffset);

    static native int shapeSetStrokeCap(long paintHandle, int cap);

    static native int shapeGetStrokeCap(long paintHandle, int[] capOut);

    static native int shapeSetStrokeJoin(long paintHandle, int join);

    static native int shapeGetStrokeJoin(long paintHandle, int[] joinOut);

    static native int shapeSetStrokeMiterlimit(long paintHandle, float miterlimit);

    static native int shapeGetStrokeMiterlimit(long paintHandle, float[] miterlimitOut);

    static native int shapeSetTrimpath(long paintHandle, float begin, float end, boolean simultaneous);

    static native int shapeGetFillColor(long paintHandle, int[] rgbaOut);

    static native int shapeSetFillRule(long paintHandle, int rule);

    static native int shapeGetFillRule(long paintHandle, int[] ruleOut);

    static native int shapeSetPaintOrder(long paintHandle, boolean strokeFirst);

    static native int shapeSetGradient(long paintHandle, long gradHandle);

    static native int shapeGetGradient(long paintHandle, long[] gradHandleOut);

    static native long linearGradientNew();

    static native long radialGradientNew();

    static native int linearGradientSet(long gradHandle, float x1, float y1, float x2, float y2);

    static native int linearGradientGet(long gradHandle, float[] x1y1x2y2Out);

    static native int radialGradientSet(long gradHandle, float cx, float cy, float r, float fx, float fy, float fr);

    static native int radialGradientGet(long gradHandle, float[] cxcyrFxfyfrOut);

    static native int gradientSetColorStops(long gradHandle, float[] offsets, int[] colors, int cnt);

    static native int gradientGetColorStops(long gradHandle, float[][] outOffsets, int[][] outColors);

    static native int gradientSetSpread(long gradHandle, int spread);

    static native int gradientGetSpread(long gradHandle, int[] spreadOut);

    static native int gradientSetTransform(long gradHandle, float[] matrix);

    static native int gradientGetTransform(long gradHandle, float[] matrixOut);

    static native int gradientGetType(long gradHandle, int[] typeOut);

    static native long gradientDuplicate(long gradHandle);

    static native int gradientDel(long gradHandle);

    // Picture & Scene APIs
    static native int pictureLoadRaw(long paintHandle, int[] data, int w, int h, int colorspace, boolean copy);

    static native int pictureLoadRawBuffer(long paintHandle, Buffer buffer, int w, int h, int colorspace, boolean copy);

    static native int pictureLoadData(long paintHandle, byte[] data, int size, String mimetype, String rpath, boolean copy);

    static native int pictureSetAssetResolver(long paintHandle, PictureAssetResolver resolver);

    static native int pictureGetSize(long paintHandle, float[] sizeOut);

    static native int pictureSetOrigin(long paintHandle, float x, float y);

    static native int pictureGetOrigin(long paintHandle, float[] originOut);

    static native long pictureGetPaint(long paintHandle, int id);

    static native int pictureSetFilter(long paintHandle, int method);

    static native int pictureSetAccessible(long paintHandle, boolean accessible);

    static native long sceneNew();

    static native int sceneAdd(long sceneHandle, long paintHandle);

    static native int sceneInsert(long sceneHandle, long targetHandle, long atHandle);

    static native int sceneRemove(long sceneHandle, long paintHandle);

    static native int sceneClearEffects(long sceneHandle);

    static native int sceneAddEffectGaussianBlur(long sceneHandle, double sigma, int direction, int border, int quality);

    static native int sceneAddEffectDropShadow(long sceneHandle, int r, int g, int b, int a, double angle, double distance, double sigma, int quality);

    static native int sceneAddEffectFill(long sceneHandle, int r, int g, int b, int a);

    static native int sceneAddEffectTint(long sceneHandle, int black_r, int black_g, int black_b, int white_r, int white_g, int white_b, double intensity);

    static native int sceneAddEffectTritone(long sceneHandle, int shadow_r, int shadow_g, int shadow_b, int midtone_r, int midtone_g, int midtone_b, int highlight_r, int highlight_g, int highlight_b, int blend);

    // Text & Font APIs
    static native long textNew();

    static native int textSetFont(long paintHandle, String name);

    static native int textSetSize(long paintHandle, float size);

    static native int textSetText(long paintHandle, String utf8);

    static native String textGetText(long paintHandle);

    static native int textAlign(long paintHandle, float x, float y);

    static native int textLayout(long paintHandle, float w, float h);

    static native int textWrapMode(long paintHandle, int mode);

    static native int textLineCount(long paintHandle);

    static native int textSpacing(long paintHandle, float letter, float line);

    static native int textSetItalic(long paintHandle, float shear);

    static native int textSetOutline(long paintHandle, float width, int r, int g, int b);

    static native int textSetColor(long paintHandle, int r, int g, int b);

    static native int textSetGradient(long paintHandle, long gradHandle);

    static native int textGetTextMetrics(long paintHandle, float[] metricsOut);

    static native int textGetGlyphMetrics(long paintHandle, String ch, float[] metricsOut, int[] nextIndexOut);

    static native int fontLoad(String path);

    static native int fontLoadData(String name, byte[] data, int size, String mimetype, boolean copy);

    static native int fontUnload(String path);

    // Saver APIs
    static native long saverNew();

    static native int saverSavePaint(long saverHandle, long paintHandle, String path, int quality);

    static native int saverSaveAnimation(long saverHandle, long animHandle, String path, int quality, int fps);

    static native int saverSync(long saverHandle);

    static native int saverDel(long saverHandle);

    // Animation APIs
    static native long animationNew();

    static native int animationSetFrame(long animHandle, float no);

    static native long animationGetPicture(long animHandle);

    static native int animationGetFrame(long animHandle, float[] noOut);

    static native int animationGetTotalFrame(long animHandle, float[] cntOut);

    static native int animationGetDuration(long animHandle, float[] durationOut);

    static native int animationSetSegment(long animHandle, float begin, float end);

    static native int animationGetSegment(long animHandle, float[] beginEndOut);

    static native int animationDel(long animHandle);

    // Accessor APIs
    static native long accessorNew();

    static native int accessorDel(long accessorHandle);

    static native int accessorSet(long accessorHandle, long paintHandle, AccessorCallback callback);

    static native int accessorGenerateId(String name);

    static native String accessorGetName(long accessorHandle, int id);

    // LottieAnimation APIs
    static native long lottieAnimationNew();

    static native boolean lottieAnimationExpressionsSupported();

    static native int lottieAnimationGenSlot(long animHandle, String slot);

    static native int lottieAnimationApplySlot(long animHandle, int id);

    static native int lottieAnimationDelSlot(long animHandle, int id);

    static native int lottieAnimationSetMarker(long animHandle, String marker);

    static native int lottieAnimationGetMarkersCnt(long animHandle, int[] cntOut);

    static native int lottieAnimationGetMarker(long animHandle, int idx, String[] nameOut);

    static native int lottieAnimationGetMarkerInfo(long animHandle, int idx, String[] nameOut, float[] beginEndOut);

    static native int lottieAnimationTween(long animHandle, float from, float to, float progress);

    static native int lottieAnimationSetQuality(long animHandle, int value);

    static native int lottieAnimationSetVolume(long animHandle, float volume);

    static native float lottieAnimationGetVolume(long animHandle);

    static native int lottieAnimationSetAudioResolver(long animHandle, LottieAudioResolver resolver);
}
