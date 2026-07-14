package io.github.xtrafrancyz.jthorvg;

public final class SoftwareCanvas extends NativeHandle {
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

    public void add(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.canvasAdd(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_canvas_add");
    }

    public void update() {
        ThorvgResult.fromCode(ThorvgNative.canvasUpdate(requireHandle())).throwIfError("tvg_canvas_update");
    }

    public void draw(boolean clear) {
        ThorvgResult.fromCode(ThorvgNative.canvasDraw(requireHandle(), clear)).throwIfError("tvg_canvas_draw");
    }

    public void sync() {
        ThorvgResult.fromCode(ThorvgNative.canvasSync(requireHandle())).throwIfError("tvg_canvas_sync");
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.canvasDestroy(handle)).throwIfError("tvg_canvas_destroy");
        clearHandle();
        target = null;
    }
}
