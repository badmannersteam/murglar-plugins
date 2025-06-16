@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.buildconfig)
}

subprojects {
    val versions = rootProject.libs.versions

    group = "com.github.badmannersteam.murglar-plugins"
    version = versions.murglar.plugins.get()

    apply(plugin = rootProject.libs.plugins.buildconfig.get().pluginId)

    buildConfig {
        packageName = ""
        className = "Versions"
        useJavaOutput()

        buildConfigField("String", "murglarPlugins", "\"${versions.murglar.plugins.get()}\"")
        buildConfigField("int", "murglarPluginsMajor", versions.murglar.plugins.get().substringBefore('.'))
        buildConfigField("int", "minSdk", versions.min.sdk.get())
        buildConfigField("int", "targetSdk", versions.target.sdk.get())
        buildConfigField("String", "buildTools", "\"${versions.build.tools.get()}\"")
    }
}