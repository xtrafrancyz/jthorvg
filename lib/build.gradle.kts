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
val nativeOutputDir = layout.buildDirectory.dir("native/windows")
val packagedNativeResourcesDir = layout.buildDirectory.dir("generated/resources/main")
val isWindowsHost = providers.systemProperty("os.name").map { it.startsWith("Windows") }
val thorvgHome = providers.gradleProperty("thorvgHome")
    .orElse(providers.environmentVariable("THORVG_HOME"))
val thorvgIncludeDir = providers.gradleProperty("thorvgIncludeDir")
    .orElse(providers.environmentVariable("THORVG_INCLUDE_DIR"))
    .orElse(thorvgHome.map { "$it/src/bindings/capi" })
val thorvgLibDir = providers.gradleProperty("thorvgLibDir")
    .orElse(providers.environmentVariable("THORVG_LIB_DIR"))
val thorvgLibraryName = providers.gradleProperty("thorvgLibraryName")
    .orElse(providers.environmentVariable("THORVG_LIBRARY_NAME"))
    .orElse("thorvg.lib")
val javaHome = javaToolchains.launcherFor(java.toolchain).map { it.metadata.installationPath.asFile }

sourceSets.named("main") {
    resources.srcDir(packagedNativeResourcesDir)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

val buildWindowsJni = tasks.register<Exec>("buildWindowsJni") {
    group = "build"
    description = "Builds the Windows JNI bridge DLL when ThorVG headers/libs and MSVC are available."

    val outputFile = nativeOutputDir.map { it.file("$nativeLibraryBaseName.dll") }
    val toolchainJavaHome = javaHome.get()
    nativeOutputDir.get().asFile.mkdirs()

    inputs.file("src/main/c/jthorvg_jni.c")
    inputs.property("thorvgIncludeDir", thorvgIncludeDir.orNull ?: "")
    inputs.property("thorvgLibDir", thorvgLibDir.orNull ?: "")
    inputs.property("thorvgLibraryName", thorvgLibraryName.orNull ?: "")
    outputs.file(outputFile)

    onlyIf {
        val windows = isWindowsHost.getOrElse(false)
        if (!windows) {
            logger.lifecycle("Skipping buildWindowsJni because the host OS is not Windows.")
            return@onlyIf false
        }
        if (!thorvgIncludeDir.isPresent || !thorvgLibDir.isPresent) {
            logger.lifecycle("Skipping buildWindowsJni because THORVG include/lib locations are not configured.")
            return@onlyIf false
        }
        true
    }

    workingDir = projectDir
    commandLine(
        "cl",
        "/nologo",
        "/LD",
        "/I${toolchainJavaHome.resolve("include").absolutePath}",
        "/I${toolchainJavaHome.resolve("include/win32").absolutePath}",
        "/I${thorvgIncludeDir.orNull ?: ""}",
        "src/main/c/jthorvg_jni.c",
        "/link",
        "/LIBPATH:${thorvgLibDir.orNull ?: ""}",
        thorvgLibraryName.get(),
        "/OUT:${outputFile.get().asFile.absolutePath}"
    )
}

val packageWindowsJni = tasks.register<Sync>("packageWindowsJni") {
    dependsOn(buildWindowsJni)
    from(nativeOutputDir)
    include("*.dll")
    into(packagedNativeResourcesDir)
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(packageWindowsJni)
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(packageWindowsJni)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
