package io.github.xtrafrancyz.jthorvg;

public final class Scene extends Paint {
    Scene(long handle) {
        super(handle);
    }

    public void add(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.sceneAdd(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_scene_add");
    }

    public void insert(Paint target, Paint at) {
        ThorvgResult.fromCode(ThorvgNative.sceneInsert(requireHandle(), target.requireHandle(), at.requireHandle()))
            .throwIfError("tvg_scene_insert");
    }

    public void remove(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.sceneRemove(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_scene_remove");
    }

    public void clearEffects() {
        ThorvgResult.fromCode(ThorvgNative.sceneClearEffects(requireHandle()))
            .throwIfError("tvg_scene_clear_effects");
    }

    public void addEffectGaussianBlur(double sigma, int direction, int border, int quality) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectGaussianBlur(requireHandle(), sigma, direction, border, quality))
            .throwIfError("tvg_scene_add_effect_gaussian_blur");
    }

    public void addEffectDropShadow(int r, int g, int b, int a, double angle, double distance, double sigma, int quality) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectDropShadow(requireHandle(), r, g, b, a, angle, distance, sigma, quality))
            .throwIfError("tvg_scene_add_effect_drop_shadow");
    }

    public void addEffectFill(int r, int g, int b, int a) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectFill(requireHandle(), r, g, b, a))
            .throwIfError("tvg_scene_add_effect_fill");
    }

    public void addEffectTint(int black_r, int black_g, int black_b, int white_r, int white_g, int white_b, double intensity) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectTint(requireHandle(), black_r, black_g, black_b, white_r, white_g, white_b, intensity))
            .throwIfError("tvg_scene_add_effect_tint");
    }

    public void addEffectTritone(int shadow_r, int shadow_g, int shadow_b, int midtone_r, int midtone_g, int midtone_b, int highlight_r, int highlight_g, int highlight_b, int blend) {
        ThorvgResult.fromCode(ThorvgNative.sceneAddEffectTritone(requireHandle(), shadow_r, shadow_g, shadow_b, midtone_r, midtone_g, midtone_b, highlight_r, highlight_g, highlight_b, blend))
            .throwIfError("tvg_scene_add_effect_tritone");
    }
}
