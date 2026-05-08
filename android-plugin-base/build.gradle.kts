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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

androidComponents {
    onVariants { variant ->
        variant.androidTest?.manifestPlaceholders?.putAll(
            listOf(
                "pluginId", "pluginName", "pluginFullName", "pluginType",
                "pluginEntryPointClass", "pluginVersion", "pluginLibVersion"
            ).associateWith { "" }
        )
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
