package io.github.xtrafrancyz.jthorvg;

/**
 * A module for rendering the graphical elements using the software engine.
 */
public final class SoftwareCanvas extends Canvas {
    private SoftwareCanvasTarget target;

    SoftwareCanvas(long handle) {
        super(handle);
    }

    /**
     * Sets the buffer used in the rasterization process and defines the used colorspace.
     * <p>
     * For optimisation reasons TVG does not allocate memory for the output buffer on its own.
     * The buffer of a desirable size should be allocated and owned by the caller.
     *
     * @param target The software canvas target buffer configuration.
     * @warning Do not access buffer during draw() - sync(). It should not be accessed while the engine is writing on it.
     * @note Currently, only TVG_COLORSPACE_ABGR8888, TVG_COLORSPACE_ARGB8888, TVG_COLORSPACE_ABGR8888S, and TVG_COLORSPACE_ARGB8888S are supported for cs.
     */
    public void setTarget(SoftwareCanvasTarget target) {
        ThorvgResult.fromCode(ThorvgNative.swCanvasSetTarget(
            requireHandle(),
            target.nativeBuffer(),
            target.stride(),
            target.width(),
            target.height(),
            target.colorspace().code()))
            .throwIfError("tvg_swcanvas_set_target");
        this.target = target;
    }

    @Override
    public void close() {
        super.close();
        target = null;
    }
}
