@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
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
    "compileOnly"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    "compileOnly"("com.github.badmannersteam.murglar-plugins:core:${Versions.murglarPlugins}")
    "implementation"("com.github.badmannersteam.murglar-plugins:android-plugin-base:${Versions.murglarPlugins}@aar")
}


configure<BaseAppModuleExtension> {
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
        val config = this@configure.signingConfigs["signingConfig"]
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

configure<KotlinProjectExtension> {
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
    val murglarClass: Property<String>

    val appId get() = "com.badmanners.murglar.plugin.${id.get()}"
    val fullName get() = "Murglar plugin for ${name.get()}"
    val fullVersion get() = "${Versions.murglarPluginsMajor}.${version.get()}"
    val apkName get() = "murglar-plugin-${id.get()}-${fullVersion}.apk"
}

val pluginExtension = extensions.create<MurglarPluginExtension>("murglarAndroidPlugin")

val currentProjectName: String = project.name
gradle.afterProject {
    if (name != currentProjectName)
        return@afterProject

    configure<BaseAppModuleExtension> {
        namespace = pluginExtension.appId

        defaultConfig {
            applicationId = pluginExtension.appId

            versionCode = pluginExtension.version.get()
            versionName = pluginExtension.fullVersion

            manifestPlaceholders += mutableMapOf(
                "pluginId" to pluginExtension.id.get(),
                "pluginName" to pluginExtension.name.get(),
                "pluginFullName" to pluginExtension.fullName,
                "pluginMurglarClass" to pluginExtension.murglarClass.get(),
                "pluginVersion" to pluginExtension.version.get(),
                "pluginLibVersion" to Versions.murglarPluginsMajor
            )
        }

        applicationVariants.all {
            outputs.all {
                (this as ApkVariantOutput).outputFileName = pluginExtension.apkName
            }
        }
    }
}
