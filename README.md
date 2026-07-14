# jthorvg

`jthorvg` is a Gradle-based Java library that wraps a focused subset of the [ThorVG](https://github.com/thorvg/thorvg) C API through JNI.

ThorVG is vendored into this repository under `vendor/thorvg`, and the native build links that vendored code directly into the JNI shared library so the final output is a single ready-to-load `.dll` or `.so`.

## Included wrapper surface

The initial wrapper covers the core pieces needed to render with the software canvas:

- engine loading, initialization, shutdown, and version lookup
- software canvas creation and target binding
- adding paints, updating, drawing, and syncing a canvas
- shape creation, rectangle paths, fill colors, and paint transforms
- picture creation, file loading, and sizing

The JNI bridge is implemented against ThorVG's C API header from:

`src/bindings/capi/thorvg_capi.h`

## Project layout

- `gradlew` / `gradlew.bat` — Gradle wrapper
- `lib/src/main/java` — Java API
- `lib/src/main/c/jthorvg_jni.c` — JNI bridge
- `lib/src/test/java` — focused unit tests for the Java wrapper layer

## Build and test

From the repository root:

```bash
./gradlew test
./gradlew build
```

## Building the native library

The native build uses the vendored ThorVG sources and produces a single JNI shared library with ThorVG linked in statically.

Supported hosts:

- Linux → `libjthorvg_jni.so`
- Windows → `jthorvg_jni.dll`

Required tools:

- Java 17+
- `meson`
- `ninja`
- a host C/C++ toolchain
  - Linux: `gcc` / `g++`
  - Windows: Microsoft Visual C++ tools on `PATH` (for example from a Visual Studio Native Tools prompt)

Build the native library from the repository root with:

```bash
./gradlew :lib:buildNative
```

The resulting shared library is written to:

- Linux: `lib/build/native/linux/libjthorvg_jni.so`
- Windows: `lib/build/native/windows/jthorvg_jni.dll`

The default Gradle Java build also packages the current host's shared library into the main artifact resources.

## Using the library

Load the JNI bridge before calling ThorVG APIs:

```java
Thorvg.load();
Thorvg.init(0);

SoftwareCanvasTarget target = SoftwareCanvasTarget.allocateArgb8888(256, 256);
SoftwareCanvas canvas = Thorvg.newSoftwareCanvas();
canvas.setTarget(target);

Shape shape = Thorvg.newShape();
shape.appendRect(32, 32, 192, 192, 0, 0, true);
shape.setFillColor(0, 128, 255, 255);
canvas.add(shape);
canvas.draw(true);
canvas.sync();
```

The software canvas target uses a direct `IntBuffer`, which is required so ThorVG can safely keep the native buffer pointer across draw/sync calls.
