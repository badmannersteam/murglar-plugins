plugins {
    alias(libs.plugins.android.library.plugin)
    `maven-publish`
}

android {
    namespace = "com.badmanners.murglar.plugin"

    compileSdk = libs.versions.target.sdk.get().toInt()
    buildToolsVersion = libs.versions.build.tools.get()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        consumerProguardFile("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testVariants.all {
        mergedFlavor.manifestPlaceholders += listOf(
            "pluginId", "pluginName", "pluginFullName", "pluginMurglarClass", "pluginVersion", "pluginLibVersion"
        ).associateWith { "" }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}
