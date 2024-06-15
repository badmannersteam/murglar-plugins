rootProject.name = "murglar-plugins"

include("core")
include("core-plugin-gradle-plugin")
include("android-plugin-base")
include("android-plugin-gradle-plugin")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        repositories {
            mavenLocal()
            gradlePluginPortal()
            google()
        }
        create("libs") {
            version("murglar-plugins", "4.2")

            // 1.5+ has this - https://github.com/Kotlin/kotlinx.serialization/issues/2231
            version("kotlinx-json", "1.4.1")
            version("time-backport", "1.6.8")
            // 1.9 uses java 8 features, don't update
            version("commons-text", "1.8")

            version("kotlin", "1.8.22")
            version("android-build-plugin", "8.1.1")
            version("buildconfig", "3.1.0")

            version("min-sdk", "21")
            version("target-sdk", "33")
            version("build-tools", "33.0.2")

            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8").versionRef("kotlin")
            library("kotlinx-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("kotlinx-json")
            library("time-backport", "org.threeten", "threetenbp").versionRef("time-backport")
            library("commons-text", "org.apache.commons", "commons-text").versionRef("commons-text")

            library("kotlin-gradle-plugin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
            library("kotlin-noarg", "org.jetbrains.kotlin", "kotlin-noarg").versionRef("kotlin")
            library("kotlin-serialization", "org.jetbrains.kotlin", "kotlin-serialization").versionRef("kotlin")
            library("android-build-plugin", "com.android.tools.build", "gradle").versionRef("android-build-plugin")

            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-noarg", "org.jetbrains.kotlin.plugin.noarg").versionRef("kotlin")
            plugin("buildconfig", "com.github.gmazzo.buildconfig").versionRef("buildconfig")
            plugin("android-library-plugin", "com.android.library").versionRef("android-build-plugin")
        }
    }
}
