package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

public final class LottieAnimation extends Animation {
    LottieAnimation(long handle) {
        super(handle);
    }

    public int genSlot(String slot) {
        Objects.requireNonNull(slot, "slot");
        return ThorvgNative.lottieAnimationGenSlot(requireHandle(), slot);
    }

    public void applySlot(int id) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationApplySlot(requireHandle(), id))
            .throwIfError("tvg_lottie_animation_apply_slot");
    }

    public void delSlot(int id) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationDelSlot(requireHandle(), id))
            .throwIfError("tvg_lottie_animation_del_slot");
    }

    public void setMarker(String marker) {
        Objects.requireNonNull(marker, "marker");
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationSetMarker(requireHandle(), marker))
            .throwIfError("tvg_lottie_animation_set_marker");
    }

    public int getMarkersCount() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationGetMarkersCnt(requireHandle(), out))
            .throwIfError("tvg_lottie_animation_get_markers_cnt");
        return out[0];
    }

    public String getMarker(int idx) {
        String[] out = new String[1];
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationGetMarker(requireHandle(), idx, out))
            .throwIfError("tvg_lottie_animation_get_marker");
        return out[0];
    }

    public String getMarkerInfo(int idx, float[] beginEndOut) {
        if (beginEndOut != null && beginEndOut.length < 2) {
            throw new IllegalArgumentException("beginEndOut must be of length 2 or greater");
        }
        String[] out = new String[1];
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationGetMarkerInfo(requireHandle(), idx, out, beginEndOut))
            .throwIfError("tvg_lottie_animation_get_marker_info");
        return out[0];
    }

    public void tween(float from, float to, float progress) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationTween(requireHandle(), from, to, progress))
            .throwIfError("tvg_lottie_animation_tween");
    }

    public void setQuality(int value) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationSetQuality(requireHandle(), value))
            .throwIfError("tvg_lottie_animation_set_quality");
    }

    public void setAudioResolver(LottieAudioResolver resolver) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationSetAudioResolver(requireHandle(), resolver))
            .throwIfError("tvg_lottie_animation_set_audio_resolver");
    }
}
