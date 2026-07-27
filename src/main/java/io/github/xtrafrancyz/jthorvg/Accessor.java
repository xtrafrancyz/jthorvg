package io.github.xtrafrancyz.jthorvg;

import java.util.Objects;

/**
 * A module for manipulation of the scene tree.
 * <p>
 * This module helps to control the scene tree.
 */
public final class Accessor extends NativeHandle {
    Accessor(long handle) {
        super(handle);
    }

    /**
     * Sets the paint of the accessor then iterates through its descendents.
     * <p>
     * Iterates through all descendents of the scene passed through the paint argument
     * while calling func on each and passing the data pointer to this function. When
     * func returns false iteration stops and the function returns.
     *
     * @param paint    A scene object.
     * @param callback Callback to execute for each child.
     */
    public void set(Paint paint, AccessorCallback callback) {
        Objects.requireNonNull(paint, "paint");
        Objects.requireNonNull(callback, "callback");
        ThorvgResult.fromCode(ThorvgNative.accessorSet(requireHandle(), paint.requireHandle(), callback))
            .throwIfError("tvg_accessor_set");
    }

    /**
     * Generate a unique ID (hash key) from a given name.
     * <p>
     * This function computes a unique identifier value based on the provided string.
     * You can use this to assign a unique ID to the Paint object.
     *
     * @param name The input string to generate the unique identifier from.
     * @return The generated unique identifier value.
     */
    public static int generateId(String name) {
        Objects.requireNonNull(name, "name");
        return ThorvgNative.accessorGenerateId(name);
    }

    /**
     * Retrieve the original name string from a given unique ID.
     * <p>
     * Returns the name associated with the specified identifier.
     * <p>
     * This method is only valid when accessible mode is set to true
     * for the Picture associated with the given paint. Otherwise, the name
     * information may not be available.
     *
     * @param id The unique identifier.
     * @return The corresponding name string, or null if not found or unavailable.
     */
    public String getName(int id) {
        return ThorvgNative.accessorGetName(requireHandle(), id);
    }

    /**
     * Deletes the given accessor object.
     */
    @Override
    public void close() {
        if (isClosed())
            return;
        long handle = requireHandle();
        ThorvgResult.fromCode(ThorvgNative.accessorDel(handle)).throwIfError("tvg_accessor_del");
        clearHandle();
    }
}
