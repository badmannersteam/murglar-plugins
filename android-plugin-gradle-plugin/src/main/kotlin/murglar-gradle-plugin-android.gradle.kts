@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.net.URI
import java.util.Properties


plugins {
    id("com.android.application")
    id("kotlin-android")
}

group = "com.github.badmannersteam.murglar-plugins"

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven { url = URI.create("https://jitpack.io") }
}

configurations.configureEach {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {
    "compileOnly"("com.github.badmannersteam.murglar-plugins:core:${Versions.murglarPlugins}")
    "implementation"("com.github.badmannersteam.murglar-plugins:android-plugin-base:${Versions.murglarPlugins}@aar")
}

androidComponents {
    onVariants { variant ->
        variant.runtimeConfiguration.apply {
            exclude("org.jetbrains.kotlin", "kotlin-stdlib")
            exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
            exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
            exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
            exclude("org.jetbrains.kotlinx", "kotlinx-serialization-json")
            exclude("org.jetbrains.kotlinx", "kotlinx-serialization-core")
            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-bom")
            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8")
            exclude("org.threeten", "threetenbp")
            exclude("org.apache.commons", "commons-text")
            exclude("me.xdrop", "fuzzywuzzy")
        }
    }
}


android {
    compileSdk = Versions.targetSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }

    signingConfigs {
        create("signingConfig") {
            val properties = localProperties()
            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
            storeFile = rootProject.file(properties.getProperty("storeFile"))
            storePassword = properties.getProperty("storePassword")
        }
    }

    buildTypes {
        val config = signingConfigs["signingConfig"]
        debug {
            signingConfig = config
        }
        release {
            signingConfig = config
            isMinifyEnabled = true
            proguardFiles += getDefaultProguardFile("proguard-android.txt")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.named("check") {
    doFirst {
        val iconFile = project.file("src/main/res/drawable/icon.xml")
        check(iconFile.exists()) {
            "No plugin icon found (${iconFile.path})!"
        }
    }
}

fun localProperties() = Properties().apply {
    rootProject.file("local.properties")
        .takeIf(File::exists)
        ?.let { load(it.inputStream()) }
        ?: error("No local.properties found!")
}

interface MurglarPluginExtension {
    val id: Property<String>
    val name: Property<String>
    val version: Property<Int>
    val type: Property<PluginType>
    val entryPointClass: Property<String>

    val appId get() = "com.badmanners.murglar.plugin.${id.get()}"
    val fullName get() = "Murglar plugin for ${name.get()}"
    val fullVersion get() = "${Versions.murglarPluginsMajor}.${version.get()}"
    val apkName get() = "murglar-plugin-${id.get()}-${fullVersion}.apk"

    enum class PluginType {
        MURGLAR, COVERS_PROVIDER, LYRICS_PROVIDER, TAGS_PROVIDER
    }
}

val pluginExtension = extensions.create<MurglarPluginExtension>("murglarAndroidPlugin")

val currentProjectName: String = project.name
gradle.afterProject {
    if (name != currentProjectName)
        return@afterProject

    android {
        namespace = pluginExtension.appId

        defaultConfig {
            applicationId = pluginExtension.appId

            versionCode = pluginExtension.version.get()
            versionName = pluginExtension.fullVersion

            manifestPlaceholders += mutableMapOf(
                "pluginId" to pluginExtension.id.get(),
                "pluginName" to pluginExtension.name.get(),
                "pluginFullName" to pluginExtension.fullName,
                "pluginType" to pluginExtension.type.convention(MurglarPluginExtension.PluginType.MURGLAR).get(),
                "pluginEntryPointClass" to pluginExtension.entryPointClass.get(),
                "pluginVersion" to pluginExtension.version.get(),
                "pluginLibVersion" to Versions.murglarPluginsMajor
            )
        }

        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = pluginExtension.apkName
            }
        }
    }
}
