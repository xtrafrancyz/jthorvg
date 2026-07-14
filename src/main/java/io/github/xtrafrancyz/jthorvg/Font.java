package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

/**
 * A module enabling loading and unloading of scalable font data.
 */
public final class Font {
    private Font() {}

    /**
     * Loads a scalable font data from a file.
     * <p>
     * ThorVG efficiently caches the loaded data using the specified path as a key.
     * This means that loading the same file again will not result in duplicate operations;
     * instead, ThorVG will reuse the previously loaded font data.
     *
     * @param path The path to the font file.
     */
    public static void load(String path) {
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.fontLoad(path))
            .throwIfError("tvg_font_load");
    }

    /**
     * Loads a scalable font data from a memory block of a given size.
     * <p>
     * ThorVG efficiently caches the loaded font data using the specified name as a key.
     * This means that loading the same fonts again will not result in duplicate operations.
     * Instead, ThorVG will reuse the previously loaded font data.
     *
     * @param name     The name under which the font will be stored and accessible (e.x. in a setFont API).
     * @param data     The font data bytes.
     * @param mimetype Mimetype or extension of font data. In case a null or an empty "" value is provided the loader will be determined automatically.
     * @param copy     If true the data are copied into the engine local buffer, otherwise they are not (default).
     */
    public static void loadData(String name, byte[] data, String mimetype, boolean copy) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(data, "data");
        ThorvgResult.fromCode(ThorvgNative.fontLoadData(name, data, data.length, mimetype, copy))
            .throwIfError("tvg_font_load_data");
    }

    /**
     * Unloads the specified scalable font data that was previously loaded.
     * <p>
     * This function is used to release resources associated with a font file that has been loaded into memory.
     *
     * @param path The path to the loaded font file.
     */
    public static void unload(String path) {
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.fontUnload(path))
            .throwIfError("tvg_font_unload");
    }
}
