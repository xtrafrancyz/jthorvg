import java.util.*

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
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
val vendoredThorvgDir = project.file("src/main/c/thorvg")
val javaHome = javaToolchains.launcherFor(java.toolchain).map { it.metadata.installationPath.asFile }
val prebuiltNativeDir = providers.gradleProperty("prebuiltNativeDir").map { project.file(it) }

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

    if (os == "windows") {
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

fun commandExists(workingDir: File, command: String): Boolean {
    return try {
        val process = ProcessBuilder("cmd", "/c", "where", command)
            .directory(workingDir)
            .redirectErrorStream(true)
            .start()
        process.inputStream.bufferedReader().useLines { lines ->
            lines.forEach { _ -> }
        }
        process.waitFor() == 0
    } catch (_: Exception) {
        false
    }
}

sourceSets.named("main") {
    resources.srcDir(packagedNativeResourcesDir)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:all,-missing", "-quiet")
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
            "-Db_vscrt=mt",
            "-Dengines=cpu,gl",
            "-Dloaders=all",
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
        val thorvgStaticArchive = thorvgBuildDir.resolve("src/libthorvg-1.a")
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
            "windows" -> {
                if (commandExists(projectDir, "cl")) {
                    logger.lifecycle("MSVC toolchain detected; building native library with cl.")
                    runCommand(
                        projectDir,
                        "cl",
                        "/nologo",
                        "/LD",
                        "/MT",
                        "/DTVG_STATIC",
                        "/I${javaIncludeDir.absolutePath}",
                        "/I${javaPlatformIncludeDir.absolutePath}",
                        "/I${capiIncludeDir.absolutePath}",
                        jniSource.absolutePath,
                        thorvgStaticLibrary.absolutePath,
                        "/link",
                        "/OUT:${outputFile.absolutePath}"
                    )
                } else {
                    logger.lifecycle("MSVC toolchain is unavailable; retrying native build with MinGW g++.")
                    runCommand(
                        projectDir,
                        "g++",
                        "-shared",
                        "-static",
                        "-DTVG_STATIC",
                        "-I${javaIncludeDir.absolutePath}",
                        "-I${javaPlatformIncludeDir.absolutePath}",
                        "-I${capiIncludeDir.absolutePath}",
                        "-x",
                        "c",
                        jniSource.absolutePath,
                        "-x",
                        "none",
                        thorvgStaticArchive.absolutePath,
                        "-static-libgcc",
                        "-static-libstdc++",
                        "-lwinpthread",
                        "-o",
                        outputFile.absolutePath
                    )
                }
            }

            "linux" -> runCommand(
                projectDir,
                "g++",
                "-shared",
                "-fPIC",
                "-fvisibility=hidden",
                "-I${javaIncludeDir.absolutePath}",
                "-I${javaPlatformIncludeDir.absolutePath}",
                "-I${capiIncludeDir.absolutePath}",
                jniSource.absolutePath,
                thorvgStaticLibrary.absolutePath,
                "-static-libgcc",
                "-static-libstdc++",
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

val verifyPrebuiltNative = tasks.register("verifyPrebuiltNative") {
    onlyIf { prebuiltNativeDir.isPresent }

    doLast {
        val prebuiltDir = prebuiltNativeDir.get()
        if (!prebuiltDir.isDirectory) {
            throw GradleException("Configured prebuilt native directory does not exist: ${prebuiltDir.absolutePath}")
        }
        if (!prebuiltDir.resolve("linux/lib$nativeLibraryBaseName.so").isFile) {
            throw GradleException("Missing Linux JNI library in prebuilt native directory.")
        }
        if (!prebuiltDir.resolve("windows/$nativeLibraryBaseName.dll").isFile) {
            throw GradleException("Missing Windows JNI library in prebuilt native directory.")
        }
    }
}

val packageNative = tasks.register<Sync>("packageNative") {
    if (prebuiltNativeDir.isPresent) {
        val prebuiltDir = prebuiltNativeDir.get()
        dependsOn(verifyPrebuiltNative)
        from(prebuiltDir)
    } else {
        dependsOn(buildNative)
        from(layout.buildDirectory.dir("native"))
    }
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
    val os = hostOs.get()
    if (os != "unsupported") {
        val nativeDir = layout.buildDirectory.dir("native/$os").get().asFile
        systemProperty("java.library.path", nativeDir.absolutePath)
    }

    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("jthorvg")
                description.set("ThorVG JNI bindings for Java")
                url.set("https://github.com/xtrafrancyz/jthorvg")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit/")
                    }
                }
                developers {
                    developer {
                        id.set("xtrafrancyz")
                        name.set("Dmytro Manchynskyi")
                        email.set("xtrafrancyz@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/xtrafrancyz/jthorvg.git")
                    developerConnection.set("scm:git:ssh://git@github.com/xtrafrancyz/jthorvg.git")
                    url.set("https://github.com/xtrafrancyz/jthorvg")
                }
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_SIGNING_KEY")
    val signingPassword = System.getenv("GPG_PASSPHRASE")
    if (!signingKey.isNullOrEmpty() && !signingPassword.isNullOrEmpty()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["mavenJava"])
    } else {
        logger.warn("GPG signing key not found. Skipping signing.")
    }
}

nexusPublishing {
    repositories {
        create("SonatypeCentral") {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(System.getenv("SONATYPE_USERNAME") ?: "")
            password.set(System.getenv("SONATYPE_PASSWORD") ?: "")
        }
    }
}
