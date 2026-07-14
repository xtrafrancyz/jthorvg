package io.github.xtrafrancyz.jthorvg;

public final class RadialGradient extends Gradient {
    RadialGradient(long handle) {
        super(handle);
    }

    public void set(float cx, float cy, float r, float fx, float fy, float fr) {
        ThorvgResult.fromCode(ThorvgNative.radialGradientSet(requireHandle(), cx, cy, r, fx, fy, fr))
            .throwIfError("tvg_radial_gradient_set");
    }

    public float[] get() {
        float[] out = new float[6];
        ThorvgResult.fromCode(ThorvgNative.radialGradientGet(requireHandle(), out))
            .throwIfError("tvg_radial_gradient_get");
        return out;
    }
}
