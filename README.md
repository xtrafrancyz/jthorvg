# jthorvg

[![Maven Central](https://img.shields.io/maven-central/v/io.github.xtrafrancyz/jthorvg?color=blue)](https://central.sonatype.com/artifact/io.github.xtrafrancyz/jthorvg)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Java JNI bindings for [ThorVG](https://github.com/thorvg/thorvg) — a lightweight, open-source vector graphics engine.

Render SVG, Lottie animations, shapes, text, and raster images to off-screen pixel buffers on **Linux** and **Windows** (x86_64), all from Java. The native ThorVG library is bundled inside the JAR — no separate installation required.

---

## Features

- **SVG** loading and rendering
- **Lottie** animation playback with frame-level control, markers, tweening, and slot overrides
- **Shapes** — paths, rectangles, circles, arcs, strokes, solid fills, linear/radial gradients
- **Pictures** — load PNG, JPEG, WEBP, SVG, Lottie from file, byte array, or raw pixel data
- **Text** rendering with font loading
- **Scenes** — group and compose paint objects
- **Software canvas** (CPU rasterizer) and **OpenGL/ES canvas**
- Bundled native library — zero extra setup for Linux and Windows (amd64)

## Requirements

- Java 17+

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.xtrafrancyz:jthorvg:<version>")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'io.github.xtrafrancyz:jthorvg:<version>'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.xtrafrancyz</groupId>
    <artifactId>jthorvg</artifactId>
    <version><!-- version --></version>
</dependency>
```

Replace `<version>` with the latest version shown in the badge above.

---

## Usage

### 1. Initialize the engine

Load the native library (auto-extracts the bundled binary) and initialize the ThorVG engine once at startup:

```java
import io.github.xtrafrancyz.jthorvg.*;

// Load the bundled native library (extracts to a temp dir on first call)
Thorvg.load();

// Initialize with 0 worker threads (main-thread-only rendering)
// Increase the count to enable parallel rendering
Thorvg.init(0);
```

Shut down when done:

```java
Thorvg.term();
```

---

### 2. Render a shape to a pixel buffer

```java
int width = 200;
int height = 200;

// Allocate an off-screen ARGB8888 buffer
SoftwareCanvasTarget target = SoftwareCanvasTarget.allocateArgb8888(width, height);

try (SoftwareCanvas canvas = Thorvg.newSoftwareCanvas()) {
    canvas.setTarget(target);

    // White background
    try (Shape bg = Thorvg.newShape()) {
        bg.appendRect(0, 0, width, height, 0, 0, true);
        bg.setFillColor(255, 255, 255, 255);
        canvas.add(bg);
    }

    // Red filled circle
    try (Shape circle = Thorvg.newShape()) {
        circle.appendCircle(100, 100, 80, 80);
        circle.setFillColor(255, 0, 0, 255);
        canvas.add(circle);
    }

    canvas.draw(true);
    canvas.sync();
}

// Access the rendered pixels (ARGB packed ints)
java.nio.IntBuffer pixels = target.pixels();
```

---

### 3. Render an SVG file

```java
try (SoftwareCanvas canvas = Thorvg.newSoftwareCanvas()) {
    canvas.setTarget(SoftwareCanvasTarget.allocateArgb8888(512, 512));

    Picture picture = Thorvg.newPicture();
    picture.load(java.nio.file.Path.of("image.svg"));
    picture.setSize(512, 512);
    canvas.add(picture);

    canvas.draw(true);
    canvas.sync();
}
```

SVG data can also be loaded from a byte array:

```java
byte[] svgBytes = "<svg ...>...</svg>".getBytes();
picture.loadData(svgBytes, "svg", "", true);
```

---

### 4. Play a Lottie animation

```java
LottieAnimation animation = Thorvg.newLottieAnimation();
Picture picture = animation.getPicture();
picture.load(java.nio.file.Path.of("animation.json"));

float totalFrames = animation.getTotalFrame();
float duration    = animation.getDuration(); // seconds

try (SoftwareCanvas canvas = Thorvg.newSoftwareCanvas()) {
    canvas.setTarget(SoftwareCanvasTarget.allocateArgb8888(512, 512));
    canvas.add(picture);

    // Render frame 30
    animation.setFrame(30);
    canvas.update(picture);
    canvas.draw(true);
    canvas.sync();
}

animation.close();
```

#### Lottie extras

```java
// Play a named marker segment
animation.setMarker("intro");

// Tween between two frames
animation.tween(0, 60, 0.5f); // 50% between frame 0 and 60

// Override a slot with custom JSON data
int slotId = animation.genSlot("{\"key\":\"value\"}");
animation.applySlot(slotId);
```

---

### 5. Gradients

```java
try (Shape shape = Thorvg.newShape()) {
    shape.appendRect(10, 10, 180, 180, 0, 0, true);

    LinearGradient gradient = Thorvg.newLinearGradient();
    gradient.setLinear(10, 10, 190, 190);
    // configure color stops via native API ...
    shape.setFillGradient(gradient);

    canvas.add(shape);
}
```

---

## API Overview

| Class | Purpose |
|---|---|
| `Thorvg` | Engine lifecycle and factory for all objects |
| `SoftwareCanvas` | CPU-based off-screen canvas |
| `GLCanvas` | OpenGL/ES-based canvas |
| `SoftwareCanvasTarget` | Pixel buffer configuration for `SoftwareCanvas` |
| `Shape` | Paths, rectangles, circles, arcs, strokes, fills |
| `Picture` | Images (SVG, PNG, JPEG, Lottie, raw pixels) |
| `Scene` | Composites multiple paint objects |
| `Text` | Text rendering with font support |
| `Animation` | Frame-based animation control |
| `LottieAnimation` | Extended Lottie controls (markers, tweening, slots) |
| `LinearGradient` | Linear gradient fill |
| `RadialGradient` | Radial gradient fill |
| `Saver` | Export painted content |
| `Accessor` | Walk and modify paint tree nodes |

---

## Building from Source

Prerequisites: Java 17, Python 3, [Meson](https://mesonbuild.com/) + Ninja, and a C++ compiler (GCC/MinGW or MSVC).

```bash
./gradlew build
```

The build compiles the vendored ThorVG source into a static library and links it into the JNI shared library automatically.

To skip the native build and supply prebuilt binaries:

```bash
./gradlew build -PprebuiltNativeDir=/path/to/native-dir
```

The directory must contain `linux/libjthorvg_jni.so` and `windows/jthorvg_jni.dll`.

---

## License

[MIT License](LICENSE) — see the `LICENSE` file for details.

ThorVG is distributed under the [MIT License](https://github.com/thorvg/thorvg/blob/main/LICENSE).
