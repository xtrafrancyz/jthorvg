package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

public final class Accessor extends NativeHandle {
    Accessor(long handle) {
        super(handle);
    }

    public void set(Paint paint, AccessorCallback callback) {
        Objects.requireNonNull(paint, "paint");
        Objects.requireNonNull(callback, "callback");
        ThorvgResult.fromCode(ThorvgNative.accessorSet(requireHandle(), paint.requireHandle(), callback))
            .throwIfError("tvg_accessor_set");
    }

    public static int generateId(String name) {
        Objects.requireNonNull(name, "name");
        return ThorvgNative.accessorGenerateId(name);
    }

    public String getName(int id) {
        return ThorvgNative.accessorGetName(requireHandle(), id);
    }

    @Override
    public void close() {
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.accessorDel(handle)).throwIfError("tvg_accessor_del");
        clearHandle();
    }
}
