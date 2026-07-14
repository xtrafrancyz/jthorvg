package io.github.xtrafrancyz.jthorvg;

public class Animation extends NativeHandle {
    Animation(long handle) {
        super(handle);
    }

    public final void setFrame(float no) {
        ThorvgResult.fromCode(ThorvgNative.animationSetFrame(requireHandle(), no))
            .throwIfError("tvg_animation_set_frame");
    }

    public final long getPicture() {
        return ThorvgNative.animationGetPicture(requireHandle());
    }

    public final float getFrame() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.animationGetFrame(requireHandle(), out))
            .throwIfError("tvg_animation_get_frame");
        return out[0];
    }

    public final float getTotalFrame() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.animationGetTotalFrame(requireHandle(), out))
            .throwIfError("tvg_animation_get_total_frame");
        return out[0];
    }

    public final float getDuration() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.animationGetDuration(requireHandle(), out))
            .throwIfError("tvg_animation_get_duration");
        return out[0];
    }

    public final void setSegment(float begin, float end) {
        ThorvgResult.fromCode(ThorvgNative.animationSetSegment(requireHandle(), begin, end))
            .throwIfError("tvg_animation_set_segment");
    }

    public final float[] getSegment() {
        float[] out = new float[2];
        ThorvgResult.fromCode(ThorvgNative.animationGetSegment(requireHandle(), out))
            .throwIfError("tvg_animation_get_segment");
        return out;
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.animationDel(handle)).throwIfError("tvg_animation_del");
        clearHandle();
    }
}
