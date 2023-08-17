plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.noarg)
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    api(libs.kotlinx.json)
    api(libs.time.backport)
    api(libs.commons.text)
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
}

noArg {
    annotation("com.badmanners.murglar.lib.core.utils.contract.Model")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            from(components["java"])
        }
    }
}