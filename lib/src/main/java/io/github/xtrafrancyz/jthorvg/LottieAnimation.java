package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

/**
 * A module for manipulation of lottie extension features.
 * <p>
 * The module enables control of advanced Lottie features.
 */
public final class LottieAnimation extends Animation {
    LottieAnimation(long handle) {
        super(handle);
    }

    /**
     * Generates a new slot from the given slot data.
     *
     * @param slot The Lottie slot data in JSON format.
     * @return The generated slot ID when successful, 0 otherwise.
     */
    public int genSlot(String slot) {
        Objects.requireNonNull(slot, "slot");
        return ThorvgNative.lottieAnimationGenSlot(requireHandle(), slot);
    }

    /**
     * Applies a previously generated slot to the animation.
     *
     * @param id The ID of the slot to apply, or 0 to reset all slots.
     */
    public void applySlot(int id) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationApplySlot(requireHandle(), id))
            .throwIfError("tvg_lottie_animation_apply_slot");
    }

    /**
     * Deletes a previously generated slot.
     *
     * @param id The ID of the slot to delete.
     * <b>Note:</b> This function should be paired with genSlot.
     */
    public void delSlot(int id) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationDelSlot(requireHandle(), id))
            .throwIfError("tvg_lottie_animation_del_slot");
    }

    /**
     * Specifies a segment by marker.
     *
     * @param marker The name of the segment marker.
     */
    public void setMarker(String marker) {
        Objects.requireNonNull(marker, "marker");
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationSetMarker(requireHandle(), marker))
            .throwIfError("tvg_lottie_animation_set_marker");
    }

    /**
     * Gets the marker count of the animation.
     *
     * @return The count value of the markers.
     */
    public int getMarkersCount() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationGetMarkersCnt(requireHandle(), out))
            .throwIfError("tvg_lottie_animation_get_markers_cnt");
        return out[0];
    }

    /**
     * Gets the marker name by a given index.
     *
     * @param idx The index of the animation marker, starts from 0.
     * @return The name of marker when succeed.
     */
    public String getMarker(int idx) {
        String[] out = new String[1];
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationGetMarker(requireHandle(), idx, out))
            .throwIfError("tvg_lottie_animation_get_marker");
        return out[0];
    }

    /**
     * Retrieves marker information by index.
     *
     * @param idx          The zero-based index of the animation marker.
     * @param beginEndOut  An array to receive [begin, end] frames of the marker.
     * @return The marker name.
     */
    public String getMarkerInfo(int idx, float[] beginEndOut) {
        if (beginEndOut != null && beginEndOut.length < 2) {
            throw new IllegalArgumentException("beginEndOut must be of length 2 or greater");
        }
        String[] out = new String[1];
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationGetMarkerInfo(requireHandle(), idx, out, beginEndOut))
            .throwIfError("tvg_lottie_animation_get_marker_info");
        return out[0];
    }

    /**
     * Interpolates between two frames over a specified duration.
     * <p>
     * This method performs tweening, a process of generating intermediate frame
     * between from and to based on the given progress.
     *
     * @param from     The start frame number of the interpolation.
     * @param to       The end frame number of the interpolation.
     * @param progress The current progress of the interpolation (range: 0.0 to 1.0).
     */
    public void tween(float from, float to, float progress) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationTween(requireHandle(), from, to, progress))
            .throwIfError("tvg_lottie_animation_tween");
    }

    /**
     * Sets the quality level for Lottie effects.
     * <p>
     * This function controls the rendering quality of effects like blur, shadows, etc.
     * Lower values prioritize performance while higher values prioritize quality.
     *
     * @param value The quality level (0-100). 0 represents lowest quality/best performance,
     *              100 represents highest quality/lowest performance, default is 50.
     * <b>Note:</b> This option is used as a hint; its behavior heavily depends on the render backend.
     */
    public void setQuality(int value) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationSetQuality(requireHandle(), value))
            .throwIfError("tvg_lottie_animation_set_quality");
    }

    /**
     * Sets the audio resolver callback for Lottie audio layers.
     * <p>
     * The resolver is invoked whenever the playback state of an audio layer changes.
     * It allows applications to synchronize audio playback with the animation timeline.
     *
     * @param resolver A LottieAudioResolver instance.
     * <b>Note:</b> To disable audio notifications, pass null as resolver.
     */
    public void setAudioResolver(LottieAudioResolver resolver) {
        ThorvgResult.fromCode(ThorvgNative.lottieAnimationSetAudioResolver(requireHandle(), resolver))
            .throwIfError("tvg_lottie_animation_set_audio_resolver");
    }
}
