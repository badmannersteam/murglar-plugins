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
            mavenCentral()
            gradlePluginPortal()
            google()
        }
        create("libs") {
            version("murglar-plugins", "5.0-SNAPSHOT")

            version("kotlin", "2.1.21")
            version("android-build-plugin", "8.10.1")

            version("min-sdk", "21")
            version("target-sdk", "35")
            version("build-tools", "35.0.0")

            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
            library("kotlinx-coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.10.2")
            library("kotlinx-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.8.1")
            library("time-backport", "org.threeten", "threetenbp").version("1.7.1")
            // 1.9 uses java 8 features, don't update
            library("commons-text", "org.apache.commons", "commons-text").version("1.8")
            library("fuzzywuzzy", "me.xdrop", "fuzzywuzzy").version("1.4.0")

            library("kotlin-gradle-plugin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
            library("kotlin-noarg", "org.jetbrains.kotlin", "kotlin-noarg").versionRef("kotlin")
            library("kotlin-serialization", "org.jetbrains.kotlin", "kotlin-serialization").versionRef("kotlin")
            library("android-build-plugin", "com.android.tools.build", "gradle").versionRef("android-build-plugin")
            library("shadow", "com.gradleup.shadow", "shadow-gradle-plugin").version("8.3.6")

            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-noarg", "org.jetbrains.kotlin.plugin.noarg").versionRef("kotlin")
            plugin("buildconfig", "com.github.gmazzo.buildconfig").version("5.5.4")
            plugin("android-library-plugin", "com.android.library").versionRef("android-build-plugin")
        }
    }
}
