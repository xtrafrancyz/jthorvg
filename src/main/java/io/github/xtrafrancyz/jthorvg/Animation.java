package io.github.xtrafrancyz.jthorvg;

/**
 * A module for manipulation of animatable images.
 * <p>
 * The module supports the display and control of animation frames.
 */
public class Animation extends NativeHandle {
    Animation(long handle) {
        super(handle);
    }

    /**
     * Specifies the current frame in the animation.
     * <p>
     * <b>Note:</b> For efficiency, ThorVG ignores updates to the new frame value if the difference from the current frame value
     * is less than 0.001. In such cases, it returns Result::InsufficientCondition.
     * Values less than 0.001 may be disregarded and may not be accurately retained by the Animation.
     *
     * @param no The index of the animation frame to be displayed. The index should be less than the getTotalFrame().
     * @return true if the frame was successfully set, false if the difference from the current frame is less than 0.001.
     */
    public final boolean setFrame(float no) {
        ThorvgResult result = ThorvgResult.fromCode(ThorvgNative.animationSetFrame(requireHandle(), no));
        if (result == ThorvgResult.INSUFFICIENT_CONDITION)
            return false; // Ignore if the difference is less than 0.001
        result.throwIfError("tvg_animation_set_frame");
        return true;
    }

    /**
     * Retrieves a picture instance associated with this animation instance.
     * <p>
     * This function provides access to the picture instance that can be used to load animation formats, such as lot.
     * After setting up the picture, it can be added to the designated canvas, enabling control over animation frames
     * with this Animation instance.
     * <p>
     * <b>Warning:</b> The picture instance is owned by Animation. It should not be deleted manually.
     *
     * @return A picture instance handle that is tied to this animation.
     */
    public final Picture getPicture() {
        return new Picture(ThorvgNative.animationGetPicture(requireHandle()), false);
    }

    /**
     * Retrieves the current frame number of the animation.
     *
     * @return The current frame number of the animation, between 0 and totalFrame() - 1.
     */
    public final float getFrame() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.animationGetFrame(requireHandle(), out))
            .throwIfError("tvg_animation_get_frame");
        return out[0];
    }

    /**
     * Retrieves the total number of frames in the animation.
     * <p>
     * <b>Note:</b> Frame numbering starts from 0.
     * <p>
     * <b>Note:</b> If the Picture is not properly configured, this function will return 0.
     *
     * @return The total number of frames in the animation.
     */
    public final float getTotalFrame() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.animationGetTotalFrame(requireHandle(), out))
            .throwIfError("tvg_animation_get_total_frame");
        return out[0];
    }

    /**
     * Retrieves the duration of the animation in seconds.
     * <p>
     * <b>Note:</b> If the Picture is not properly configured, this function will return 0.
     *
     * @return The duration of the animation in seconds.
     */
    public final float getDuration() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.animationGetDuration(requireHandle(), out))
            .throwIfError("tvg_animation_get_duration");
        return out[0];
    }

    /**
     * Specifies the playback segment of the animation.
     * <p>
     * The set segment is designated as the play area of the animation.
     * This is useful for playing a specific segment within the entire animation.
     * After setting, the number of animation frames and the playback time are calculated
     * by mapping the playback segment as the entire range.
     * <p>
     * <b>Note:</b> Animation allows a range from 0.0 to the total frame. end should not be lower than begin.
     * <p>
     * <b>Note:</b> If a marker has been specified, its range will be disregarded.
     *
     * @param begin segment begin frame.
     * @param end   segment end frame.
     */
    public final void setSegment(float begin, float end) {
        ThorvgResult.fromCode(ThorvgNative.animationSetSegment(requireHandle(), begin, end))
            .throwIfError("tvg_animation_set_segment");
    }

    /**
     * Gets the current segment range information.
     *
     * @return A float array containing [begin, end] segment frames.
     */
    public final float[] getSegment() {
        float[] out = new float[2];
        ThorvgResult.fromCode(ThorvgNative.animationGetSegment(requireHandle(), out))
            .throwIfError("tvg_animation_get_segment");
        return out;
    }

    /**
     * Deletes the given Animation object.
     */
    @Override
    public void close() {
        if (isClosed())
            return;
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.animationDel(handle)).throwIfError("tvg_animation_del");
        clearHandle();
    }
}
