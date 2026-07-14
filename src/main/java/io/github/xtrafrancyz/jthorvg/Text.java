package io.github.xtrafrancyz.jthorvg;

/**
 * A class to represent text objects in a graphical context, allowing for rendering and manipulation of unicode text.
 */
public final class Text extends Paint {
    Text(long handle) {
        super(handle);
    }

    /**
     * Sets the font family for the text.
     * <p>
     * This function specifies the name of the font to be used when rendering text.
     *
     * @param name The name of the font. This should match a font available through the canvas backend.
     *             If set to null, ThorVG will attempt to select a fallback font available on the engine.
     * <b>Note:</b> This function only sets the font family name. Use setSize() to define the font size.
     * <b>Note:</b> If the name is not specified, ThorVG will select an available fallback font.
     */
    public void setFont(String name) {
        ThorvgResult.fromCode(ThorvgNative.textSetFont(requireHandle(), name))
            .throwIfError("tvg_text_set_font");
    }

    /**
     * Sets the font size for the text.
     * <p>
     * This function sets the font size used during text rendering.
     * The size is specified in point units, and supports floating-point precision
     * for smooth scaling and animation effects.
     *
     * @param size The font size in points. Must be greater than 0.0.
     * <b>Note:</b> Use this function in combination with setFont() to fully define text appearance.
     * <b>Note:</b> Fractional sizes (e.g., 12.5) are supported for sub-pixel rendering and animations.
     */
    public void setSize(float size) {
        if (size <= 0.0f) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        ThorvgResult.fromCode(ThorvgNative.textSetSize(requireHandle(), size))
            .throwIfError("tvg_text_set_size");
    }

    /**
     * Assigns the given unicode text to be rendered.
     * <p>
     * This function sets the unicode text that will be displayed by the rendering system.
     * The text is set according to the specified UTF encoding method, which defaults to UTF-8.
     *
     * @param utf8 The multi-byte text encoded with utf8 string to be rendered.
     */
    public void setText(String utf8) {
        ThorvgResult.fromCode(ThorvgNative.textSetText(requireHandle(), utf8))
            .throwIfError("tvg_text_set_text");
    }

    /**
     * Returns the currently assigned unicode text.
     * <p>
     * This function retrieves the unicode string that is currently set
     * for rendering. The returned text is encoded in UTF-8.
     *
     * @return The UTF-8 encoded multi-byte text string.
     */
    public String getText() {
        return ThorvgNative.textGetText(requireHandle());
    }

    /**
     * Sets text alignment or anchor per axis.
     * <p>
     * If layout width/height is set on an axis, align within the layout box.
     * Otherwise, treat it as an anchor within the text bounds which point of
     * the text box is pinned to the paint position.
     *
     * @param x Horizontal alignment/anchor in [0..1]: 0=left/start, 0.5=center, 1=right/end. (Default is 0)
     * @param y Vertical alignment/anchor in [0..1]: 0=top, 0.5=middle, 1=bottom. (Default is 0)
     */
    public void align(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.textAlign(requireHandle(), x, y))
            .throwIfError("tvg_text_align");
    }

    /**
     * Sets the virtual layout box (constraints) for the text.
     * <p>
     * If width/height is set on an axis, that axis is constrained by a virtual layout box and
     * the text may wrap/align inside it. If width/height == 0, the axis is
     * unconstrained and align() acts as an anchor on that axis.
     *
     * @param w Layout width in user space. Use 0 for no horizontal constraint. (Default is 0)
     * @param h Layout height in user space. Use 0 for no vertical constraint. (Default is 0)
     * <b>Note:</b> This defines constraints only; alignment/anchoring is controlled by align().
     */
    public void layout(float w, float h) {
        ThorvgResult.fromCode(ThorvgNative.textLayout(requireHandle(), w, h))
            .throwIfError("tvg_text_layout");
    }

    /**
     * Sets the text wrapping mode for this text object.
     * <p>
     * This method controls how the text is laid out when it exceeds the available space.
     * The wrapping mode determines whether text is truncated, wrapped by character or word,
     * or adjusted automatically. An ellipsis mode is also available for truncation with "...".
     *
     * @param mode The wrapping strategy to apply.
     */
    public void setWrapMode(int mode) {
        ThorvgResult.fromCode(ThorvgNative.textWrapMode(requireHandle(), mode))
            .throwIfError("tvg_text_wrap_mode");
    }

    /**
     * Returns the number of text lines.
     * <p>
     * This function retrieves the number of lines generated after applying text layout and wrapping.
     *
     * @return The total number of lines.
     */
    public int getLineCount() {
        return ThorvgNative.textLineCount(requireHandle());
    }

    /**
     * Set the spacing scale factors for text layout.
     * <p>
     * This function adjusts the letter spacing (horizontal space between glyphs) and
     * line spacing (vertical space between lines of text) using scale factors.
     * <p>
     * Both values are relative to the font's default metrics:
     * - The letter spacing is applied as a scale factor to the glyph's advance width.
     * - The line spacing is applied as a scale factor to the glyph's advance height.
     *
     * @param letter The scale factor for letter spacing.
     *               Values &gt; 1.0 increase spacing, values &lt; 1.0 decrease it.
     *               Must be greater than or equal to 0.0. (default: 1.0)
     * @param line   The scale factor for line spacing.
     *               Values &gt; 1.0 increase line spacing, values &lt; 1.0 decrease it.
     *               Must be greater than or equal to 0.0. (default: 1.0)
     */
    public void setSpacing(float letter, float line) {
        ThorvgResult.fromCode(ThorvgNative.textSpacing(requireHandle(), letter, line))
            .throwIfError("tvg_text_spacing");
    }

    /**
     * Apply an italic (slant) transformation to the text.
     * <p>
     * This function applies a shear transformation to simulate an italic (oblique) style
     * for the current text object. The shear factor determines the degree of slant
     * applied along the X-axis.
     *
     * @param shear The shear factor to apply. A value of 0.0 applies no slant, while values around 0.5 result in a strong slant.
     *              Must be in the range [0.0, 0.5]. Recommended value is 0.18.
     * <b>Note:</b> The shear factor will be clamped to the valid range if it exceeds the limits.
     * <b>Note:</b> This does not require the font itself to be italic.
     *       It visually simulates the effect by applying a transformation matrix.
     * <b>Warning:</b> Excessive slanting may cause visual distortion depending on the font and size.
     */
    public void setItalic(float shear) {
        ThorvgResult.fromCode(ThorvgNative.textSetItalic(requireHandle(), shear))
            .throwIfError("tvg_text_set_italic");
    }

    /**
     * Sets an outline (stroke) around the text object.
     * <p>
     * This function adds an outline to the text with the specified width and RGB color.
     * The outline enhances the visibility of the text by rendering a stroke around its glyphs.
     *
     * @param width The width of the outline. Must be positive value. (The default is 0)
     * @param r     Red component of the outline color (0–255).
     * @param g     Green component of the outline color (0–255).
     * @param b     Blue component of the outline color (0–255).
     * <b>Note:</b> To disable the outline, set width to 0.
     */
    public void setOutline(float width, int r, int g, int b) {
        ThorvgResult.fromCode(ThorvgNative.textSetOutline(requireHandle(), width, r, g, b))
            .throwIfError("tvg_text_set_outline");
    }

    /**
     * Sets the text solid color.
     *
     * @param r The red color channel value in the range [0 ~ 255]. The default value is 0.
     * @param g The green color channel value in the range [0 ~ 255]. The default value is 0.
     * @param b The blue color channel value in the range [0 ~ 255]. The default value is 0.
     * <b>Note:</b> Either a solid color or a gradient fill is applied, depending on what was set as last.
     */
    public void setColor(int r, int g, int b) {
        ThorvgResult.fromCode(ThorvgNative.textSetColor(requireHandle(), r, g, b))
            .throwIfError("tvg_text_set_color");
    }

    /**
     * Sets the gradient fill for the text.
     *
     * @param gradient The linear or radial gradient fill.
     * <b>Note:</b> Either a solid color or a gradient fill is applied, depending on what was set as last.
     */
    public void setGradient(Gradient gradient) {
        ThorvgResult.fromCode(ThorvgNative.textSetGradient(requireHandle(), gradient.requireHandle()))
            .throwIfError("tvg_text_set_gradient");
    }

    /**
     * Retrieves the layout metrics of the text object.
     * <p>
     * Fills the provided array with the font layout values of this text object,
     * such as ascent, descent, linegap, and line advance.
     * <p>
     * The returned values reflect the font size applied to the text object,
     * but do not include any transformations (e.g., scale, rotation, or translation).
     *
     * @return A float array containing [ascent, descent, linegap, advance] of the text metrics.
     */
    public float[] getTextMetrics() {
        float[] out = new float[4];
        ThorvgResult.fromCode(ThorvgNative.textGetTextMetrics(requireHandle(), out))
            .throwIfError("tvg_text_get_text_metrics");
        return out;
    }

    /**
     * Retrieves the layout metrics of a glyph in the text object.
     * <p>
     * Fills the provided array with the horizontal layout values
     * of the specified glyph, such as advance, left-side bearing, and bounding box.
     * <p>
     * The returned values reflect the font size applied to the text object,
     * but do not include any transformations (e.g., scale, rotation, or translation).
     * <p>
     * The input character must be a single UTF-8 encoded character.
     *
     * @param ch           A string containing the character.
     * @param nextIndexOut An optional array that receives the index immediately following the processed character.
     * @return A float array of size 6 containing the glyph metrics: [advance, bearing, minX, minY, maxX, maxY].
     */
    public float[] getGlyphMetrics(String ch, int[] nextIndexOut) {
        float[] out = new float[6];
        ThorvgResult.fromCode(ThorvgNative.textGetGlyphMetrics(requireHandle(), ch, out, nextIndexOut))
            .throwIfError("tvg_text_get_glyph_metrics");
        return out;
    }
}
