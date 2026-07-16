package io.github.xtrafrancyz.jthorvg;

/**
 * A class representing a graphical element.
 * <p>
 * Warning: The Paint objects cannot be shared between Canvases.
 */
public abstract class Paint extends NativeHandle {
    Paint(long handle) {
        super(handle);
    }

    /**
     * Moves the given Paint in a two-dimensional space.
     * <p>
     * The origin of the coordinate system is in the upper-left corner of the canvas.
     * The horizontal and vertical axes point to the right and down, respectively.
     *
     * @param x The value of the horizontal shift.
     * @param y The value of the vertical shift.
     */
    public final void translate(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.paintTranslate(requireHandle(), x, y))
            .throwIfError("tvg_paint_translate");
    }

    /**
     * Scales the given Paint object by the given factor.
     *
     * @param factor The value of the scaling factor. The default value is 1.
     */
    public final void scale(float factor) {
        ThorvgResult.fromCode(ThorvgNative.paintScale(requireHandle(), factor))
            .throwIfError("tvg_paint_scale");
    }

    /**
     * Increment the reference count for the Paint object.
     * <p>
     * This method increases the reference count of Paint object, allowing shared ownership and control over its lifetime.
     *
     * @return The updated reference count after the increment by 1.
     * <b>Warning:</b> Please ensure that each call to ref() is paired with a corresponding call to unref() to prevent a dangling instance.
     */
    public final int ref() {
        return ThorvgNative.paintRef(requireHandle());
    }

    /**
     * Decrement the reference count for the Paint object.
     * <p>
     * This method decreases the reference count of the Paint object by 1.
     * If the reference count reaches zero and the free flag is set to true, the instance is automatically deleted.
     *
     * @param free Flag indicating whether to delete the Paint instance when the reference count reaches zero.
     * @return The updated reference count after the decrement.
     */
    public final int unref(boolean free) {
        return ThorvgNative.paintUnref(requireHandle(), free);
    }

    /**
     * Retrieve the current reference count of the Paint object.
     * <p>
     * This method provides the current reference count, allowing the user to check the shared ownership state of the Paint object.
     *
     * @return The current reference count of the Paint object.
     */
    public final int getRef() {
        return ThorvgNative.paintGetRef(requireHandle());
    }

    /**
     * Sets the visibility of the Paint object.
     * <p>
     * This is useful for selectively excluding paint objects during rendering.
     * <p>
     * <b>Note:</b> An invisible object is not considered inactive—it may still participate
     * in internal update processing if its properties are updated, but it will not
     * be taken into account for the final drawing output. To completely deactivate
     * a paint object, remove it from the canvas.
     *
     * @param visible A boolean flag indicating visibility. The default is true.
     *                true, the object will be rendered by the engine.
     *                false, the object will be excluded from the drawing process.
     */
    public final void setVisible(boolean visible) {
        ThorvgResult.fromCode(ThorvgNative.paintSetVisible(requireHandle(), visible))
            .throwIfError("tvg_paint_set_visible");
    }

    /**
     * Gets the current visibility status of the Paint object.
     *
     * @return true if the object is visible and will be rendered; false if the object is hidden and will not be rendered.
     */
    public final boolean getVisible() {
        return ThorvgNative.paintGetVisible(requireHandle());
    }

    /**
     * Gets the ID of the Paint object.
     *
     * @return The ID of the paint object, or 0 if the ID is not set.
     */
    public final int getId() {
        return ThorvgNative.paintGetId(requireHandle());
    }

    /**
     * Sets the ID of the Paint object.
     * <p>
     * The ID is used to specify a paint instance in a scene.
     *
     * @param id The ID to assign to the paint object.
     */
    public final void setId(int id) {
        ThorvgResult.fromCode(ThorvgNative.paintSetId(requireHandle(), id))
            .throwIfError("tvg_paint_set_id");
    }

    /**
     * Rotates the given Paint by the given angle.
     * <p>
     * The angle in measured clockwise from the horizontal axis.
     * The rotational axis passes through the point on the object with zero coordinates.
     *
     * @param degree The value of the rotation angle in degrees.
     */
    public final void rotate(float degree) {
        ThorvgResult.fromCode(ThorvgNative.paintRotate(requireHandle(), degree))
            .throwIfError("tvg_paint_rotate");
    }

    /**
     * Transforms the given Paint using the augmented transformation matrix.
     * <p>
     * The augmented matrix of the transformation is expected to be given.
     *
     * @param matrix The 3x3 augmented matrix.
     */
    public final void setTransform(float[] matrix) {
        if (matrix != null && matrix.length != 9) {
            throw new IllegalArgumentException("matrix must be an array of length 9");
        }
        ThorvgResult.fromCode(ThorvgNative.paintSetTransform(requireHandle(), matrix))
            .throwIfError("tvg_paint_set_transform");
    }

    /**
     * Gets the matrix of the affine transformation of the given Paint object.
     * <p>
     * In case no transformation was applied, the identity matrix is returned.
     *
     * @return The 3x3 augmented matrix.
     */
    public final float[] getTransform() {
        float[] matrixOut = new float[9];
        ThorvgResult.fromCode(ThorvgNative.paintGetTransform(requireHandle(), matrixOut))
            .throwIfError("tvg_paint_get_transform");
        return matrixOut;
    }

    /**
     * Sets the opacity of the given Paint.
     * <p>
     * <b>Note:</b> Setting the opacity with this API may require multiple renderings using a composition. It is recommended to avoid changing the opacity if possible.
     *
     * @param opacity The opacity value in the range [0 ~ 255], where 0 is completely transparent and 255 is opaque.
     */
    public final void setOpacity(int opacity) {
        if (opacity < 0 || opacity > 255) {
            throw new IllegalArgumentException("opacity must be between 0 and 255");
        }
        ThorvgResult.fromCode(ThorvgNative.paintSetOpacity(requireHandle(), opacity))
            .throwIfError("tvg_paint_set_opacity");
    }

    /**
     * Gets the opacity of the given Paint.
     *
     * @return The opacity value in the range [0 ~ 255], where 0 is completely transparent and 255 is opaque.
     */
    public final int getOpacity() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.paintGetOpacity(requireHandle(), out))
            .throwIfError("tvg_paint_get_opacity");
        return out[0];
    }

    /**
     * Checks whether a given region intersects the filled area of the paint.
     * <p>
     * This function determines whether the specified rectangular region—defined by (x, y, w, h)—
     * intersects the geometric fill region of the paint object.
     * <p>
     * This is useful for hit-testing purposes, such as detecting whether a user interaction (e.g., touch or click)
     * occurs within a painted region.
     * <p>
     * The paint must be updated in a Canvas beforehand—typically after the Canvas has been
     * drawn and synchronized.
     * <p>
     * <b>Note:</b> To test a single point, set the region size to w = 1, h = 1.
     * <p>
     * <b>Note:</b> For efficiency, an AABB (axis-aligned bounding box) test is performed internally before precise hit detection.
     * <p>
     * <b>Note:</b> This test does not take into account the results of blending or masking.
     * <p>
     * <b>Note:</b> This test does take into account the the hidden paints as well.
     *
     * @param x      The x-coordinate of the top-left corner of the test region.
     * @param y      The y-coordinate of the top-left corner of the test region.
     * @param width  The width of the region to test. Must be greater than 0; defaults to 1.
     * @param height The height of the region to test. Must be greater than 0; defaults to 1.
     * @return true if any part of the region intersects the filled area; otherwise, false.
     */
    public final boolean intersects(int x, int y, int width, int height) {
        return ThorvgNative.paintIntersects(requireHandle(), x, y, width, height);
    }

    /**
     * Retrieves the axis-aligned bounding box (AABB) of the paint object in canvas space.
     * <p>
     * Returns the bounding box of the paint as an axis-aligned bounding box (AABB), with all relevant transformations applied.
     *
     * @return An array of float containing [x, y, w, h] of the bounding box.
     */
    public final float[] getAabb() {
        float[] out = new float[4];
        ThorvgResult.fromCode(ThorvgNative.paintGetAabb(requireHandle(), out))
            .throwIfError("tvg_paint_get_aabb");
        return out;
    }

    /**
     * Retrieves the object-oriented bounding box (OBB) of the paint object in canvas space.
     * <p>
     * This function returns the bounding box of the paint, as an oriented bounding box (OBB) after transformations are applied.
     *
     * @return An array of float containing 8 values representing 4 points of the OBB [x1, y1, x2, y2, x3, y3, x4, y4].
     */
    public final float[] getObb() {
        float[] out = new float[8];
        ThorvgResult.fromCode(ThorvgNative.paintGetObb(requireHandle(), out))
            .throwIfError("tvg_paint_get_obb");
        return out;
    }

    /**
     * Sets the masking target object and the masking method.
     *
     * @param target The target object of the masking.
     * @param method The method used to mask the source object with the target.
     */
    public final void setMaskMethod(Paint target, int method) {
        ThorvgResult.fromCode(ThorvgNative.paintSetMaskMethod(requireHandle(), target.requireHandle(), method))
            .throwIfError("tvg_paint_set_mask_method");
    }

    /**
     * Gets the masking target object and the masking method.
     *
     * @return A long array of length 2 containing [targetHandle, method].
     */
    public final long[] getMaskMethod() {
        long[] targetOut = new long[1];
        int[] methodOut = new int[1];
        ThorvgResult.fromCode(ThorvgNative.paintGetMaskMethod(requireHandle(), targetOut, methodOut))
            .throwIfError("tvg_paint_get_mask_method");
        return new long[]{targetOut[0], methodOut[0]};
    }

    /**
     * Clip the drawing region of the paint object.
     * <p>
     * This function restricts the drawing area of the paint object to the specified shape's paths.
     *
     * @param clipper The shape object as the clipper.
     */
    public final void setClip(Paint clipper) {
        ThorvgResult.fromCode(ThorvgNative.paintSetClip(requireHandle(), clipper.requireHandle()))
            .throwIfError("tvg_paint_set_clip");
    }

    /**
     * Get the clipper shape of the paint object.
     *
     * @return The handle of the shape object used as the clipper, or 0 if no clipper is set.
     */
    public final long getClip() {
        return ThorvgNative.paintGetClip(requireHandle());
    }

    /**
     * Retrieves the parent paint object.
     * <p>
     * This function returns a pointer to the parent object if the current paint
     * belongs to one. Otherwise, it returns 0.
     *
     * @return A handle to the parent object if available, otherwise 0.
     */
    public final long getParent() {
        return ThorvgNative.paintGetParent(requireHandle());
    }

    /**
     * Gets the unique value of the paint instance indicating the instance type.
     *
     * @return The unique type of the paint instance type.
     */
    public final int getType() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.paintGetType(requireHandle(), out))
            .throwIfError("tvg_paint_get_type");
        return out[0];
    }

    /**
     * Sets the blending method for the paint object.
     * <p>
     * The blending feature allows you to combine colors to create visually appealing effects, including transparency, lighting, shading, and color mixing, among others.
     * Its process involves the combination of colors or images from the source paint object with the destination (the lower layer image) using blending operations.
     * The blending operation is determined by the chosen method.
     *
     * @param method The blending method to be set.
     */
    public final void setBlendMethod(int method) {
        ThorvgResult.fromCode(ThorvgNative.paintSetBlendMethod(requireHandle(), method))
            .throwIfError("tvg_paint_set_blend_method");
    }

    /**
     * Safely releases a Paint object.
     */
    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.paintRel(handle)).throwIfError("tvg_paint_rel");
        clearHandle();
    }
}
