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
    exclude("org.jetbrains.kotlinx", "kotlinx-serialization-core")
    exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
    exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-bom")
    exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8")
    exclude("org.threeten", "threetenbp")
    exclude("org.apache.commons", "commons-text")
    exclude("me.xdrop", "fuzzywuzzy")
}

kotlin {
    jvmToolchain(17)
}

noArg {
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
    val version: Property<Int>
    val snapshot: Property<Boolean>
    val type: Property<PluginType>
    val entryPointClass: Property<String>

    val fullVersion
        get() = "${Versions.murglarPluginsMajor}.${version.get()}${if (snapshot.getOrElse(false)) "-SNAPSHOT" else ""}"

    enum class PluginType {
        MURGLAR, COVERS_PROVIDER, LYRICS_PROVIDER
    }
}

val pluginExtension = extensions.create<MurglarPluginExtension>("murglarPlugin")

afterEvaluate {
    if (!pluginExtension.id.isPresent)
        return@afterEvaluate

    version = pluginExtension.fullVersion

    val pluginType = pluginExtension.type.convention(MurglarPluginExtension.PluginType.MURGLAR)

    tasks.shadowJar {
        archiveBaseName = pluginExtension.id.map { "murglar-plugin-$it" }
        archiveVersion = pluginExtension.version.map { "${Versions.murglarPluginsMajor}.$it" }
        archiveClassifier = ""
        manifest.attributes.apply {
            set("Plugin-Id", pluginExtension.id)
            set("Plugin-Name", pluginExtension.name)
            set("Plugin-Type", pluginType.map(MurglarPluginExtension.PluginType::name))
            set("Plugin-Entry-Point-Class", pluginExtension.entryPointClass)
            set("Plugin-Version", pluginExtension.version.map(Int::toString))
            set("Plugin-Lib-Version", Versions.murglarPluginsMajor)
        }
    }

    if (pluginType.get() == MurglarPluginExtension.PluginType.MURGLAR)
        tasks.named("check") {
            doFirst {
                val iconFile = project.file("src/main/resources/icon.xml")
                check(iconFile.exists()) {
                    "No plugin icon found (${iconFile.path})!"
                }
            }
        }
}

