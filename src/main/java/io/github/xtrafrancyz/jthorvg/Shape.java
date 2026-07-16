package io.github.xtrafrancyz.jthorvg;

/**
 * A class for managing two-dimensional figures and their properties.
 * <p>
 * A shape has three major properties: shape outline, stroking, filling. The outline in the shape is retained as the path.
 * Path can be composed by accumulating primitive commands such as moveTo(), lineTo(), cubicTo() or complete shape interfaces such as appendRect(), appendCircle(), etc.
 * Path can consist of sub-paths. One sub-path is determined by a close command.
 * <p>
 * The stroke of a shape is an optional property in case the shape needs to be represented with/without the outline borders.
 * It's efficient since the shape path and the stroking path can be shared with each other. It's also convenient when controlling both in one context.
 */
public final class Shape extends Paint {
    Shape(long handle) {
        super(handle);
    }

    public static final class PathData {
        public final byte[] cmds;
        public final float[] pts;

        public PathData(byte[] cmds, float[] pts) {
            this.cmds = cmds;
            this.pts = pts;
        }
    }

    public static final class StrokeDash {
        public final float[] pattern;
        public final float offset;

        public StrokeDash(float[] pattern, float offset) {
            this.pattern = pattern;
            this.offset = offset;
        }
    }

    /**
     * Resets the shape path properties.
     * <p>
     * The color, the fill and the stroke properties are retained.
     * <p>
     * Note: The memory, where the path data is stored, is not deallocated at this stage for caching effect.
     */
    public void reset() {
        ThorvgResult.fromCode(ThorvgNative.shapeReset(requireHandle()))
            .throwIfError("tvg_shape_reset");
    }

    /**
     * Sets the initial point of the sub-path.
     * <p>
     * The value of the current point is set to the given point.
     *
     * @param x The horizontal coordinate of the initial point of the sub-path.
     * @param y The vertical coordinate of the initial point of the sub-path.
     */
    public void moveTo(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.shapeMoveTo(requireHandle(), x, y))
            .throwIfError("tvg_shape_move_to");
    }

    /**
     * Adds a new point to the sub-path, which results in drawing a line from the current point to the given end-point.
     * <p>
     * The value of the current point is set to the given end-point.
     * <p>
     * <b>Note:</b> In case this is the first command in the path, it corresponds to the moveTo() call.
     *
     * @param x The horizontal coordinate of the end-point of the line.
     * @param y The vertical coordinate of the end-point of the line.
     */
    public void lineTo(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.shapeLineTo(requireHandle(), x, y))
            .throwIfError("tvg_shape_line_to");
    }

    /**
     * Adds new points to the sub-path, which results in drawing a cubic Bezier curve.
     * <p>
     * The Bezier curve starts at the current point and ends at the given end-point (x, y). Two control points (cx1, cy1) and (cx2, cy2) are used to determine the shape of the curve.
     * The value of the current point is set to the given end-point.
     * <p>
     * <b>Note:</b> In case this is the first command in the path, no data from the path are rendered.
     *
     * @param cx1 The horizontal coordinate of the 1st control point.
     * @param cy1 The vertical coordinate of the 1st control point.
     * @param cx2 The horizontal coordinate of the 2nd control point.
     * @param cy2 The vertical coordinate of the 2nd control point.
     * @param x   The horizontal coordinate of the endpoint of the curve.
     * @param y   The vertical coordinate of the endpoint of the curve.
     */
    public void cubicTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.shapeCubicTo(requireHandle(), cx1, cy1, cx2, cy2, x, y))
            .throwIfError("tvg_shape_cubic_to");
    }

    /**
     * Closes the current sub-path by drawing a line from the current point to the initial point of the sub-path.
     * <p>
     * The value of the current point is set to the initial point of the closed sub-path.
     * <p>
     * Note: In case the sub-path does not contain any points, this function has no effect.
     */
    public void closePath() {
        ThorvgResult.fromCode(ThorvgNative.shapeClose(requireHandle()))
            .throwIfError("tvg_shape_close");
    }

    /**
     * Appends a rectangle to the path.
     * <p>
     * The rectangle with rounded corners can be achieved by setting non-zero values to rx and ry arguments.
     * The rx and ry values specify the radii of the ellipse defining the rounding of the corners.
     * <p>
     * The position of the rectangle is specified by the coordinates of its upper-left corner - x and y arguments.
     * <p>
     * The rectangle is treated as a new sub-path - it is not connected with the previous sub-path.
     * <p>
     * The value of the current point is set to (x + rx, y) - in case rx is greater
     * than w/2 the current point is set to (x + w/2, y).
     * <p>
     * <b>Note:</b> For rx and ry greater than or equal to the half of w and the half of h, respectively, the shape become an ellipse.
     *
     * @param x         The horizontal coordinate of the upper-left corner of the rectangle.
     * @param y         The vertical coordinate of the upper-left corner of the rectangle.
     * @param width     The width of the rectangle.
     * @param height    The height of the rectangle.
     * @param rx        The x-axis radius of the ellipse defining the rounded corners of the rectangle.
     * @param ry        The y-axis radius of the ellipse defining the rounded corners of the rectangle.
     * @param clockwise Specifies the path direction: true for clockwise, false for counterclockwise.
     */
    public void appendRect(float x, float y, float width, float height, float rx, float ry, boolean clockwise) {
        ThorvgResult.fromCode(ThorvgNative.shapeAppendRect(requireHandle(), x, y, width, height, rx, ry, clockwise))
            .throwIfError("tvg_shape_append_rect");
    }

    /**
     * Appends an ellipse to the path.
     * <p>
     * The position of the ellipse is specified by the coordinates of its center - cx and cy arguments.
     * <p>
     * The ellipse is treated as a new sub-path - it is not connected with the previous sub-path.
     * <p>
     * The value of the current point is set to (cx, cy - ry).
     *
     * @param cx        The horizontal coordinate of the center of the ellipse.
     * @param cy        The vertical coordinate of the center of the ellipse.
     * @param rx        The x-axis radius of the ellipse.
     * @param ry        The y-axis radius of the ellipse.
     * @param clockwise Specifies the path direction: true for clockwise, false for counterclockwise.
     */
    public void appendCircle(float cx, float cy, float rx, float ry, boolean clockwise) {
        ThorvgResult.fromCode(ThorvgNative.shapeAppendCircle(requireHandle(), cx, cy, rx, ry, clockwise))
            .throwIfError("tvg_shape_append_circle");
    }

    /**
     * Appends a given sub-path to the path.
     * <p>
     * The current point value is set to the last point from the sub-path.
     * For each command from the cmds array, an appropriate number of points in pts array should be specified.
     * If the number of points in the pts array is different than the number required by the cmds array, the shape with this sub-path will not be displayed on the screen.
     *
     * @param cmds The array of the commands in the sub-path.
     * @param pts  The array of the two-dimensional points.
     */
    public void appendPath(byte[] cmds, float[] pts) {
        if (cmds == null || pts == null) {
            throw new IllegalArgumentException("cmds and pts must be non-null");
        }
        ThorvgResult.fromCode(ThorvgNative.shapeAppendPath(requireHandle(), cmds, cmds.length, pts, pts.length / 2))
            .throwIfError("tvg_shape_append_path");
    }

    /**
     * Retrieves the current path data of the shape.
     * <p>
     * This function provides access to the shape's path data, including the commands
     * and points that define the path.
     *
     * @return PathData containing the commands and points.
     */
    public PathData getPath() {
        byte[][] outCmds = new byte[1][];
        float[][] outPts = new float[1][];
        ThorvgResult.fromCode(ThorvgNative.shapeGetPath(requireHandle(), outCmds, outPts))
            .throwIfError("tvg_shape_get_path");
        return new PathData(outCmds[0], outPts[0]);
    }

    /**
     * Sets the stroke width for the path.
     * <p>
     * This function defines the thickness of the stroke applied to all figures
     * in the path object. A stroke is the outline drawn along the edges of the
     * path's geometry.
     * <p>
     * <b>Note:</b> A value of width 0 disables the stroke.
     *
     * @param width The width of the stroke in pixels. Must be positive value. (The default is 0)
     */
    public void setStrokeWidth(float width) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeWidth(requireHandle(), width))
            .throwIfError("tvg_shape_set_stroke_width");
    }

    /**
     * Gets the shape's stroke width.
     *
     * @return The stroke width.
     */
    public float getStrokeWidth() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeWidth(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_width");
        return out[0];
    }

    /**
     * Sets the shape's stroke color.
     * <p>
     * <b>Note:</b> If the stroke width is 0 (default), the stroke will not be visible regardless of the color.
     * <p>
     * <b>Note:</b> Either a solid color or a gradient fill is applied, depending on what was set as last.
     *
     * @param red   The red color channel value in the range [0 ~ 255]. The default value is 0.
     * @param green The green color channel value in the range [0 ~ 255]. The default value is 0.
     * @param blue  The blue color channel value in the range [0 ~ 255]. The default value is 0.
     * @param alpha The alpha channel value in the range [0 ~ 255], where 0 is completely transparent and 255 is opaque.
     */
    public void setStrokeColor(int red, int green, int blue, int alpha) {
        validateColorComponent("red", red);
        validateColorComponent("green", green);
        validateColorComponent("blue", blue);
        validateColorComponent("alpha", alpha);
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeColor(requireHandle(), red, green, blue, alpha))
            .throwIfError("tvg_shape_set_stroke_color");
    }

    /**
     * Gets the shape's stroke color.
     *
     * @return An array of [red, green, blue, alpha] values.
     */
    public int[] getStrokeColor() {
        int[] out = new int[4];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeColor(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_color");
        return out;
    }

    /**
     * Sets the gradient fill of the stroke for all of the figures from the path.
     * <p>
     * <b>Note:</b> Either a solid color or a gradient fill is applied, depending on what was set as last.
     *
     * @param gradient The gradient fill.
     */
    public void setStrokeGradient(Gradient gradient) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeGradient(requireHandle(), gradient.requireHandle()))
            .throwIfError("tvg_shape_set_stroke_gradient");
    }

    /**
     * Gets the gradient fill of the shape's stroke.
     *
     * @return The handle of the gradient fill.
     */
    public long getStrokeGradient() {
        long[] out = new long[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeGradient(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_gradient");
        return out[0];
    }

    /**
     * Sets the shape's stroke dash pattern.
     * <p>
     * <b>Note:</b> To reset the stroke dash pattern, pass null to dashPattern.
     * <p>
     * <b>Note:</b> Values of dashPattern less than zero are treated as zero.
     * <p>
     * <b>Note:</b> If all values in the dashPattern are equal to or less than 0, the dash is ignored.
     * <p>
     * <b>Note:</b> If the dashPattern contains an odd number of elements, the sequence is repeated in the same
     * order to form an even-length pattern, preserving the alternation of dashes and gaps.
     *
     * @param dashPattern An array of alternating dash and gap lengths.
     * @param offset      The shift of the starting point within the repeating dash pattern, from which the pattern begins to be applied.
     */
    public void setStrokeDash(float[] dashPattern, float offset) {
        int cnt = dashPattern != null ? dashPattern.length : 0;
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeDash(requireHandle(), dashPattern, cnt, offset))
            .throwIfError("tvg_shape_set_stroke_dash");
    }

    /**
     * Gets the dash pattern of the stroke.
     *
     * @return StrokeDash containing the dash pattern array and offset.
     */
    public StrokeDash getStrokeDash() {
        float[][] outDash = new float[1][];
        float[] outOffset = new float[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeDash(requireHandle(), outDash, outOffset))
            .throwIfError("tvg_shape_get_stroke_dash");
        return new StrokeDash(outDash[0], outOffset[0]);
    }

    /**
     * Sets the cap style used for stroking the path.
     * <p>
     * The cap style specifies the shape to be used at the end of the open stroked sub-paths.
     *
     * @param cap The cap style value. The default value is TVG_STROKE_CAP_SQUARE.
     */
    public void setStrokeCap(int cap) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeCap(requireHandle(), cap))
            .throwIfError("tvg_shape_set_stroke_cap");
    }

    /**
     * Gets the stroke cap style used for stroking the path.
     *
     * @return The cap style value.
     */
    public int getStrokeCap() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeCap(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_cap");
        return out[0];
    }

    /**
     * Sets the join style for stroked path segments.
     *
     * @param join The join style value. The default value is TVG_STROKE_JOIN_BEVEL.
     */
    public void setStrokeJoin(int join) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeJoin(requireHandle(), join))
            .throwIfError("tvg_shape_set_stroke_join");
    }

    /**
     * Gets the stroke join method.
     *
     * @return The join style value.
     */
    public int getStrokeJoin() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeJoin(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_join");
        return out[0];
    }

    /**
     * Sets the stroke miterlimit.
     *
     * @param miterlimit The miterlimit imposes a limit on the extent of the stroke join when the TVG_STROKE_JOIN_MITER join style is set. The default value is 4.
     */
    public void setStrokeMiterlimit(float miterlimit) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeMiterlimit(requireHandle(), miterlimit))
            .throwIfError("tvg_shape_set_stroke_miterlimit");
    }

    /**
     * Gets the stroke miterlimit.
     *
     * @return The stroke miterlimit.
     */
    public float getStrokeMiterlimit() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeMiterlimit(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_miterlimit");
        return out[0];
    }

    /**
     * Sets the trim of the shape along the defined path segment, allowing control over which part of the shape is visible.
     * <p>
     * If the values of the arguments begin and end exceed the 0-1 range, they are wrapped around in a manner similar to angle wrapping, effectively treating the range as circular.
     *
     * @param begin        Specifies the start of the segment to display along the path.
     * @param end          Specifies the end of the segment to display along the path.
     * @param simultaneous Determines how to trim multiple paths within a single shape. If set to true (default), trimming is applied simultaneously to all paths;
     *                     Otherwise, all paths are treated as a single entity with a combined length equal to the sum of their individual lengths and are trimmed as such.
     */
    public void setTrimpath(float begin, float end, boolean simultaneous) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetTrimpath(requireHandle(), begin, end, simultaneous))
            .throwIfError("tvg_shape_set_trimpath");
    }

    /**
     * Sets the shape's solid color.
     * <p>
     * The parts of the shape defined as inner are colored.
     * <p>
     * <b>Note:</b> Either a solid color or a gradient fill is applied, depending on what was set as last.
     *
     * @param red   The red color channel value in the range [0 ~ 255]. The default value is 0.
     * @param green The green color channel value in the range [0 ~ 255]. The default value is 0.
     * @param blue  The blue color channel value in the range [0 ~ 255]. The default value is 0.
     * @param alpha The alpha channel value in the range [0 ~ 255], where 0 is completely transparent and 255 is opaque. The default value is 0.
     */
    public void setFillColor(int red, int green, int blue, int alpha) {
        validateColorComponent("red", red);
        validateColorComponent("green", green);
        validateColorComponent("blue", blue);
        validateColorComponent("alpha", alpha);
        ThorvgResult.fromCode(ThorvgNative.shapeSetFillColor(requireHandle(), red, green, blue, alpha))
            .throwIfError("tvg_shape_set_fill_color");
    }

    /**
     * Gets the shape's solid color.
     *
     * @return An array of [red, green, blue, alpha] values.
     */
    public int[] getFillColor() {
        int[] out = new int[4];
        ThorvgResult.fromCode(ThorvgNative.shapeGetFillColor(requireHandle(), out))
            .throwIfError("tvg_shape_get_fill_color");
        return out;
    }

    /**
     * Sets the fill rule for the shape.
     * <p>
     * Specifies how the interior of the shape is determined when its path intersects itself.
     * The default fill rule is TVG_FILL_RULE_NON_ZERO.
     *
     * @param rule The fill rule to apply to the shape.
     */
    public void setFillRule(int rule) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetFillRule(requireHandle(), rule))
            .throwIfError("tvg_shape_set_fill_rule");
    }

    /**
     * Retrieves the current fill rule used by the shape.
     * <p>
     * This function returns the fill rule, which determines how the interior
     * regions of the shape are calculated when it overlaps itself.
     *
     * @return The current fill rule value of the shape.
     */
    public int getFillRule() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetFillRule(requireHandle(), out))
            .throwIfError("tvg_shape_get_fill_rule");
        return out[0];
    }

    /**
     * Sets the rendering order of the stroke and the fill.
     *
     * @param strokeFirst If true the stroke is rendered before the fill, otherwise the stroke is rendered as the second one (the default option).
     */
    public void setPaintOrder(boolean strokeFirst) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetPaintOrder(requireHandle(), strokeFirst))
            .throwIfError("tvg_shape_set_paint_order");
    }

    /**
     * Sets the gradient fill for all of the figures from the path.
     * <p>
     * The parts of the shape defined as inner are filled.
     * <p>
     * <b>Note:</b> Either a solid color or a gradient fill is applied, depending on what was set as last.
     *
     * @param gradient The gradient fill.
     */
    public void setGradient(Gradient gradient) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetGradient(requireHandle(), gradient.requireHandle()))
            .throwIfError("tvg_shape_set_gradient");
    }

    /**
     * Gets the gradient fill of the shape.
     *
     * @return The handle of the gradient fill.
     */
    public long getGradient() {
        long[] out = new long[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetGradient(requireHandle(), out))
            .throwIfError("tvg_shape_get_gradient");
        return out[0];
    }

    private static void validateColorComponent(String name, int value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(name + " must be between 0 and 255: " + value);
        }
    }
}
