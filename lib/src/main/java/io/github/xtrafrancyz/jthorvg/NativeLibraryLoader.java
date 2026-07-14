package io.github.xtrafrancyz.jthorvg;

import java.nio.file.Path;
import java.util.Objects;

final class NativeLibraryLoader {
    private static final String BASE_LIBRARY_NAME = "jthorvg_jni";

    private static volatile boolean loaded;

    private NativeLibraryLoader() {
    }

    static synchronized void load() {
        if (!loaded) {
            System.loadLibrary(BASE_LIBRARY_NAME);
            loaded = true;
        }
    }

    static synchronized void load(Path libraryPath) {
        Objects.requireNonNull(libraryPath, "libraryPath");
        if (!loaded) {
            System.load(libraryPath.toAbsolutePath().toString());
            loaded = true;
        }
    }

    static boolean isLoaded() {
        return loaded;
    }

    static String baseLibraryName() {
        return BASE_LIBRARY_NAME;
    }
}
