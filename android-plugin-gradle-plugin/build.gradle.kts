plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.build.plugin)
}
