package io.github.xtrafrancyz.jthorvg;

public abstract class Canvas extends NativeHandle {
    Canvas(long handle) {
        super(handle);
    }

    public void add(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.canvasAdd(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_canvas_add");
    }

    public void insert(Paint target, Paint at) {
        ThorvgResult.fromCode(ThorvgNative.canvasInsert(requireHandle(), target.requireHandle(), at.requireHandle()))
            .throwIfError("tvg_canvas_insert");
    }

    public void remove(Paint paint) {
        ThorvgResult.fromCode(ThorvgNative.canvasRemove(requireHandle(), paint.requireHandle()))
            .throwIfError("tvg_canvas_remove");
    }

    public void setViewport(int x, int y, int width, int height) {
        ThorvgResult.fromCode(ThorvgNative.canvasSetViewport(requireHandle(), x, y, width, height))
            .throwIfError("tvg_canvas_set_viewport");
    }

    public void update() {
        ThorvgResult.fromCode(ThorvgNative.canvasUpdate(requireHandle())).throwIfError("tvg_canvas_update");
    }

    public void draw(boolean clear) {
        ThorvgResult.fromCode(ThorvgNative.canvasDraw(requireHandle(), clear)).throwIfError("tvg_canvas_draw");
    }

    public void sync() {
        ThorvgResult.fromCode(ThorvgNative.canvasSync(requireHandle())).throwIfError("tvg_canvas_sync");
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.canvasDestroy(handle)).throwIfError("tvg_canvas_destroy");
        clearHandle();
    }
}
