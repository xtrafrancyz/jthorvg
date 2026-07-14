package io.github.xtrafrancyz.jthorvg;

/**
 * A module for rendering the graphical elements using the opengl engine.
 */
public final class GLCanvas extends Canvas {
    GLCanvas(long handle) {
        super(handle);
    }

    /**
     * Sets the drawing target for rasterization.
     * <p>
     * This function specifies the drawing target where the rasterization will occur. It can target
     * a specific framebuffer object (FBO) or the main surface.
     *
     * @param display    The platform-specific display handle (EGLDisplay for EGL). Set 0 for other systems.
     * @param surface    The platform-specific surface handle (EGLSurface for EGL, HDC for WGL). Set 0 for other systems.
     * @param context    The OpenGL context to be used for rendering on this canvas.
     * @param id         The GL target ID, usually indicating the FBO ID. A value of 0 specifies the main surface.
     * @param width      The width (in pixels) of the raster image.
     * @param height     The height (in pixels) of the raster image.
     * @param colorspace Specifies how the pixel values should be interpreted. Currently, it only allows {@link ThorvgColorspace#ABGR8888S} as GL_RGBA8.
     * @note If display and surface are not provided, the ThorVG GL engine assumes that
     *       the appropriate OpenGL context is already current and will not attempt to bind a new one.
     */
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
