plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.noarg)
    implementation(libs.kotlin.allopen)
    implementation(libs.kotlin.serialization)
    implementation(libs.shadow)
}

