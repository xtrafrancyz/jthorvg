package io.github.xtrafrancyz.jthorvg;

/**
 * A structure representing a radial gradient fill of a Paint object.
 */
public final class RadialGradient extends Gradient {
    RadialGradient(long handle) {
        super(handle);
    }

    /**
     * Sets the radial gradient attributes.
     * <p>
     * The radial gradient is defined by the end circle with a center (cx, cy) and a radius r and
     * the start circle with a center/focal point (fx, fy) and a radius fr.
     * The gradient will be rendered such that the gradient stop at an offset of 100% aligns with the edge of the end circle
     * and the stop at an offset of 0% aligns with the edge of the start circle.
     * <p>
     * <b>Note:</b> In case the radius r is zero, an object is filled with a single color using the last color specified in the setColorStops().
     * <p>
     * <b>Note:</b> In case the focal point (fx and fy) lies outside the end circle, it is projected onto the edge of the end circle.
     * <p>
     * <b>Note:</b> If the start circle doesn't fully fit inside the end circle (after possible repositioning), the fr is reduced accordingly.
     * <p>
     * <b>Note:</b> By manipulating the position and size of the focal point, a wide range of visual effects can be achieved, such as directing
     * the gradient focus towards a specific edge or enhancing the depth and complexity of shading patterns.
     * If a focal effect is not desired, simply align the focal point (fx and fy) with the center of the end circle (cx and cy)
     * and set the radius (fr) to zero. This will result in a uniform gradient without any focal variations.
     *
     * @param cx The horizontal coordinate of the center of the end circle.
     * @param cy The vertical coordinate of the center of the end circle.
     * @param r  The radius of the end circle.
     * @param fx The horizontal coordinate of the center of the start circle.
     * @param fy The vertical coordinate of the center of the start circle.
     * @param fr The radius of the start circle.
     */
    public void set(float cx, float cy, float r, float fx, float fy, float fr) {
        ThorvgResult.fromCode(ThorvgNative.radialGradientSet(requireHandle(), cx, cy, r, fx, fy, fr))
            .throwIfError("tvg_radial_gradient_set");
    }

    /**
     * Gets radial gradient attributes.
     *
     * @return A float array containing [cx, cy, r, fx, fy, fr] representing the gradient attributes.
     */
    public float[] get() {
        float[] out = new float[6];
        ThorvgResult.fromCode(ThorvgNative.radialGradientGet(requireHandle(), out))
            .throwIfError("tvg_radial_gradient_get");
        return out;
    }

    /**
     * Duplicates the given Gradient object.
     * <p>
     * Creates a new object and sets its all properties as in the original object.
     *
     * @return A copied Gradient object handle.
     */
    @Override
    public RadialGradient duplicate() {
        return new RadialGradient(ThorvgNative.gradientDuplicate(requireHandle()));
    }
}
