package io.github.xtrafrancyz.jthorvg;

import java.nio.Buffer;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A module enabling to create and to load an image in one of the supported formats: svg, png, jpg, lottie and raw.
 */
public final class Picture extends Paint {
    private boolean freeOnClose = true;

    Picture(long handle) {
        super(handle);
    }

    Picture(long handle, boolean freeOnClose) {
        super(handle);
        this.freeOnClose = freeOnClose;
    }

    /**
     * Loads a picture data directly from a file.
     * <p>
     * ThorVG efficiently caches the loaded data using the specified path as a key.
     * This means that loading the same file again will not result in duplicate operations;
     * instead, ThorVG will reuse the previously loaded picture data.
     *
     * @param path The absolute path to the image file.
     */
    public void load(Path path) {
        Objects.requireNonNull(path, "path");
        ThorvgResult.fromCode(ThorvgNative.pictureLoad(requireHandle(), path.toAbsolutePath().toString()))
            .throwIfError("tvg_picture_load");
    }

    /**
     * Loads raw image data in a specific format from a memory block of the given size.
     * <p>
     * ThorVG efficiently caches the loaded data, using the provided data address as a key
     * when copy is set to false. This allows ThorVG to avoid redundant operations
     * by reusing the previously loaded picture data for the same sharable data,
     * rather than duplicating the load process.
     *
     * @param data       A pointer to the memory block where the raw image data is stored.
     * @param w          The width of the image in pixels.
     * @param h          The height of the image in pixels.
     * @param colorspace Specifies how the 32-bit color values should be interpreted (read/write).
     * @param copy       If true, the data is copied into the engine's local buffer. If false, the data is not copied.
     */
    public void loadRaw(int[] data, int w, int h, ThorvgColorspace colorspace, boolean copy) {
        Objects.requireNonNull(data, "data");
        ThorvgResult.fromCode(ThorvgNative.pictureLoadRaw(requireHandle(), data, w, h, colorspace.code(), copy))
            .throwIfError("tvg_picture_load_raw");
    }

    /**
     * Loads raw image data from a direct buffer.
     *
     * @param buffer     A direct buffer containing the raw image data.
     * @param w          The width of the image.
     * @param h          The height of the image.
     * @param colorspace Specifies how the 32-bit color values should be interpreted.
     * @param copy       If true, the data is copied into the engine's local buffer.
     */
    public void loadRawBuffer(Buffer buffer, int w, int h, ThorvgColorspace colorspace, boolean copy) {
        Objects.requireNonNull(buffer, "buffer");
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("buffer must be a direct buffer");
        }
        ThorvgResult.fromCode(ThorvgNative.pictureLoadRawBuffer(requireHandle(), buffer, w, h, colorspace.code(), copy))
            .throwIfError("tvg_picture_load_raw");
    }

    /**
     * Loads a picture data from a memory block of a given size.
     * <p>
     * ThorVG efficiently caches the loaded data using the specified data address as a key
     * when copy is false. This means that loading the same data again will not result in duplicate operations
     * for the sharable data. Instead, ThorVG will reuse the previously loaded picture data.
     *
     * @param data     A pointer to a memory location where the content of the picture file is stored.
     * @param mimetype Mimetype or extension of data such as "jpg", "jpeg", "svg", "svg+xml", "lot", "lottie+json", "png", etc. In case an empty string or an unknown type is provided, the loaders will be tried one by one.
     * @param rpath    A resource directory path, if the data needs to access any external resources.
     * @param copy     If true the data are copied into the engine local buffer, otherwise they are not.
     */
    public void loadData(byte[] data, String mimetype, String rpath, boolean copy) {
        Objects.requireNonNull(data, "data");
        ThorvgResult.fromCode(ThorvgNative.pictureLoadData(requireHandle(), data, data.length, mimetype, rpath, copy))
            .throwIfError("tvg_picture_load_data");
    }

    /**
     * Sets the asset resolver callback for handling external resources (e.g., images and fonts).
     * <p>
     * This callback is invoked when an external asset reference (such as an image source or file path)
     * is encountered in a Picture object. It allows the user to provide a custom mechanism for loading
     * or substituting assets, such as loading from an external source or a virtual filesystem.
     *
     * @param resolver A user-defined resolver instance.
     *                 <b>Note:</b> This function must be called before load()
     *                 Setting the resolver after loading will have no effect on asset resolution for that asset.
     */
    public void setAssetResolver(PictureAssetResolver resolver) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetAssetResolver(requireHandle(), resolver))
            .throwIfError("tvg_picture_set_asset_resolver");
    }

    /**
     * Resizes the picture content to the given width and height.
     * <p>
     * The picture content is resized while keeping the default size aspect ratio.
     * The scaling factor is established for each of dimensions and the smaller value is applied to both of them.
     *
     * @param width  A new width of the image in pixels.
     * @param height A new height of the image in pixels.
     */
    public void setSize(float width, float height) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetSize(requireHandle(), width, height))
            .throwIfError("tvg_picture_set_size");
    }

    /**
     * Gets the size of the loaded picture.
     *
     * @return An array containing [width, height] in pixels.
     */
    public float[] getSize() {
        float[] out = new float[2];
        ThorvgResult.fromCode(ThorvgNative.pictureGetSize(requireHandle(), out))
            .throwIfError("tvg_picture_get_size");
        return out;
    }

    /**
     * Sets the normalized origin point of the Picture object.
     * <p>
     * This method defines the origin point of the Picture using normalized coordinates.
     * Unlike a typical pivot point used only for transformations, this origin affects both
     * the transformation behavior and the actual rendering position of the Picture.
     * <p>
     * The specified origin becomes the reference point for positioning the Picture on the canvas.
     * For example, setting the origin to (0.5f, 0.5f) moves the visual center of the picture
     * to the position specified by translate().
     * <p>
     * The coordinates are given in a normalized range relative to the picture's bounds:
     * - (0.0f, 0.0f): top-left corner
     * - (0.5f, 0.5f): center
     * - (1.0f, 1.0f): bottom-right corner
     *
     * @param x The normalized x-coordinate of the origin point (range: 0.0f to 1.0f).
     * @param y The normalized y-coordinate of the origin point (range: 0.0f to 1.0f).
     */
    public void setOrigin(float x, float y) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetOrigin(requireHandle(), x, y))
            .throwIfError("tvg_picture_set_origin");
    }

    /**
     * Gets the normalized origin point of the Picture object.
     * <p>
     * This method retrieves the current origin point of the Picture, expressed
     * in normalized coordinates relative to the picture’s bounds.
     *
     * @return An array containing [x, y] coordinates of the origin point.
     */
    public float[] getOrigin() {
        float[] out = new float[2];
        ThorvgResult.fromCode(ThorvgNative.pictureGetOrigin(requireHandle(), out))
            .throwIfError("tvg_picture_get_origin");
        return out;
    }

    /**
     * Retrieve a paint object from the Picture scene by its Unique ID.
     * <p>
     * This function searches for a paint object within the Picture scene that matches the provided id.
     *
     * @param id The Unique ID of the paint object.
     * @return A handle to the paint object that matches the given identifier, or 0 if no matching paint object is found.
     */
    public long getPaint(int id) {
        return ThorvgNative.pictureGetPaint(requireHandle(), id);
    }

    /**
     * Sets the image filtering method for rendering this picture.
     * <p>
     * Specifies how the image data should be filtered when it is scaled or transformed
     * during rendering. This affects the visual quality and performance of the output.
     *
     * @param method The filtering method to apply.
     */
    public void setFilter(int method) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetFilter(requireHandle(), method))
            .throwIfError("tvg_picture_set_filter");
    }

    /**
     * Enable or disable accessible mode for a Picture.
     * <p>
     * When accessible mode is enabled, the Picture maintains an internal mapping
     * of ID-accessible vector assets nodes (such as SVG), allowing efficient access to Paint objects
     * and their associated identifier information via Accessor APIs.
     * <p>
     * When disabled, no additional mapping is maintained and all nodes are treated
     * as general traversal targets.
     *
     * @param accessible Set to true to enable accessible mode, or false to disable it.
     */
    public void setAccessible(boolean accessible) {
        ThorvgResult.fromCode(ThorvgNative.pictureSetAccessible(requireHandle(), accessible))
            .throwIfError("tvg_picture_set_accessible");
    }

    @Override
    public void close() {
        if (!freeOnClose)
            return;
        super.close();
    }
}
