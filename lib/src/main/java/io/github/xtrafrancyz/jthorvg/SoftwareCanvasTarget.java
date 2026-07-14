package io.github.xtrafrancyz.jthorvg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Objects;

public final class SoftwareCanvasTarget {
    private final IntBuffer pixels;
    private final int width;
    private final int height;
    private final int stride;
    private final ThorvgColorspace colorspace;

    private SoftwareCanvasTarget(IntBuffer pixels, int width, int height, int stride, ThorvgColorspace colorspace) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.stride = stride;
        this.colorspace = colorspace;
    }

    public static SoftwareCanvasTarget allocateArgb8888(int width, int height) {
        int pixelCount = Math.multiplyExact(width, height);
        ByteBuffer storage = ByteBuffer.allocateDirect(Math.multiplyExact(pixelCount, Integer.BYTES))
            .order(ByteOrder.nativeOrder());
        return wrap(storage.asIntBuffer(), width, height, width, ThorvgColorspace.ARGB8888);
    }

    public static SoftwareCanvasTarget wrap(IntBuffer pixels, int width, int height, int stride, ThorvgColorspace colorspace) {
        Objects.requireNonNull(pixels, "pixels");
        Objects.requireNonNull(colorspace, "colorspace");
        if (!pixels.isDirect()) {
            throw new IllegalArgumentException("pixels must be a direct IntBuffer");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("width must be greater than 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be greater than 0");
        }
        if (stride < width) {
            throw new IllegalArgumentException("stride must be greater than or equal to width");
        }
        int requiredCapacity = Math.multiplyExact(stride, height);
        if (pixels.capacity() < requiredCapacity) {
            throw new IllegalArgumentException("pixels capacity must be at least stride * height");
        }
        return new SoftwareCanvasTarget(pixels, width, height, stride, colorspace);
    }

    IntBuffer nativeBuffer() {
        return pixels;
    }

    public IntBuffer pixels() {
        return pixels.duplicate();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int stride() {
        return stride;
    }

    public ThorvgColorspace colorspace() {
        return colorspace;
    }
}
