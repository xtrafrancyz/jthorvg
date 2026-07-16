package io.github.xtrafrancyz.jthorvg;

/**
 * A module for managing and drawing graphical elements.
 * <p>
 * A canvas is an entity responsible for drawing the target. It sets up the drawing engine and the buffer, which can be drawn on the screen. It also manages given Paint objects.
 * <p>
 * Note: A Canvas behavior depends on the raster engine though the final content of the buffer is expected to be identical.
 * Warning: The Paint objects belonging to one Canvas can't be shared among multiple Canvases.
 */
public abstract class Canvas extends NativeHandle {
    Canvas(long handle) {
        super(handle);
    }

    /**
     * Adds a paint object to the canvas for rendering.
     * <p>
     * Adds the specified paint into the canvas root scene. Only paints added to
     * the canvas are considered rendering targets. The canvas retains the paint
     * object until it is explicitly removed via remove().
     * <p>
     * <b>Note:</b> Ownership of the paint object is transferred to the canvas upon
     * successful addition. To retain ownership, call paint.ref()
     * before adding it to the canvas.
     * <p>
     * <b>Note:</b> The rendering order of paint objects follows the order in which they are
     * added to the canvas. If layering is required, ensure paints are added in
     * the desired order.
     *
     * @param paint A handle to the paint object to be rendered.
     */
    public void add(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.canvasAdd(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_canvas_add");
    }

    /**
     * Inserts a paint object into the canvas root scene.
     * <p>
     * Inserts a paint object into the root scene of the specified canvas. If the
     * at parameter is provided, the paint object is inserted immediately before
     * the specified paint in the root scene. If at is null, the paint object
     * is appended to the end of the root scene.
     * <p>
     * <b>Note:</b> Ownership of the target object is transferred to the canvas upon
     * successful addition. To retain ownership, call paint.ref()
     * before adding it to the canvas.
     * <p>
     * <b>Note:</b> The rendering order of paint objects follows their order in the root
     * scene. If layering is required, ensure paints are inserted in the
     * desired order.
     *
     * @param target A handle to the paint object to be inserted into the root scene.
     *               This parameter must not be null.
     * @param at     A handle to an existing paint object in the root scene before
     *               which target will be inserted. If null, target is
     *               appended to the end of the root scene.
     */
    public void insert(Paint target, Paint at) {
        ThorvgResult.fromCode(ThorvgNative.canvasInsert(requireHandle(), target.requireHandle(), at.requireHandle()))
            .throwIfError("tvg_canvas_insert");
    }

    /**
     * Removes a paint object from the root scene.
     * <p>
     * This function removes a specified paint object from the root scene. If no paint
     * object is specified (i.e., null is used), the function
     * performs to clear all paints from the scene.
     *
     * @param paint A pointer to the Paint object to be removed from the root scene.
     *              If null, remove all the paints from the root scene.
     */
    public void remove(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.canvasRemove(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_canvas_remove");
    }

    /**
     * Sets the drawing region of the canvas.
     * <p>
     * This function defines a rectangular area of the canvas to be used for drawing operations.
     * The specified viewport clips rendering output to the boundaries of that rectangle.
     * <p>
     * Please note that changing the viewport is only allowed at the beginning of the rendering sequence—that is, after calling sync().
     * <p>
     * <b>Warning:</b> Changing the viewport is not allowed after calling add(),
     * remove(), update(), or draw().
     * <p>
     * <b>Note:</b> When the target is reset, the viewport will also be reset to match the target size.
     *
     * @param x      The x-coordinate of the upper-left corner of the rectangle.
     * @param y      The y-coordinate of the upper-left corner of the rectangle.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public void setViewport(int x, int y, int width, int height) {
        ThorvgResult.fromCode(ThorvgNative.canvasSetViewport(requireHandle(), x, y, width, height))
            .throwIfError("tvg_canvas_set_viewport");
    }

    /**
     * Requests the canvas to update modified paint objects in preparation for rendering.
     * <p>
     * This function triggers an internal update for all paint instances that have been modified
     * since the last update. It ensures that the canvas state is ready for accurate rendering.
     *
     * <p>
     * <b>Note:</b> Only paint objects that have been changed will be processed.
     * <p>
     * <b>Note:</b> If the canvas is configured with multiple threads, the update may be performed asynchronously.
     */
    public void update() {
        ThorvgResult.fromCode(ThorvgNative.canvasUpdate(requireHandle())).throwIfError("tvg_canvas_update");
    }

    /**
     * Requests the canvas to render the Paint objects.
     * <b>Note:</b> Clearing the buffer is unnecessary if the canvas will be fully covered
     * with opaque content. Skipping the clear can improve performance.
     * <p>
     * <b>Note:</b> Drawing may be performed asynchronously if the thread count is greater than zero.
     * To ensure the drawing process is complete, call sync() afterwards.
     * <p>
     * <b>Note:</b> If the canvas has not been updated prior to draw(), it may implicitly perform update()
     *
     * @param clear If true, clears the target buffer to zero before drawing.
     */
    public void draw(boolean clear) {
        ThorvgResult.fromCode(ThorvgNative.canvasDraw(requireHandle(), clear)).throwIfError("tvg_canvas_draw");
    }

    /**
     * Guarantees that drawing task is finished.
     * <p>
     * The Canvas rendering can be performed asynchronously. To make sure that rendering is finished,
     * the sync() must be called after the draw() regardless of threading.
     */
    public void sync() {
        ThorvgResult.fromCode(ThorvgNative.canvasSync(requireHandle())).throwIfError("tvg_canvas_sync");
    }

    /**
     * Clears the canvas internal data, releases all paints stored by the canvas and destroys the canvas object itself.
     */
    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.canvasDestroy(handle)).throwIfError("tvg_canvas_destroy");
        clearHandle();
    }
}
