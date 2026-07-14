package io.github.xtrafrancyz.jthorvg;

/**
 * A module for rendering the graphical elements using the webgpu engine.
 */
public final class WGCanvas extends Canvas {
    WGCanvas(long handle) {
        super(handle);
    }

    /**
     * Sets the drawing target for the rasterization.
     *
     * @param device     WGPUDevice, a desired handle for the wgpu device.
     * @param instance   WGPUInstance, context for all other wgpu objects.
     * @param target     Either WGPUSurface or WGPUTexture, serving as handles to a presentable surface or texture.
     * @param width      The width of the target.
     * @param height     The height of the target.
     * @param colorspace Specifies how the pixel values should be interpreted. Currently, it allows ABGR8888 and ABGR8888S.
     * @param type       0: surface, 1: texture are used as presentable target.
     * <b>Warning:</b> Regardless of the value of colorspace, this target API uses the default alpha mode.
     */
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

    /**
     * Sets the drawing target for the rasterization with context.
     *
     * @param instance   WGPUInstance, context for all other wgpu objects.
     * @param adapter    WGPUAdapter, the adapter associated with the rendering device.
     * @param device     WGPUDevice, a desired handle for the wgpu device.
     * @param target     Either WGPUSurface or WGPUTexture, serving as handles to a presentable surface or texture.
     * @param width      The width of the target.
     * @param height     The height of the target.
     * @param colorspace Specifies how the pixel values should be interpreted. Currently, it allows ABGR8888 and ABGR8888S.
     * @param type       0: surface, 1: texture are used as presentable target.
     */
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
