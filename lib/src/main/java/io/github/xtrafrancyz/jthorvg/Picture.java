package io.github.xtrafrancyz.jthorvg;

import java.nio.file.Path;
import java.util.Objects;

public final class Picture extends Paint {
    Picture(long handle) {
        super(handle);
    }

    public void load(Path path) {
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.pictureLoad(requireHandle(), path.toAbsolutePath().toString()))
            .throwIfError("tvg_picture_load");
    }

    public void setSize(float width, float height) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetSize(requireHandle(), width, height))
            .throwIfError("tvg_picture_set_size");
    }
}
