import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import java.net.URI


plugins {
    `java-library`
    id("kotlin")
    id("kotlin-noarg")
    id("kotlinx-serialization")
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
    "compileOnly"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    "compileOnly"("com.github.badmannersteam.murglar-plugins:core:${Versions.murglarPlugins}")
    "testImplementation"("org.jetbrains.kotlin:kotlin-test")
    "testImplementation"("com.github.badmannersteam.murglar-plugins:core:${Versions.murglarPlugins}")
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

    tasks.withType<Jar> {
        archiveBaseName.set(pluginExtension.id.map { "murglar-plugin-$it" })
        manifest.attributes.apply {
            set("Plugin-Id", pluginExtension.id)
            set("Plugin-Name", pluginExtension.name)
            set("Plugin-Murglar-Class", pluginExtension.murglarClass)
            set("Plugin-Version", pluginExtension.version.map { it.substringBefore('-') })
            set("Plugin-Lib-Version", Versions.murglarPluginsMajor)
        }
    }
}

