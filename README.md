# jthorvg

`jthorvg` is a Gradle-based Java library that wraps a focused subset of the [ThorVG](https://github.com/thorvg/thorvg) C API through JNI.

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

## Building the Windows JNI DLL

The primary native target is Windows. The Gradle task expects:

- a Windows host
- Microsoft Visual C++ tools available on `PATH` (for example from a Visual Studio Native Tools prompt)
- ThorVG built separately
- ThorVG headers and import libraries configured through either environment variables or Gradle properties

Supported configuration values:

- `THORVG_HOME` → used to derive `src/bindings/capi`
- `THORVG_INCLUDE_DIR`
- `THORVG_LIB_DIR`
- `THORVG_LIBRARY_NAME` (defaults to `thorvg.lib`)

Example in a Windows Native Tools shell:

```powershell
$env:THORVG_HOME = 'C:\dev\thorvg'
$env:THORVG_LIB_DIR = 'C:\dev\thorvg\build\lib'
.\gradlew.bat :lib:buildWindowsJni
```

When built, the JNI DLL is named `jthorvg_jni.dll`.

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
