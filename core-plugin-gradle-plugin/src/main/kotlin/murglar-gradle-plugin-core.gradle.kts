import gradle.kotlin.dsl.accessors._593230ccf66f690bcbf89c0aa3add4af.build
import gradle.kotlin.dsl.accessors._593230ccf66f690bcbf89c0aa3add4af.runtimeClasspath
import gradle.kotlin.dsl.accessors._593230ccf66f690bcbf89c0aa3add4af.shadowJar
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import java.net.URI


plugins {
    `java-library`
    id("kotlin")
    id("kotlin-noarg")
    id("kotlinx-serialization")
    id("com.gradleup.shadow")
}

group = "com.github.badmannersteam.murglar-plugins"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = URI.create("https://jitpack.io") }
}

configurations.configureEach {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {
    "compileOnly"("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    "compileOnly"("com.github.badmannersteam.murglar-plugins:core:${Versions.murglarPlugins}")
    "testImplementation"("org.jetbrains.kotlin:kotlin-test")
    "testImplementation"("com.github.badmannersteam.murglar-plugins:core:${Versions.murglarPlugins}")
}

configurations.runtimeClasspath {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    exclude("org.jetbrains.kotlinx", "kotlinx-serialization-json")
    exclude("org.threeten", "threetenbp")
    exclude("org.apache.commons", "commons-text")
}

configure<KotlinProjectExtension> {
    jvmToolchain(17)
}

configure<NoArgExtension> {
    annotation("com.badmanners.murglar.lib.core.utils.contract.Model")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

interface MurglarPluginExtension {
    val id: Property<String>
    val name: Property<String>
    val version: Property<String>
    val murglarClass: Property<String>
}

val pluginExtension = extensions.create<MurglarPluginExtension>("murglarPlugin")

afterEvaluate {
    if (!pluginExtension.id.isPresent)
        return@afterEvaluate

    version = "${Versions.murglarPluginsMajor}.${pluginExtension.version.get()}"

    tasks.shadowJar {
        archiveBaseName = pluginExtension.id.map { "murglar-plugin-$it" }
        archiveClassifier = ""
        manifest.attributes.apply {
            set("Plugin-Id", pluginExtension.id)
            set("Plugin-Name", pluginExtension.name)
            set("Plugin-Murglar-Class", pluginExtension.murglarClass)
            set("Plugin-Version", pluginExtension.version.map { it.substringBefore('-') })
            set("Plugin-Lib-Version", Versions.murglarPluginsMajor)
        }
    }
}

