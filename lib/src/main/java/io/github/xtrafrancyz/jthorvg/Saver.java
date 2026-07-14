package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

public final class Saver extends NativeHandle {
    Saver(long handle) {
        super(handle);
    }

    public void savePaint(Paint paint, String path, int quality) {
        Objects.requireNonNull(paint, "paint");
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.saverSavePaint(requireHandle(), paint.requireHandle(), path, quality))
            .throwIfError("tvg_saver_save_paint");
    }

    public void saveAnimation(Animation animation, String path, int quality, int fps) {
        Objects.requireNonNull(animation, "animation");
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.saverSaveAnimation(requireHandle(), animation.requireHandle(), path, quality, fps))
            .throwIfError("tvg_saver_save_animation");
    }

    public void sync() {
        ThorvgResult.fromCode(ThorvgNative.saverSync(requireHandle()))
            .throwIfError("tvg_saver_sync");
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.saverDel(handle)).throwIfError("tvg_saver_del");
        clearHandle();
    }
}
