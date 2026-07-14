package io.github.xtrafrancyz.jthorvg;

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

    public final void setColorStops(float[] offsets, int[] colors) {
        if (offsets == null || colors == null || offsets.length != colors.length) {
            throw new IllegalArgumentException("offsets and colors arrays must be non-null and have the same length");
        }
        ThorvgResult.fromCode(ThorvgNative.gradientSetColorStops(requireHandle(), offsets, colors, offsets.length))
            .throwIfError("tvg_gradient_set_color_stops");
    }

    public final ColorStops getColorStops() {
        float[][] outOffsets = new float[1][];
        int[][] outColors = new int[1][];
        ThorvgResult.fromCode(ThorvgNative.gradientGetColorStops(requireHandle(), outOffsets, outColors))
            .throwIfError("tvg_gradient_get_color_stops");
        return new ColorStops(outOffsets[0], outColors[0]);
    }

    public final void setSpread(int spread) {
        ThorvgResult.fromCode(ThorvgNative.gradientSetSpread(requireHandle(), spread))
            .throwIfError("tvg_gradient_set_spread");
    }

    public final int getSpread() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.gradientGetSpread(requireHandle(), out))
            .throwIfError("tvg_gradient_get_spread");
        return out[0];
    }

    public final void setTransform(float[] matrix) {
        if (matrix != null && matrix.length != 9) {
            throw new IllegalArgumentException("matrix must be an array of length 9");
        }
        ThorvgResult.fromCode(ThorvgNative.gradientSetTransform(requireHandle(), matrix))
            .throwIfError("tvg_gradient_set_transform");
    }

    public final float[] getTransform() {
        float[] matrixOut = new float[9];
        ThorvgResult.fromCode(ThorvgNative.gradientGetTransform(requireHandle(), matrixOut))
            .throwIfError("tvg_gradient_get_transform");
        return matrixOut;
    }

    public final int getType() {
        int[] out = new int[1];
        ThorvgResult.fromCode(ThorvgNative.gradientGetType(requireHandle(), out))
            .throwIfError("tvg_gradient_get_type");
        return out[0];
    }

    public final long duplicate() {
        return ThorvgNative.gradientDuplicate(requireHandle());
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.gradientDel(handle)).throwIfError("tvg_gradient_del");
        clearHandle();
    }
}
