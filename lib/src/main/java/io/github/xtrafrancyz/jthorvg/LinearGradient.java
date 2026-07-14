package io.github.xtrafrancyz.jthorvg;

/**
 * A structure representing a linear gradient fill of a Paint object.
 */
public final class LinearGradient extends Gradient {
    LinearGradient(long handle) {
        super(handle);
    }

    /**
     * Sets the linear gradient bounds.
     * <p>
     * The bounds of the linear gradient are defined as a surface constrained by two parallel lines crossing
     * the given points (x1, y1) and (x2, y2), respectively. Both lines are perpendicular to the line linking
     * (x1, y1) and (x2, y2).
     *
     * @param x1 The horizontal coordinate of the first point used to determine the gradient bounds.
     * @param y1 The vertical coordinate of the first point used to determine the gradient bounds.
     * @param x2 The horizontal coordinate of the second point used to determine the gradient bounds.
     * @param y2 The vertical coordinate of the second point used to determine the gradient bounds.
     * @note In case the first and the second points are equal, an object is filled with a single color using the last color specified in the setColorStops().
     */
    public void set(float x1, float y1, float x2, float y2) {
        ThorvgResult.fromCode(ThorvgNative.linearGradientSet(requireHandle(), x1, y1, x2, y2))
            .throwIfError("tvg_linear_gradient_set");
    }

    /**
     * Gets the linear gradient bounds.
     * <p>
     * The bounds of the linear gradient are defined as a surface constrained by two parallel lines crossing
     * the given points (x1, y1) and (x2, y2), respectively. Both lines are perpendicular to the line linking
     * (x1, y1) and (x2, y2).
     *
     * @return A float array containing [x1, y1, x2, y2] representing the gradient bounds.
     */
    public float[] get() {
        float[] out = new float[4];
        ThorvgResult.fromCode(ThorvgNative.linearGradientGet(requireHandle(), out))
            .throwIfError("tvg_linear_gradient_get");
        return out;
    }
}
