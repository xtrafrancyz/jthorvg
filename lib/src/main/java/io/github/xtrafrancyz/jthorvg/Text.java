package io.github.xtrafrancyz.jthorvg;

public final class Text extends Paint {
    Text(long handle) {
        super(handle);
    }

    public void setFont(String name) {
        ThorvgResult.fromCode(ThorvgNative.textSetFont(requireHandle(), name))
            .throwIfError("tvg_text_set_font");
    }

    public void setSize(float size) {
        if (size <= 0.0f) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        ThorvgResult.fromCode(ThorvgNative.textSetSize(requireHandle(), size))
            .throwIfError("tvg_text_set_size");
    }

    public void setText(String utf8) {
        ThorvgResult.fromCode(ThorvgNative.textSetText(requireHandle(), utf8))
            .throwIfError("tvg_text_set_text");
    }

    public String getText() {
        return ThorvgNative.textGetText(requireHandle());
    }

    public void align(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.textAlign(requireHandle(), x, y))
            .throwIfError("tvg_text_align");
    }

    public void layout(float w, float h) {
        ThorvgResult.fromCode(ThorvgNative.textLayout(requireHandle(), w, h))
            .throwIfError("tvg_text_layout");
    }

    public void setWrapMode(int mode) {
        ThorvgResult.fromCode(ThorvgNative.textWrapMode(requireHandle(), mode))
            .throwIfError("tvg_text_wrap_mode");
    }

    public int getLineCount() {
        return ThorvgNative.textLineCount(requireHandle());
    }

    public void setSpacing(float letter, float line) {
        ThorvgResult.fromCode(ThorvgNative.textSpacing(requireHandle(), letter, line))
            .throwIfError("tvg_text_spacing");
    }

    public void setItalic(float shear) {
        ThorvgResult.fromCode(ThorvgNative.textSetItalic(requireHandle(), shear))
            .throwIfError("tvg_text_set_italic");
    }

    public void setOutline(float width, int r, int g, int b) {
        ThorvgResult.fromCode(ThorvgNative.textSetOutline(requireHandle(), width, r, g, b))
            .throwIfError("tvg_text_set_outline");
    }

    public void setColor(int r, int g, int b) {
        ThorvgResult.fromCode(ThorvgNative.textSetColor(requireHandle(), r, g, b))
            .throwIfError("tvg_text_set_color");
    }

    public void setGradient(Gradient gradient) {
        ThorvgResult.fromCode(ThorvgNative.textSetGradient(requireHandle(), gradient.requireHandle()))
            .throwIfError("tvg_text_set_gradient");
    }

    public float[] getTextMetrics() {
        float[] out = new float[4];
        ThorvgResult.fromCode(ThorvgNative.textGetTextMetrics(requireHandle(), out))
            .throwIfError("tvg_text_get_text_metrics");
        return out;
    }

    public float[] getGlyphMetrics(String ch, int[] nextIndexOut) {
        float[] out = new float[6];
        ThorvgResult.fromCode(ThorvgNative.textGetGlyphMetrics(requireHandle(), ch, out, nextIndexOut))
            .throwIfError("tvg_text_get_glyph_metrics");
        return out;
    }
}
