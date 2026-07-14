package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

/**
 * A module for exporting a paint object into a specified file.
 * <p>
 * The module enables to save the composed scene and/or image from a paint object.
 * Once it's successfully exported to a file, it can be recreated using the Picture module.
 */
public final class Saver extends NativeHandle {
    Saver(long handle) {
        super(handle);
    }

    /**
     * Exports the given paint data to the given path.
     * <p>
     * If the saver module supports any compression mechanism, it will optimize the data size.
     * This might affect the encoding/decoding time in some cases. You can turn off the compression
     * if you wish to optimize for speed.
     *
     * @param paint   The paint to be saved with all its associated properties.
     * @param path    A path to the file, in which the paint data is to be saved.
     * @param quality The encoded quality level. 0 is the minimum, 100 is the maximum value(recommended).
     * @note Saving can be asynchronous if the assigned thread number is greater than zero. To guarantee the saving is done, call sync() afterwards.
     */
    public void savePaint(Paint paint, String path, int quality) {
        Objects.requireNonNull(paint, "paint");
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.saverSavePaint(requireHandle(), paint.requireHandle(), path, quality))
            .throwIfError("tvg_saver_save_paint");
    }

    /**
     * Exports the given animation data to the given path.
     * <p>
     * If the saver module supports any compression mechanism, it will optimize the data size.
     * This might affect the encoding/decoding time in some cases. You can turn off the compression
     * if you wish to optimize for speed.
     *
     * @param animation The animation to be saved with all its associated properties.
     * @param path      A path to the file, in which the animation data is to be saved.
     * @param quality   The encoded quality level. 0 is the minimum, 100 is the maximum value(recommended).
     * @param fps       The frames per second for the animation. If 0, the default fps is used.
     * @note A higher frames per second (FPS) would result in a larger file size. It is recommended to use the default value.
     * @note Saving can be asynchronous if the assigned thread number is greater than zero. To guarantee the saving is done, call sync() afterwards.
     */
    public void saveAnimation(Animation animation, String path, int quality, int fps) {
        Objects.requireNonNull(animation, "animation");
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.saverSaveAnimation(requireHandle(), animation.requireHandle(), path, quality, fps))
            .throwIfError("tvg_saver_save_animation");
    }

    /**
     * Guarantees that the saving task is finished.
     * <p>
     * The behavior of the Saver module works on a sync/async basis, depending on the threading setting of the Initializer.
     * Thus, if you wish to have a benefit of it, you must call sync() after the savePaint() in the proper delayed time.
     * Otherwise, you can call sync() immediately.
     */
    public void sync() {
        ThorvgResult.fromCode(ThorvgNative.saverSync(requireHandle()))
            .throwIfError("tvg_saver_sync");
    }

    /**
     * Deletes the given Saver object.
     */
    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.saverDel(handle)).throwIfError("tvg_saver_del");
        clearHandle();
    }
}
