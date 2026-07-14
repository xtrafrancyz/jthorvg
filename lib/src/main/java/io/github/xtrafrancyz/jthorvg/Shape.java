package io.github.xtrafrancyz.jthorvg;

public final class Shape extends Paint {
    Shape(long handle) {
        super(handle);
    }

    public void appendRect(float x, float y, float width, float height, float rx, float ry, boolean clockwise) {
        ThorvgResult.fromCode(ThorvgNative.shapeAppendRect(requireHandle(), x, y, width, height, rx, ry, clockwise))
            .throwIfError("tvg_shape_append_rect");
    }

    public void setFillColor(int red, int green, int blue, int alpha) {
        validateColorComponent("red", red);
        validateColorComponent("green", green);
        validateColorComponent("blue", blue);
        validateColorComponent("alpha", alpha);
        ThorvgResult.fromCode(ThorvgNative.shapeSetFillColor(requireHandle(), red, green, blue, alpha))
            .throwIfError("tvg_shape_set_fill_color");
    }

    private static void validateColorComponent(String name, int value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(name + " must be between 0 and 255: " + value);
        }
    }
}
