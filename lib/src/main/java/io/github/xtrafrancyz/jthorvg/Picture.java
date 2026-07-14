package io.github.xtrafrancyz.jthorvg;

import java.nio.Buffer;
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

    public void loadRaw(int[] data, int w, int h, ThorvgColorspace colorspace, boolean copy) {
        Objects.requireNonNull(data, "data");
        ThorvgResult.fromCode(ThorvgNative.pictureLoadRaw(requireHandle(), data, w, h, colorspace.code(), copy))
            .throwIfError("tvg_picture_load_raw");
    }

    public void loadRawBuffer(Buffer buffer, int w, int h, ThorvgColorspace colorspace, boolean copy) {
        Objects.requireNonNull(buffer, "buffer");
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("buffer must be a direct buffer");
        }
        ThorvgResult.fromCode(ThorvgNative.pictureLoadRawBuffer(requireHandle(), buffer, w, h, colorspace.code(), copy))
            .throwIfError("tvg_picture_load_raw");
    }

    public void loadData(byte[] data, String mimetype, String rpath, boolean copy) {
        Objects.requireNonNull(data, "data");
        ThorvgResult.fromCode(ThorvgNative.pictureLoadData(requireHandle(), data, data.length, mimetype, rpath, copy))
            .throwIfError("tvg_picture_load_data");
    }

    public void setAssetResolver(PictureAssetResolver resolver) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetAssetResolver(requireHandle(), resolver))
            .throwIfError("tvg_picture_set_asset_resolver");
    }

    public void setSize(float width, float height) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetSize(requireHandle(), width, height))
            .throwIfError("tvg_picture_set_size");
    }

    public float[] getSize() {
        float[] out = new float[2];
        ThorvgResult.fromCode(ThorvgNative.pictureGetSize(requireHandle(), out))
            .throwIfError("tvg_picture_get_size");
        return out;
    }

    public void setOrigin(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetOrigin(requireHandle(), x, y))
            .throwIfError("tvg_picture_set_origin");
    }

    public float[] getOrigin() {
        float[] out = new float[2];
        ThorvgResult.fromCode(ThorvgNative.pictureGetOrigin(requireHandle(), out))
            .throwIfError("tvg_picture_get_origin");
        return out;
    }

    public long getPaint(int id) {
        return ThorvgNative.pictureGetPaint(requireHandle(), id);
    }

    public void setFilter(int method) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetFilter(requireHandle(), method))
            .throwIfError("tvg_picture_set_filter");
    }

    public void setAccessible(boolean accessible) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetAccessible(requireHandle(), accessible))
            .throwIfError("tvg_picture_set_accessible");
    }
}
