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

    public static GLCanvas newGLCanvas() {
        return newGLCanvas(EnumSet.of(ThorvgEngineOption.DEFAULT));
    }

    public static GLCanvas newGLCanvas(Set<ThorvgEngineOption> options) {
        ensureLoaded();
        Objects.requireNonNull(options, "options");
        long handle = ThorvgNative.glCanvasCreate(ThorvgEngineOption.toNativeMask(options));
        if (handle == 0) {
            throw new IllegalStateException("tvg_glcanvas_create returned a null canvas handle");
        }
        return new GLCanvas(handle);
    }

    public static WGCanvas newWGCanvas() {
        return newWGCanvas(EnumSet.of(ThorvgEngineOption.DEFAULT));
    }

    public static WGCanvas newWGCanvas(Set<ThorvgEngineOption> options) {
        ensureLoaded();
        Objects.requireNonNull(options, "options");
        long handle = ThorvgNative.wgCanvasCreate(ThorvgEngineOption.toNativeMask(options));
        if (handle == 0) {
            throw new IllegalStateException("tvg_wgcanvas_create returned a null canvas handle");
        }
        return new WGCanvas(handle);
    }

    public static Shape newShape() {
        ensureLoaded();
        long handle = ThorvgNative.shapeNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_shape_new returned a null paint handle");
        }
        return new Shape(handle);
    }

    public static LinearGradient newLinearGradient() {
        ensureLoaded();
        long handle = ThorvgNative.linearGradientNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_linear_gradient_new returned a null gradient handle");
        }
        return new LinearGradient(handle);
    }

    public static RadialGradient newRadialGradient() {
        ensureLoaded();
        long handle = ThorvgNative.radialGradientNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_radial_gradient_new returned a null gradient handle");
        }
        return new RadialGradient(handle);
    }

    public static Picture newPicture() {
        ensureLoaded();
        long handle = ThorvgNative.pictureNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_picture_new returned a null paint handle");
        }
        return new Picture(handle);
    }

    public static Scene newScene() {
        ensureLoaded();
        long handle = ThorvgNative.sceneNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_scene_new returned a null scene handle");
        }
        return new Scene(handle);
    }

    public static Text newText() {
        ensureLoaded();
        long handle = ThorvgNative.textNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_text_new returned a null text handle");
        }
        return new Text(handle);
    }

    public static Saver newSaver() {
        ensureLoaded();
        long handle = ThorvgNative.saverNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_saver_new returned a null saver handle");
        }
        return new Saver(handle);
    }

    public static Accessor newAccessor() {
        ensureLoaded();
        long handle = ThorvgNative.accessorNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_accessor_new returned a null accessor handle");
        }
        return new Accessor(handle);
    }

    public static Animation newAnimation() {
        ensureLoaded();
        long handle = ThorvgNative.animationNew();
        if (handle == 0) {
            throw new IllegalStateException("tvg_animation_new returned a null animation handle");
        }
        return new Animation(handle);
    }

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
