package io.github.xtrafrancyz.jthorvg;

/**
 * A module managing the multiple paints as one group paint.
 * <p>
 * As a group, scene can be transformed, translucent, composited with other target paints,
 * its children will be affected by the scene world.
 */
public final class Scene extends Paint {
    Scene(long handle) {
        super(handle);
    }

    /**
     * Adds a paint object to the scene.
     * <p>
     * Appends the specified paint object to the given scene. Only paint objects
     * added to the scene are considered rendering targets.
     * <p>
     * <b>Note:</b> Ownership of the paint object is transferred to the canvas upon
     * successful addition. To retain ownership, call paint.ref()
     * before adding it to the scene.
     * <p>
     * <b>Note:</b> The rendering order of paint objects follows their order in the root
     * scene. If layering is required, ensure the paints are added in the
     * desired order.
     *
     * @param paint A handle to the paint object to be added to the scene.
     *              This parameter must not be null.
     */
    public void add(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.sceneAdd(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_scene_add");
    }

    /**
     * Inserts a paint object into the scene.
     * <p>
     * Inserts the specified paint object into the scene immediately before the
     * given paint object at. The at parameter must reference an existing
     * paint object already added to the scene.
     * <p>
     * <b>Note:</b> Ownership of the target object is transferred to the scene upon
     * successful addition. To retain ownership, call paint.ref()
     * before adding it to the scene.
     * <p>
     * <b>Note:</b> The rendering order of paint objects follows their order in the root
     * scene. If layering is required, ensure the paints are added in the
     * desired order.
     *
     * @param target A handle to the paint object to be inserted into the scene.
     *               This parameter must not be null.
     * @param at     A handle to an existing paint object in the scene before
     *               which target will be inserted.
     *               This parameter must not be null.
     */
    public void insert(Paint target, Paint at) {
        ThorvgResult.fromCode(ThorvgNative.sceneInsert(requireHandle(), target.requireHandle(), at.requireHandle()))
            .throwIfError("tvg_scene_insert");
    }

    /**
     * Removes a paint object from the scene.
     * <p>
     * This function removes a specified paint object from the scene. If no paint
     * object is specified (i.e., null is used), the function
     * performs to clear all paints from the scene.
     *
     * @param paint A pointer to the Paint object to be removed from the scene.
     *              If null, remove all the paints from the scene.
     */
    public void remove(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.sceneRemove(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_scene_remove");
    }

    /**
     * Clears all previously applied scene effects.
     * <p>
     * This function clears all effects that have been applied to the scene,
     * restoring it to its original state without any post-processing.
     */
    public void clearEffects() {
        ThorvgResult.fromCode(ThorvgNative.sceneClearEffects(requireHandle()))
            .throwIfError("tvg_scene_clear_effects");
    }

    /**
     * Adds a Gaussian blur effect to the scene.
     * <p>
     * This function adds a Gaussian blur filter to the scene as a post-processing effect.
     * The blur can be applied in different directions with configurable border handling and quality settings.
     *
     * @param sigma     The blur radius (sigma) value. Must be greater than 0.
     * @param direction Blur direction: 0 = both directions, 1 = horizontal only, 2 = vertical only.
     * @param border    Border handling method: 0 = duplicate, 1 = wrap.
     * @param quality   Blur quality level [0 - 100].
     */
    public void addEffectGaussianBlur(double sigma, int direction, int border, int quality) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectGaussianBlur(requireHandle(), sigma, direction, border, quality))
            .throwIfError("tvg_scene_add_effect_gaussian_blur");
    }

    /**
     * Adds a drop shadow effect to the scene.
     * <p>
     * This function adds a drop shadow with a Gaussian blur to the scene. The shadow
     * can be customized using color, opacity, angle, distance, blur radius (sigma),
     * and quality parameters.
     *
     * @param r        Red channel value of the shadow color [0 - 255].
     * @param g        Green channel value of the shadow color [0 - 255].
     * @param b        Blue channel value of the shadow color [0 - 255].
     * @param a        Alpha (opacity) channel value of the shadow [0 - 255].
     * @param angle    Shadow direction in degrees [0 - 360].
     * @param distance Distance of the shadow from the original object.
     * @param sigma    Gaussian blur sigma value for the shadow. Must be &gt; 0.
     * @param quality  Blur quality level [0 - 100].
     */
    public void addEffectDropShadow(int r, int g, int b, int a, double angle, double distance, double sigma, int quality) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectDropShadow(requireHandle(), r, g, b, a, angle, distance, sigma, quality))
            .throwIfError("tvg_scene_add_effect_drop_shadow");
    }

    /**
     * Adds a fill color effect to the scene.
     * <p>
     * This function overrides the scene's content colors with the specified fill color.
     *
     * @param r Red color channel value [0 - 255].
     * @param g Green color channel value [0 - 255].
     * @param b Blue color channel value [0 - 255].
     * @param a Alpha (opacity) channel value [0 - 255].
     */
    public void addEffectFill(int r, int g, int b, int a) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectFill(requireHandle(), r, g, b, a))
            .throwIfError("tvg_scene_add_effect_fill");
    }

    /**
     * Adds a tint effect to the scene.
     * <p>
     * This function tints the current scene using specified black and white color values,
     * modulated by a given intensity.
     *
     * @param black_r   Red component of the black color [0 - 255].
     * @param black_g   Green component of the black color [0 - 255].
     * @param black_b   Blue component of the black color [0 - 255].
     * @param white_r   Red component of the white color [0 - 255].
     * @param white_g   Green component of the white color [0 - 255].
     * @param white_b   Blue component of the white color [0 - 255].
     * @param intensity Tint intensity value [0 - 100].
     */
    public void addEffectTint(int black_r, int black_g, int black_b, int white_r, int white_g, int white_b, double intensity) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectTint(requireHandle(), black_r, black_g, black_b, white_r, white_g, white_b, intensity))
            .throwIfError("tvg_scene_add_effect_tint");
    }

    /**
     * Adds a tritone color effect to the scene.
     * <p>
     * This function adds a tritone color effect to the given scene using three sets of RGB values
     * representing shadow, midtone, and highlight colors.
     *
     * @param shadow_r    Red component of the shadow color [0 - 255].
     * @param shadow_g    Green component of the shadow color [0 - 255].
     * @param shadow_b    Blue component of the shadow color [0 - 255].
     * @param midtone_r   Red component of the midtone color [0 - 255].
     * @param midtone_g   Green component of the midtone color [0 - 255].
     * @param midtone_b   Blue component of the midtone color [0 - 255].
     * @param highlight_r Red component of the highlight color [0 - 255].
     * @param highlight_g Green component of the highlight color [0 - 255].
     * @param highlight_b Blue component of the highlight color [0 - 255].
     * @param blend       A blending factor that determines the mix between the original color and the tritone colors [0 - 255].
     */
    public void addEffectTritone(int shadow_r, int shadow_g, int shadow_b, int midtone_r, int midtone_g, int midtone_b, int highlight_r, int highlight_g, int highlight_b, int blend) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectTritone(requireHandle(), shadow_r, shadow_g, shadow_b, midtone_r, midtone_g, midtone_b, highlight_r, highlight_g, highlight_b, blend))
            .throwIfError("tvg_scene_add_effect_tritone");
    }
}
