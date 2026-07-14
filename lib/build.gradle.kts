import org.gradle.api.GradleException
import java.util.Locale

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

group = "io.github.xtrafrancyz"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withJavadocJar()
    withSourcesJar()
}

val nativeLibraryBaseName = "jthorvg_jni"
val packagedNativeResourcesDir = layout.buildDirectory.dir("generated/resources/main")
val vendoredThorvgDir = rootProject.file("vendor/thorvg")
val javaHome = javaToolchains.launcherFor(java.toolchain).map { it.metadata.installationPath.asFile }

val hostOs = providers.systemProperty("os.name").map { osName ->
    val normalized = osName.lowercase(Locale.ROOT)
    when {
        normalized.contains("windows") -> "windows"
        normalized.contains("linux") -> "linux"
        else -> "unsupported"
    }
}

fun nativeLibraryFileName(os: String): String = when (os) {
    "windows" -> "$nativeLibraryBaseName.dll"
    "linux" -> "lib$nativeLibraryBaseName.so"
    else -> throw GradleException("Unsupported host OS '$os'.")
}

fun resolveThorvgStaticLibrary(buildDir: File, os: String): File {
    val expectedFile = buildDir.resolve("src/libthorvg-1.a")
    if (!expectedFile.isFile)
		throw GradleException("ThorVG static library was not produced at ${expectedFile.absolutePath} ")

	if (os.equals("windows")) {
		val windowsLibFile = expectedFile.parentFile.resolve("thorvg.lib")
		try {
            expectedFile.copyTo(target = windowsLibFile, overwrite = true)
        } catch (e: Exception) {
            throw GradleException("Failed to copy ${expectedFile.name} to ${windowsLibFile.name}: ${e.message}", e)
        }
        return windowsLibFile
	}
	
    return expectedFile
}

fun ensureVendoredThorvg() {
    if (!vendoredThorvgDir.resolve("meson.build").isFile) {
        throw GradleException("Missing vendored ThorVG source at ${vendoredThorvgDir.absolutePath}.")
    }
}

fun runCommand(workingDir: File, vararg command: String) {
    val process = ProcessBuilder(*command)
        .directory(workingDir)
		.redirectErrorStream(true)
        .start()
	process.inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            logger.lifecycle(line)
        }
    }
    val exitCode = process.waitFor()
    if (exitCode != 0) {
        throw GradleException("Command failed with exit code $exitCode: ${command.joinToString(" ")}")
    }
}

sourceSets.named("main") {
    resources.srcDir(packagedNativeResourcesDir)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

val buildVendoredThorvg = tasks.register("buildVendoredThorvg") {
    group = "build"
    description = "Builds the vendored ThorVG static library for the current host."

    inputs.dir(vendoredThorvgDir)
    outputs.dir(layout.buildDirectory.dir("thorvg"))

    onlyIf {
        val supported = hostOs.get() != "unsupported"
        if (!supported) {
            logger.lifecycle("Skipping buildVendoredThorvg because the host OS is not supported.")
        }
        supported
    }

    doLast {
        ensureVendoredThorvg()

        val os = hostOs.get()
        val buildDir = layout.buildDirectory.dir("thorvg/$os").get().asFile
        val setupArgs = mutableListOf("meson", "setup")
        if (buildDir.exists()) {
            setupArgs += "--reconfigure"
        }
        setupArgs += listOf(
            buildDir.absolutePath,
            vendoredThorvgDir.absolutePath,
            "--buildtype=release",
            "-Db_staticpic=true",
            "-Ddefault_library=static",
            "-Dengines=cpu",
            "-Dloaders=svg",
            "-Dbindings=capi",
            "-Dsavers=[]",
            "-Dtools=[]",
            "-Dextra=[]",
            "-Dtests=false",
            "-Dthreads=true",
            "-Dsimd=false",
            "-Dlog=false",
            "-Dfile=true",
            "-Dpartial=true"
        )

        runCommand(project.rootDir, *setupArgs.toTypedArray())
        runCommand(project.rootDir, "meson", "compile", "-C", buildDir.absolutePath)

        resolveThorvgStaticLibrary(buildDir, os)
    }
}

val buildNative = tasks.register("buildNative") {
    group = "build"
    description = "Builds the ready-to-load JNI shared library with vendored ThorVG linked in."

    dependsOn(buildVendoredThorvg)
    inputs.file("src/main/c/jthorvg_jni.c")
    inputs.dir(vendoredThorvgDir.resolve("src/bindings/capi"))
    outputs.dir(layout.buildDirectory.dir("native"))

    onlyIf {
        val supported = hostOs.get() != "unsupported"
        if (!supported) {
            logger.lifecycle("Skipping buildNative because the host OS is not supported.")
        }
        supported
    }

    doLast {
        ensureVendoredThorvg()

        val os = hostOs.get()
        val toolchainJavaHome = javaHome.get()
        val thorvgBuildDir = layout.buildDirectory.dir("thorvg/$os").get().asFile
        val thorvgStaticLibrary = resolveThorvgStaticLibrary(thorvgBuildDir, os)
        val outputFile = layout.buildDirectory.file("native/$os/${nativeLibraryFileName(os)}").get().asFile
        val javaIncludeDir = toolchainJavaHome.resolve("include")
        val javaPlatformIncludeDir = when (os) {
            "windows" -> javaIncludeDir.resolve("win32")
            "linux" -> javaIncludeDir.resolve("linux")
            else -> throw GradleException("Unsupported host OS '$os'.")
        }
        val capiIncludeDir = vendoredThorvgDir.resolve("src/bindings/capi")
        val jniSource = project.file("src/main/c/jthorvg_jni.c")

        if (!thorvgStaticLibrary.isFile) {
            throw GradleException("Expected ThorVG static library at ${thorvgStaticLibrary.absolutePath}.")
        }

        outputFile.parentFile.mkdirs()

        when (os) {
            "windows" -> runCommand(
                projectDir,
                "cl",
                "/nologo",
                "/LD",
                "/DTVG_STATIC",
                "/I${javaIncludeDir.absolutePath}",
                "/I${javaPlatformIncludeDir.absolutePath}",
                "/I${capiIncludeDir.absolutePath}",
                jniSource.absolutePath,
                thorvgStaticLibrary.absolutePath,
                "/link",
                "/OUT:${outputFile.absolutePath}"
            )

            "linux" -> runCommand(
                projectDir,
                "gcc",
                "-shared",
                "-fPIC",
                "-I${javaIncludeDir.absolutePath}",
                "-I${javaPlatformIncludeDir.absolutePath}",
                "-I${capiIncludeDir.absolutePath}",
                jniSource.absolutePath,
                thorvgStaticLibrary.absolutePath,
                "-lstdc++",
                "-lpthread",
                "-o",
                outputFile.absolutePath
            )
        }

        if (!outputFile.isFile) {
            throw GradleException("JNI shared library was not produced at ${outputFile.absolutePath}.")
        }
    }
}

val packageNative = tasks.register<Sync>("packageNative") {
    dependsOn(buildNative)
    from(layout.buildDirectory.dir("native"))
    into(packagedNativeResourcesDir)
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(packageNative)
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(packageNative)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
