package io.github.xtrafrancyz.jthorvg;

public final class SoftwareCanvas extends Canvas {
    private SoftwareCanvasTarget target;

    SoftwareCanvas(long handle) {
        super(handle);
    }

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
