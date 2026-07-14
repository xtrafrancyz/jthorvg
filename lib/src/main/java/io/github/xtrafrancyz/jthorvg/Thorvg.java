package io.github.xtrafrancyz.jthorvg;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class Thorvg {
    private Thorvg() {
    }

    public static void load() {
        NativeLibraryLoader.load();
    }

    public static void load(Path libraryPath) {
        NativeLibraryLoader.load(libraryPath);
    }

    public static boolean isLoaded() {
        return NativeLibraryLoader.isLoaded();
    }

    public static String nativeLibraryName() {
        return NativeLibraryLoader.baseLibraryName();
    }

    public static void init(int threads) {
        ensureLoaded();
        if (threads < 0) {
            throw new IllegalArgumentException("threads must be 0 or greater");
        }
        ThorvgResult.fromCode(ThorvgNative.engineInit(threads)).throwIfError("tvg_engine_init");
    }

    public static void term() {
        ensureLoaded();
        ThorvgResult.fromCode(ThorvgNative.engineTerm()).throwIfError("tvg_engine_term");
    }

    public static EngineVersion version() {
        ensureLoaded();
        return ThorvgNative.engineVersion();
    }

    public static SoftwareCanvas newSoftwareCanvas() {
        return newSoftwareCanvas(EnumSet.of(ThorvgEngineOption.DEFAULT));
    }

    public static SoftwareCanvas newSoftwareCanvas(Set<ThorvgEngineOption> options) {
        ensureLoaded();
        Objects.requireNonNull(options, "options");
        long handle = ThorvgNative.swCanvasNew(ThorvgEngineOption.toNativeMask(options));
        if (handle == 0) {
            throw new IllegalStateException("tvg_swcanvas_create returned a null canvas handle");
        }
        return new SoftwareCanvas(handle);
    }

    public static Shape newShape() {
        ensureLoaded();
        long handle = ThorvgNative.shapeNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_shape_new returned a null paint handle");
        }
        return new Shape(handle);
    }

    public static Picture newPicture() {
        ensureLoaded();
        long handle = ThorvgNative.pictureNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_picture_new returned a null paint handle");
        }
        return new Picture(handle);
    }

    private static void ensureLoaded() {
        if (!NativeLibraryLoader.isLoaded()) {
            throw new IllegalStateException(
                "ThorVG JNI library is not loaded. Call Thorvg.load() or Thorvg.load(Path) first."
            );
        }
    }
}
