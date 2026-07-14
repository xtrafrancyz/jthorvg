package io.github.xtrafrancyz.jthorvg;

public final class GLCanvas extends Canvas {
    GLCanvas(long handle) {
        super(handle);
    }

    public void setTarget(long display, long surface, long context, int id, int width, int height, ThorvgColorspace colorspace) {
        ThorvgResult.fromCode(ThorvgNative.glCanvasSetTarget(
            requireHandle(),
            display,
            surface,
            context,
            id,
            width,
            height,
            colorspace.code()))
            .throwIfError("tvg_glcanvas_set_target");
    }
}
