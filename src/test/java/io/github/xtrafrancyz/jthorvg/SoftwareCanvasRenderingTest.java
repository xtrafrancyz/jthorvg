package io.github.xtrafrancyz.jthorvg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SoftwareCanvasRenderingTest {

    @BeforeAll
    static void setUp() {
        Thorvg.load();
        Thorvg.init(0);
    }

    @AfterAll
    static void tearDown() {
        Thorvg.term();
    }

    @Test
    void testDrawRedRectangleOnWhiteBackgroundArgb() {
        int width = 10;
        int height = 10;
        SoftwareCanvasTarget target = SoftwareCanvasTarget.allocateArgb8888(width, height);
        assertEquals(ThorvgColorspace.ARGB8888, target.colorspace());

        try (SoftwareCanvas canvas = Thorvg.newSoftwareCanvas()) {
            canvas.setTarget(target);

            // 1. Draw white background covering the whole canvas
            try (Shape bg = Thorvg.newShape()) {
                bg.appendRect(0, 0, width, height, 0, 0, true);
                bg.setFillColor(255, 255, 255, 255);
                canvas.add(bg);
            }

            // 2. Draw a red rectangle in the center (x=2, y=2, width=6, height=6)
            try (Shape rect = Thorvg.newShape()) {
                rect.appendRect(2, 2, 6, 6, 0, 0, true);
                rect.setFillColor(255, 0, 0, 255);
                canvas.add(rect);
            }

            // Draw and sync
            canvas.draw(true);
            canvas.sync();
        }

        IntBuffer pixels = target.pixels();

        // Background pixel at (0, 0) should be white (0xFFFFFFFF)
        int bgPixel = pixels.get(0);
        assertEquals(0xFFFFFFFF, bgPixel, "Background pixel at (0,0) must be white");

        // Center pixel at (5, 5) should be red.
        // For ARGB8888 in little-endian native byte order:
        // High byte (Alpha) = 0xFF, Red = 0xFF, Green = 0x00, Blue = 0x00
        // Packed as int: 0xFFFF0000
        int centerPixel = pixels.get(5 * width + 5);
        assertEquals(0xFFFF0000, centerPixel, "Center pixel at (5,5) must be red (0xFFFF0000)");
    }

    @Test
    void testDrawSvgImageOnCanvas() {
        int width = 10;
        int height = 10;
        SoftwareCanvasTarget target = SoftwareCanvasTarget.allocateArgb8888(width, height);
        assertEquals(ThorvgColorspace.ARGB8888, target.colorspace());

        try (SoftwareCanvas canvas = Thorvg.newSoftwareCanvas()) {
            canvas.setTarget(target);

            // Draw an SVG image (a simple red circle)
            String svgData = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10\" height=\"10\">" +
                             "<circle cx=\"5\" cy=\"5\" r=\"4\" fill=\"red\" /></svg>";
            Picture picture = Thorvg.newPicture();
            picture.loadData(svgData.getBytes(), "svg", "", true);
            picture.setOrigin(0, 0);
            picture.setSize(width, height);
            canvas.add(picture);

            // Draw and sync
            canvas.draw(true);
            canvas.sync();
        }

        IntBuffer pixels = target.pixels();

        // Center pixel at (5, 5) should be red.
        int centerPixel = pixels.get(5 * width + 5);
        assertEquals(0xFFFF0000, centerPixel, "Center pixel at (5,5) must be red (0xFFFF0000)");
    }
}
