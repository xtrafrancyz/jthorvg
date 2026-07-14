package io.github.xtrafrancyz.jthorvg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Locale;

final class NativeLibraryLoader {
    private static final String BASE_LIBRARY_NAME = "jthorvg_jni";

    private static volatile boolean loaded;

    private NativeLibraryLoader() {
    }

    static synchronized void load() {
        if (!loaded) {
            UnsatisfiedLinkError systemLoadError = null;
            try {
                System.loadLibrary(BASE_LIBRARY_NAME);
                loaded = true;
                return;
            } catch (UnsatisfiedLinkError ex) {
                systemLoadError = ex;
            }

            Path extractedLibraryPath;
            try {
                extractedLibraryPath = extractBundledLibraryToTempDir();
            } catch (IOException | IllegalStateException ex) {
                UnsatisfiedLinkError loadError = new UnsatisfiedLinkError(
                    "Unable to load native library '" + BASE_LIBRARY_NAME + "' from system paths or bundled resources."
                );
                loadError.addSuppressed(systemLoadError);
                loadError.initCause(ex);
                throw loadError;
            }

            try {
                System.load(extractedLibraryPath.toAbsolutePath().toString());
                loaded = true;
            } catch (UnsatisfiedLinkError ex) {
                ex.addSuppressed(systemLoadError);
                throw ex;
            }
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

    private static Path extractBundledLibraryToTempDir() throws IOException {
        String libraryFileName = System.mapLibraryName(BASE_LIBRARY_NAME);
        String resourcePath = bundledLibraryResourcePath(libraryFileName);
        try (InputStream libraryStream = NativeLibraryLoader.class.getResourceAsStream(resourcePath)) {
            if (libraryStream == null) {
                throw new IllegalStateException("Bundled native library resource not found: " + resourcePath);
            }
            Path tempDirectory = Files.createTempDirectory("jthorvg-native-");
            Path extractedLibraryPath = tempDirectory.resolve(libraryFileName);
            Files.copy(libraryStream, extractedLibraryPath);
            extractedLibraryPath.toFile().deleteOnExit();
            tempDirectory.toFile().deleteOnExit();
            return extractedLibraryPath;
        }
    }

    private static String bundledLibraryResourcePath(String libraryFileName) {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String osFolder;
        if (osName.contains("windows")) {
            osFolder = "windows";
        } else if (osName.contains("linux")) {
            osFolder = "linux";
        } else {
            throw new IllegalStateException("Unsupported OS for bundled native library loading: " + osName);
        }
        return "/" + osFolder + "/" + libraryFileName;
    }
}
