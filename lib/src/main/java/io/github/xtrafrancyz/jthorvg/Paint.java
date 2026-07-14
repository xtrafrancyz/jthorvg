package io.github.xtrafrancyz.jthorvg;

public abstract class Paint extends NativeHandle {
    Paint(long handle) {
        super(handle);
    }

    public final void translate(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.paintTranslate(requireHandle(), x, y))
            .throwIfError("tvg_paint_translate");
    }

    public final void scale(float factor) {
        ThorvgResult.fromCode(ThorvgNative.paintScale(requireHandle(), factor))
            .throwIfError("tvg_paint_scale");
    }

    public final int ref() {
        return ThorvgNative.paintRef(requireHandle());
    }

    public final int unref(boolean free) {
        return ThorvgNative.paintUnref(requireHandle(), free);
    }

    public final int getRef() {
        return ThorvgNative.paintGetRef(requireHandle());
    }

    public final void setVisible(boolean visible) {
        ThorvgResult.fromCode(ThorvgNative.paintSetVisible(requireHandle(), visible))
            .throwIfError("tvg_paint_set_visible");
    }

    public final boolean getVisible() {
        return ThorvgNative.paintGetVisible(requireHandle());
    }

    public final int getId() {
        return ThorvgNative.paintGetId(requireHandle());
    }

    public final void setId(int id) {
        ThorvgResult.fromCode(ThorvgNative.paintSetId(requireHandle(), id))
            .throwIfError("tvg_paint_set_id");
    }

    public final void rotate(float degree) {
        ThorvgResult.fromCode(ThorvgNative.paintRotate(requireHandle(), degree))
            .throwIfError("tvg_paint_rotate");
    }

    public final void setTransform(float[] matrix) {
        if (matrix != null && matrix.length != 9) {
            throw new IllegalArgumentException("matrix must be an array of length 9");
        }
        ThorvgResult.fromCode(ThorvgNative.paintSetTransform(requireHandle(), matrix))
            .throwIfError("tvg_paint_set_transform");
    }

    public final float[] getTransform() {
        float[] matrixOut = new float[9];
        ThorvgResult.fromCode(ThorvgNative.paintGetTransform(requireHandle(), matrixOut))
            .throwIfError("tvg_paint_get_transform");
        return matrixOut;
    }

    public final void setOpacity(int opacity) {
        if (opacity < 0 || opacity > 255) {
            throw new IllegalArgumentException("opacity must be between 0 and 255");
        }
        ThorvgResult.fromCode(ThorvgNative.paintSetOpacity(requireHandle(), opacity))
            .throwIfError("tvg_paint_set_opacity");
    }

    public final int getOpacity() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.paintGetOpacity(requireHandle(), out))
            .throwIfError("tvg_paint_get_opacity");
        return out[0];
    }

    public final boolean intersects(int x, int y, int width, int height) {
        return ThorvgNative.paintIntersects(requireHandle(), x, y, width, height);
    }

    public final float[] getAabb() {
        float[] out = new float[4];
        ThorvgResult.fromCode(ThorvgNative.paintGetAabb(requireHandle(), out))
            .throwIfError("tvg_paint_get_aabb");
        return out;
    }

    public final float[] getObb() {
        float[] out = new float[8];
        ThorvgResult.fromCode(ThorvgNative.paintGetObb(requireHandle(), out))
            .throwIfError("tvg_paint_get_obb");
        return out;
    }

    public final void setMaskMethod(Paint target, int method) {
        ThorvgResult.fromCode(ThorvgNative.paintSetMaskMethod(requireHandle(), target.requireHandle(), method))
            .throwIfError("tvg_paint_set_mask_method");
    }

    public final long[] getMaskMethod() {
        long[] targetOut = new long[1];
        int[] methodOut = new int[1];
        ThorvgResult.fromCode(ThorvgNative.paintGetMaskMethod(requireHandle(), targetOut, methodOut))
            .throwIfError("tvg_paint_get_mask_method");
        return new long[]{targetOut[0], methodOut[0]};
    }

    public final void setClip(Paint clipper) {
        ThorvgResult.fromCode(ThorvgNative.paintSetClip(requireHandle(), clipper.requireHandle()))
            .throwIfError("tvg_paint_set_clip");
    }

    public final long getClip() {
        return ThorvgNative.paintGetClip(requireHandle());
    }

    public final long getParent() {
        return ThorvgNative.paintGetParent(requireHandle());
    }

    public final int getType() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.paintGetType(requireHandle(), out))
            .throwIfError("tvg_paint_get_type");
        return out[0];
    }

    public final void setBlendMethod(int method) {
        ThorvgResult.fromCode(ThorvgNative.paintSetBlendMethod(requireHandle(), method))
            .throwIfError("tvg_paint_set_blend_method");
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.paintRel(handle)).throwIfError("tvg_paint_rel");
        clearHandle();
    }
}
