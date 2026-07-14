package io.github.xtrafrancyz.jthorvg;

public final class WGCanvas extends Canvas {
    WGCanvas(long handle) {
        super(handle);
    }

    public void setTarget(long device, long instance, long target, int width, int height, ThorvgColorspace colorspace, int type) {
        ThorvgResult.fromCode(ThorvgNative.wgCanvasSetTarget(
            requireHandle(),
            device,
            instance,
            target,
            width,
            height,
            colorspace.code(),
            type))
            .throwIfError("tvg_wgcanvas_set_target");
    }

    public void setTargetWithContext(long instance, long adapter, long device, long target, int width, int height, ThorvgColorspace colorspace, int type) {
        ThorvgResult.fromCode(ThorvgNative.wgCanvasSetTargetWithContext(
            requireHandle(),
            instance,
            adapter,
            device,
            target,
            width,
            height,
            colorspace.code(),
            type))
            .throwIfError("tvg_wgcanvas_set_target_with_context");
    }
}
