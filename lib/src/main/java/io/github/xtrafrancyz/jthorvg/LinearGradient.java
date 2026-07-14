package io.github.xtrafrancyz.jthorvg;

public final class LinearGradient extends Gradient {
    LinearGradient(long handle) {
        super(handle);
    }

    public void set(float x1, float y1, float x2, float y2) {
        ThorvgResult.fromCode(ThorvgNative.linearGradientSet(requireHandle(), x1, y1, x2, y2))
            .throwIfError("tvg_linear_gradient_set");
    }

    public float[] get() {
        float[] out = new float[4];
        ThorvgResult.fromCode(ThorvgNative.linearGradientGet(requireHandle(), out))
            .throwIfError("tvg_linear_gradient_get");
        return out;
    }
}
