plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.noarg)
    implementation(libs.kotlin.serialization)
}

