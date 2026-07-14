package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

public final class Font {
    private Font() {}

    public static void load(String path) {
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.fontLoad(path))
            .throwIfError("tvg_font_load");
    }

    public static void loadData(String name, byte[] data, String mimetype, boolean copy) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(data, "data");
        ThorvgResult.fromCode(ThorvgNative.fontLoadData(name, data, data.length, mimetype, copy))
            .throwIfError("tvg_font_load_data");
    }

    public static void unload(String path) {
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.fontUnload(path))
            .throwIfError("tvg_font_unload");
    }
}
