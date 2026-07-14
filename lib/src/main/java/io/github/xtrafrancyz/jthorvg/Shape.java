package io.github.xtrafrancyz.jthorvg;

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

    public void reset() {
        ThorvgResult.fromCode(ThorvgNative.shapeReset(requireHandle()))
            .throwIfError("tvg_shape_reset");
    }

    public void moveTo(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.shapeMoveTo(requireHandle(), x, y))
            .throwIfError("tvg_shape_move_to");
    }

    public void lineTo(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.shapeLineTo(requireHandle(), x, y))
            .throwIfError("tvg_shape_line_to");
    }

    public void cubicTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.shapeCubicTo(requireHandle(), cx1, cy1, cx2, cy2, x, y))
            .throwIfError("tvg_shape_cubic_to");
    }

    public void closePath() {
        ThorvgResult.fromCode(ThorvgNative.shapeClose(requireHandle()))
            .throwIfError("tvg_shape_close");
    }

    public void appendRect(float x, float y, float width, float height, float rx, float ry, boolean clockwise) {
        ThorvgResult.fromCode(ThorvgNative.shapeAppendRect(requireHandle(), x, y, width, height, rx, ry, clockwise))
            .throwIfError("tvg_shape_append_rect");
    }

    public void appendCircle(float cx, float cy, float rx, float ry, boolean clockwise) {
        ThorvgResult.fromCode(ThorvgNative.shapeAppendCircle(requireHandle(), cx, cy, rx, ry, clockwise))
            .throwIfError("tvg_shape_append_circle");
    }

    public void appendPath(byte[] cmds, float[] pts) {
        if (cmds == null || pts == null) {
            throw new IllegalArgumentException("cmds and pts must be non-null");
        }
        ThorvgResult.fromCode(ThorvgNative.shapeAppendPath(requireHandle(), cmds, cmds.length, pts, pts.length / 2))
            .throwIfError("tvg_shape_append_path");
    }

    public PathData getPath() {
        byte[][] outCmds = new byte[1][];
        float[][] outPts = new float[1][];
        ThorvgResult.fromCode(ThorvgNative.shapeGetPath(requireHandle(), outCmds, outPts))
            .throwIfError("tvg_shape_get_path");
        return new PathData(outCmds[0], outPts[0]);
    }

    public void setStrokeWidth(float width) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeWidth(requireHandle(), width))
            .throwIfError("tvg_shape_set_stroke_width");
    }

    public float getStrokeWidth() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeWidth(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_width");
        return out[0];
    }

    public void setStrokeColor(int red, int green, int blue, int alpha) {
        validateColorComponent("red", red);
        validateColorComponent("green", green);
        validateColorComponent("blue", blue);
        validateColorComponent("alpha", alpha);
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeColor(requireHandle(), red, green, blue, alpha))
            .throwIfError("tvg_shape_set_stroke_color");
    }

    public int[] getStrokeColor() {
        int[] out = new int[4];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeColor(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_color");
        return out;
    }

    public void setStrokeGradient(Gradient gradient) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeGradient(requireHandle(), gradient.requireHandle()))
            .throwIfError("tvg_shape_set_stroke_gradient");
    }

    public long getStrokeGradient() {
        long[] out = new long[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeGradient(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_gradient");
        return out[0];
    }

    public void setStrokeDash(float[] dashPattern, float offset) {
        int cnt = dashPattern != null ? dashPattern.length : 0;
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeDash(requireHandle(), dashPattern, cnt, offset))
            .throwIfError("tvg_shape_set_stroke_dash");
    }

    public StrokeDash getStrokeDash() {
        float[][] outDash = new float[1][];
        float[] outOffset = new float[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeDash(requireHandle(), outDash, outOffset))
            .throwIfError("tvg_shape_get_stroke_dash");
        return new StrokeDash(outDash[0], outOffset[0]);
    }

    public void setStrokeCap(int cap) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeCap(requireHandle(), cap))
            .throwIfError("tvg_shape_set_stroke_cap");
    }

    public int getStrokeCap() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeCap(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_cap");
        return out[0];
    }

    public void setStrokeJoin(int join) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeJoin(requireHandle(), join))
            .throwIfError("tvg_shape_set_stroke_join");
    }

    public int getStrokeJoin() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeJoin(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_join");
        return out[0];
    }

    public void setStrokeMiterlimit(float miterlimit) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetStrokeMiterlimit(requireHandle(), miterlimit))
            .throwIfError("tvg_shape_set_stroke_miterlimit");
    }

    public float getStrokeMiterlimit() {
        float[] out = new float[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetStrokeMiterlimit(requireHandle(), out))
            .throwIfError("tvg_shape_get_stroke_miterlimit");
        return out[0];
    }

    public void setTrimpath(float begin, float end, boolean simultaneous) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetTrimpath(requireHandle(), begin, end, simultaneous))
            .throwIfError("tvg_shape_set_trimpath");
    }

    public void setFillColor(int red, int green, int blue, int alpha) {
        validateColorComponent("red", red);
        validateColorComponent("green", green);
        validateColorComponent("blue", blue);
        validateColorComponent("alpha", alpha);
        ThorvgResult.fromCode(ThorvgNative.shapeSetFillColor(requireHandle(), red, green, blue, alpha))
            .throwIfError("tvg_shape_set_fill_color");
    }

    public int[] getFillColor() {
        int[] out = new int[4];
        ThorvgResult.fromCode(ThorvgNative.shapeGetFillColor(requireHandle(), out))
            .throwIfError("tvg_shape_get_fill_color");
        return out;
    }

    public void setFillRule(int rule) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetFillRule(requireHandle(), rule))
            .throwIfError("tvg_shape_set_fill_rule");
    }

    public int getFillRule() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.shapeGetFillRule(requireHandle(), out))
            .throwIfError("tvg_shape_get_fill_rule");
        return out[0];
    }

    public void setPaintOrder(boolean strokeFirst) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetPaintOrder(requireHandle(), strokeFirst))
            .throwIfError("tvg_shape_set_paint_order");
    }

    public void setGradient(Gradient gradient) {
        ThorvgResult.fromCode(ThorvgNative.shapeSetGradient(requireHandle(), gradient.requireHandle()))
            .throwIfError("tvg_shape_set_gradient");
    }

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
