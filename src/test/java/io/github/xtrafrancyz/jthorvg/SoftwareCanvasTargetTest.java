package io.github.xtrafrancyz.jthorvg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.junit.jupiter.api.Test;

class SoftwareCanvasTargetTest {
    @Test
    void allocatesDirectArgbTarget() {
        SoftwareCanvasTarget target = SoftwareCanvasTarget.allocateArgb8888(4, 3);

        assertEquals(4, target.width());
        assertEquals(3, target.height());
        assertEquals(4, target.stride());
        assertEquals(ThorvgColorspace.ARGB8888, target.colorspace());
        assertTrue(target.pixels().isDirect());
        assertEquals(12, target.pixels().capacity());
    }

    @Test
    void rejectsNonDirectBuffers() {
        IntBuffer heapBuffer = IntBuffer.allocate(16);

        assertThrows(
            IllegalArgumentException.class,
            () -> SoftwareCanvasTarget.wrap(heapBuffer, 4, 4, 4, ThorvgColorspace.ARGB8888)
        );
    }

    @Test
    void rejectsBuffersThatAreTooSmall() {
        IntBuffer directBuffer = ByteBuffer.allocateDirect(10 * Integer.BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();

        assertThrows(
            IllegalArgumentException.class,
            () -> SoftwareCanvasTarget.wrap(directBuffer, 4, 3, 4, ThorvgColorspace.ARGB8888)
        );
    }
}
