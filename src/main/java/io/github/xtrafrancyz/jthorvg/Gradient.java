package io.github.xtrafrancyz.jthorvg;

/**
 * A structure representing a gradient fill of a Paint object.
 * <p>
 * The module enables to set and to get the gradient colors and their arrangement inside the gradient bounds,
 * to specify the gradient bounds and the gradient behavior in case the area defined by the gradient bounds
 * is smaller than the area to be filled.
 */
public abstract class Gradient extends NativeHandle {
    Gradient(long handle) {
        super(handle);
    }

    public static final class ColorStops {
        public final float[] offsets;
        public final int[] colors;

        public ColorStops(float[] offsets, int[] colors) {
            this.offsets = offsets;
            this.colors = colors;
        }
    }

    /**
     * Sets the parameters of the colors of the gradient and their position.
     *
     * @param offsets The relative position of the color stops in the range [0.0 ~ 1.0].
     * @param colors  The 32-bit colors representing the color stops.
     */
    public final void setColorStops(float[] offsets, int[] colors) {
        if (offsets == null || colors == null || offsets.length != colors.length) {
            throw new IllegalArgumentException("offsets and colors arrays must be non-null and have the same length");
        }
        ThorvgResult.fromCode(ThorvgNative.gradientSetColorStops(requireHandle(), offsets, colors, offsets.length))
            .throwIfError("tvg_gradient_set_color_stops");
    }

    /**
     * Gets the parameters of the colors of the gradient, their position and number.
     *
     * @return ColorStops containing the offsets and color arrays.
     */
    public final ColorStops getColorStops() {
        float[][] outOffsets = new float[1][];
        int[][] outColors = new int[1][];
        ThorvgResult.fromCode(ThorvgNative.gradientGetColorStops(requireHandle(), outOffsets, outColors))
            .throwIfError("tvg_gradient_get_color_stops");
        return new ColorStops(outOffsets[0], outColors[0]);
    }

    /**
     * Sets how to fill the area outside the gradient bounds.
     *
     * @param spread The FillSpread value.
     */
    public final void setSpread(int spread) {
        ThorvgResult.fromCode(ThorvgNative.gradientSetSpread(requireHandle(), spread))
            .throwIfError("tvg_gradient_set_spread");
    }

    /**
     * Gets the FillSpread value of the gradient object.
     *
     * @return The FillSpread value.
     */
    public final int getSpread() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.gradientGetSpread(requireHandle(), out))
            .throwIfError("tvg_gradient_get_spread");
        return out[0];
    }

    /**
     * Sets the matrix of the affine transformation for the gradient object.
     * <p>
     * The augmented matrix of the transformation is expected to be given.
     *
     * @param matrix The 3x3 augmented matrix.
     */
    public final void setTransform(float[] matrix) {
        if (matrix != null && matrix.length != 9) {
            throw new IllegalArgumentException("matrix must be an array of length 9");
        }
        ThorvgResult.fromCode(ThorvgNative.gradientSetTransform(requireHandle(), matrix))
            .throwIfError("tvg_gradient_set_transform");
    }

    /**
     * Gets the matrix of the affine transformation of the gradient object.
     * <p>
     * In case no transformation was applied, the identity matrix is set.
     *
     * @return The 3x3 augmented matrix.
     */
    public final float[] getTransform() {
        float[] matrixOut = new float[9];
        ThorvgResult.fromCode(ThorvgNative.gradientGetTransform(requireHandle(), matrixOut))
            .throwIfError("tvg_gradient_get_transform");
        return matrixOut;
    }

    /**
     * Gets the unique value of the gradient instance indicating the instance type.
     *
     * @return The unique type of the gradient instance type.
     */
    public final int getType() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.gradientGetType(requireHandle(), out))
            .throwIfError("tvg_gradient_get_type");
        return out[0];
    }

    /**
     * Duplicates the given Gradient object.
     * <p>
     * Creates a new object and sets its all properties as in the original object.
     *
     * @return A copied Gradient object handle.
     */
    public abstract Gradient duplicate();

    /**
     * Deletes the given gradient object.
     */
    @Override
    public void close() {
        if (isClosed())
            return;
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.gradientDel(handle)).throwIfError("tvg_gradient_del");
        clearHandle();
    }
}
