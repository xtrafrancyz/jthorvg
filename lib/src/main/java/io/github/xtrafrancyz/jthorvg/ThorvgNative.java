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
}
