package io.github.xtrafrancyz.jthorvg;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Main entry point for the ThorVG library JNI bindings.
 */
public final class Thorvg {
    private Thorvg() {
    }

    /**
     * Loads the native ThorVG library from default system paths or bundled resources.
     */
    public static void load() {
        NativeLibraryLoader.load();
    }

    /**
     * Loads the native ThorVG library from the specified path.
     *
     * @param libraryPath Path to the native library file.
     */
    public static void load(Path libraryPath) {
        NativeLibraryLoader.load(libraryPath);
    }

    /**
     * Checks if the native library is loaded.
     *
     * @return true if the library is loaded, false otherwise.
     */
    public static boolean isLoaded() {
        return NativeLibraryLoader.isLoaded();
    }

    /**
     * Returns the name of the native library.
     *
     * @return the name of the native library.
     */
    public static String nativeLibraryName() {
        return NativeLibraryLoader.baseLibraryName();
    }

    /**
     * Initializes the ThorVG engine.
     * <p>
     * ThorVG requires an active runtime environment to operate.
     * Internally, it utilizes a task scheduler to efficiently parallelize rendering operations.
     * You can specify the number of worker threads using the threads parameter.
     * During initialization, ThorVG will spawn the specified number of threads.
     * <p>
     * <b>Note:</b> The initializer uses internal reference counting to track multiple calls.
     * The number of threads is fixed on the first call to init() and cannot be changed in subsequent calls.
     *
     * @param threads The number of worker threads to create. A value of zero indicates that only the main thread will be used.
     */
    public static void init(int threads) {
        ensureLoaded();
        if (threads < 0) {
            throw new IllegalArgumentException("threads must be 0 or greater");
        }
        ThorvgResult.fromCode(ThorvgNative.engineInit(threads)).throwIfError("tvg_engine_init");
    }

    /**
     * Terminates the ThorVG engine.
     * <p>
     * Cleans up resources and stops any internal threads initialized by init().
     *
     * <b>Note:</b> The initializer maintains a reference count for safe repeated use. Only the final call to term() will fully shut down the engine.
     */
    public static void term() {
        ensureLoaded();
        ThorvgResult.fromCode(ThorvgNative.engineTerm()).throwIfError("tvg_engine_term");
    }

    /**
     * Retrieves the version of the TVG engine.
     *
     * @return EngineVersion object containing major, minor, micro version numbers and the full version string.
     */
    public static EngineVersion version() {
        ensureLoaded();
        return ThorvgNative.engineVersion();
    }

    /**
     * Creates a new Software Canvas object with default rendering engine settings.
     *
     * @return A new SoftwareCanvas object.
     */
    public static SoftwareCanvas newSoftwareCanvas() {
        return newSoftwareCanvas(EnumSet.of(ThorvgEngineOption.DEFAULT));
    }

    /**
     * Creates a new Software Canvas object with optional rendering engine settings.
     * <p>
     * This method generates a software canvas instance that can be used for drawing vector graphics.
     * It accepts an optional parameter options to choose between different rendering engine behaviors.
     *
     * @param options The rendering engine options.
     * @return A new SoftwareCanvas object.
     */
    public static SoftwareCanvas newSoftwareCanvas(Set<ThorvgEngineOption> options) {
        ensureLoaded();
        Objects.requireNonNull(options, "options");
        long handle = ThorvgNative.swCanvasNew(ThorvgEngineOption.toNativeMask(options));
        if (handle == 0) {
            throw new IllegalStateException("tvg_swcanvas_create returned a null canvas handle");
        }
        return new SoftwareCanvas(handle);
    }

    /**
     * Creates a new OpenGL/ES Canvas object with default rendering engine settings.
     *
     * @return A new GLCanvas object.
     */
    public static GLCanvas newGLCanvas() {
        return newGLCanvas(EnumSet.of(ThorvgEngineOption.DEFAULT));
    }

    /**
     * Creates a new OpenGL/ES Canvas object with optional rendering engine settings.
     * <p>
     * This method generates an OpenGL/ES canvas instance that can be used for drawing vector graphics.
     * It accepts an optional parameter options to choose between different rendering engine behaviors.
     *
     * @param options The rendering engine options.
     * @return A new GLCanvas object.
     * <b>Note:</b> Currently, it does not support {@link ThorvgEngineOption#SMART_RENDER}. The request will be ignored.
     */
    public static GLCanvas newGLCanvas(Set<ThorvgEngineOption> options) {
        ensureLoaded();
        Objects.requireNonNull(options, "options");
        long handle = ThorvgNative.glCanvasCreate(ThorvgEngineOption.toNativeMask(options));
        if (handle == 0) {
            throw new IllegalStateException("tvg_glcanvas_create returned a null canvas handle");
        }
        return new GLCanvas(handle);
    }

    /**
     * Creates a new WebGPU Canvas object with default rendering engine settings.
     *
     * @return A new WGCanvas object.
     */
    public static WGCanvas newWGCanvas() {
        return newWGCanvas(EnumSet.of(ThorvgEngineOption.DEFAULT));
    }

    /**
     * Creates a new WebGPU Canvas object with optional rendering engine settings.
     * <p>
     * This method generates a WebGPU canvas instance that can be used for drawing vector graphics.
     * It accepts an optional parameter options to choose between different rendering engine behaviors.
     *
     * @param options The rendering engine options.
     * @return A new WGCanvas object.
     * <b>Note:</b> Currently, it does not support {@link ThorvgEngineOption#SMART_RENDER}. The request will be ignored.
     */
    public static WGCanvas newWGCanvas(Set<ThorvgEngineOption> options) {
        ensureLoaded();
        Objects.requireNonNull(options, "options");
        long handle = ThorvgNative.wgCanvasCreate(ThorvgEngineOption.toNativeMask(options));
        if (handle == 0) {
            throw new IllegalStateException("tvg_wgcanvas_create returned a null canvas handle");
        }
        return new WGCanvas(handle);
    }

    /**
     * Creates a new Shape object.
     * <p>
     * This function allocates and returns a new Shape instance.
     * To properly destroy the Shape object, use {@link Shape#close()}.
     *
     * @return A new Shape object.
     */
    public static Shape newShape() {
        ensureLoaded();
        long handle = ThorvgNative.shapeNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_shape_new returned a null paint handle");
        }
        return new Shape(handle);
    }

    /**
     * Creates a new linear gradient object.
     *
     * @return A new LinearGradient object.
     */
    public static LinearGradient newLinearGradient() {
        ensureLoaded();
        long handle = ThorvgNative.linearGradientNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_linear_gradient_new returned a null gradient handle");
        }
        return new LinearGradient(handle);
    }

    /**
     * Creates a new radial gradient object.
     *
     * @return A new RadialGradient object.
     */
    public static RadialGradient newRadialGradient() {
        ensureLoaded();
        long handle = ThorvgNative.radialGradientNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_radial_gradient_new returned a null gradient handle");
        }
        return new RadialGradient(handle);
    }

    /**
     * Creates a new Picture object.
     * <p>
     * This function allocates and returns a new Picture instance.
     * To properly destroy the Picture object, use {@link Picture#close()}.
     *
     * @return A new Picture object.
     */
    public static Picture newPicture() {
        ensureLoaded();
        long handle = ThorvgNative.pictureNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_picture_new returned a null paint handle");
        }
        return new Picture(handle);
    }

    /**
     * Creates a new Scene object.
     * <p>
     * This function allocates and returns a new Scene instance.
     * To properly destroy the Scene object, use {@link Scene#close()}.
     *
     * @return A new Scene object.
     */
    public static Scene newScene() {
        ensureLoaded();
        long handle = ThorvgNative.sceneNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_scene_new returned a null scene handle");
        }
        return new Scene(handle);
    }

    /**
     * Creates a new Text object.
     * <p>
     * This function allocates and returns a new Text instance.
     * To properly destroy the Text object, use {@link Text#close()}.
     *
     * @return A new Text object.
     */
    public static Text newText() {
        ensureLoaded();
        long handle = ThorvgNative.textNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_text_new returned a null text handle");
        }
        return new Text(handle);
    }

    /**
     * Creates a new Saver object.
     *
     * @return A new Saver object.
     */
    public static Saver newSaver() {
        ensureLoaded();
        long handle = ThorvgNative.saverNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_saver_new returned a null saver handle");
        }
        return new Saver(handle);
    }

    /**
     * Creates a new Accessor object.
     *
     * @return A new Accessor object.
     */
    public static Accessor newAccessor() {
        ensureLoaded();
        long handle = ThorvgNative.accessorNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_accessor_new returned a null accessor handle");
        }
        return new Accessor(handle);
    }

    /**
     * Creates a new Animation object.
     *
     * @return A new Animation object.
     */
    public static Animation newAnimation() {
        ensureLoaded();
        long handle = ThorvgNative.animationNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_animation_new returned a null animation handle");
        }
        return new Animation(handle);
    }

    /**
     * Creates a new LottieAnimation object.
     *
     * @return A new LottieAnimation object.
     */
    public static LottieAnimation newLottieAnimation() {
        ensureLoaded();
        long handle = ThorvgNative.lottieAnimationNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_lottie_animation_new returned a null animation handle");
        }
        return new LottieAnimation(handle);
    }

    private static void ensureLoaded() {
        if (!NativeLibraryLoader.isLoaded()) {
            throw new IllegalStateException(
                "ThorVG JNI library is not loaded. Call Thorvg.load() or Thorvg.load(Path) first."
            );
        }
    }
}
