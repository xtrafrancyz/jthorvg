package io.github.xtrafrancyz.jthorvg;

public abstract class Paint extends NativeHandle {
    Paint(long handle) {
        super(handle);
    }

    public final void translate(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.paintTranslate(requireHandle(), x, y))
            .throwIfError("tvg_paint_translate");
    }

    public final void scale(float factor) {
        ThorvgResult.fromCode(ThorvgNative.paintScale(requireHandle(), factor))
            .throwIfError("tvg_paint_scale");
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.paintRel(handle)).throwIfError("tvg_paint_rel");
        clearHandle();
    }
}
