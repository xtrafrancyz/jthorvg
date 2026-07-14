package io.github.xtrafrancyz.jthorvg;

abstract class NativeHandle implements AutoCloseable {
    private long handle;

    NativeHandle(long handle) {
        if (handle == 0) {
            throw new IllegalArgumentException("Native handle must not be 0");
        }
        this.handle = handle;
    }

    protected final long requireHandle() {
        if (handle == 0) {
            throw new IllegalStateException("Native object is already closed");
        }
        return handle;
    }

    protected final void clearHandle() {
        handle = 0;
    }

    public final boolean isClosed() {
        return handle == 0;
    }
}
