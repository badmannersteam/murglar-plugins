# Murglar plugins

[![](https://jitpack.io/v/badmannersteam/murglar-plugins.svg)](https://jitpack.io/#badmannersteam/murglar-plugins)

Murglar supports plugins, that add support for the new music services, cloud drives, FTPs and other audio content
sources.

Every plugin consists of 2 parts:

- **core** part - plugin implementation, that can be bundled to *.jar (for use in pure java env like desktop
  app/telegram bot/etc)
- **android** part - apk, that bundles core part for use with Murglar on Android

This repo contains following parts of plugins system:

- [core](core) - main API module, that provides plugin interfaces and base implementations of these interfaces
- [core-plugin-gradle-plugin](core-plugin-gradle-plugin) - Gradle plugin for developing core part
- [android-plugin-base](android-plugin-base) - android library project, which is used as a base for android part
- [android-plugin-gradle-plugin](android-plugin-gradle-plugin) - Gradle plugin for developing android part

Gradle plugins configure the buildscripts for you (add required repositories, plugins and dependencies, set jar/apk
metadata, apk signing, r8, artifacts naming and other), so you don't have to worry about the bundling and configuring
the environment - just write the integration code.

## Versioning

Since the plugin code is loaded into the class-loader and then executed by the host application, we have to make sure
that the versions match.

Versioning of the `murglar-plugins` and plugins itself consists of 2 parts - `<major>.<minor>`:

- Major part increments by us if any binary breaking changes have been made and host app with the new
  major version can't load the plugin with the old major version. Plugin itself can't define/choose the major version,
  it's extracted from the Murglar plugins API and depends on its version.
- Minor part is the actual version of the plugin system (incremented by us) or plugin itself (incremented by the plugin
  author) when new features are added and no new breaking changes are presented.

So for the correct plugins execution their authors must depends on the latest version of this repo, at least on the
major part.

## Installation

> Replace all of the following `<plugin_name>` parts with the actual name of your plugin/service.

Create gradle project with 2 modules:

- `<plugin_name>-core`
- `<plugin_name>-android`

Add to the root `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()                                        // for kotlin gradle plugins
        google()                                                    // for android gradle plugin
        maven { url = java.net.URI.create("https://jitpack.io") }   // for murglar gradle plugins 
    }
    resolutionStrategy {
        eachPlugin {
            // workaround for requesting gradle plugins from plain maven repository (jitpack)
            // with `plugins {...}` block syntax
            val prefix = "murglar-gradle-plugin-"
            if (requested.id.id.startsWith(prefix)) {
                val artifactId = "${requested.id.id.substringAfter(prefix)}-plugin-gradle-plugin"
                useModule("com.github.badmannersteam.murglar-plugins:$artifactId:${requested.version}")
            }
        }
    }
}
```

Add to the `build.gradle.kts` of the `<plugin_name>-core` module:

```kotlin
plugins {
    id("murglar-gradle-plugin-core") version "<latest_version>"       // fix this to actual version
}

murglarPlugin {
    id = "sample"                                                     // your plugin id
    name = "Sample"                                                   // your plugin human readable name (in English)
    version = "1"                                                     // plugin version (minor)
    murglarClass = "com.badmanners.murglar.lib.sample.SampleMurglar"  // entry point - Murglar implementation class
}

dependencies {
    // Custom dependencies, if required.
    // Core plugin provides by default:
    // - kotlin-stdlib
    // - kotlin-test
    // - kotlinx-serialization-json
    // - threetenbp - java 8 time backport
    // - apache commons-text
}
```

Add to the `build.gradle.kts` of the `<plugin_name>-android` module:

```kotlin
plugins {
    id("murglar-gradle-plugin-android") version "<latest_version>"  // fix this to actual version
}

murglarAndroidPlugin {
    id = "sample"
    name = "Sample"
    version = 1
    murglarClass = "com.badmanners.murglar.lib.sample.SampleMurglar"
}

dependencies {
    implementation(project(":<plugin_name>-core"))  // fix this to your 'core' project name
}
```

### Android module additional requirements:

- Install [Android SDK](https://developer.android.com/studio) (if not installed).
- Create a copy of the `local.template.properties` with the name `local.properties` and place it at the root of your
  project.
- Place your `keystore.jks` with the android signing keys at the root of your project (or set the absolute path to the
  keystore in the `local.properties`). If you don't have one -
  [generate it](https://stackoverflow.com/questions/66981453/how-can-i-create-keystore-in-android-studio).
- Fix SDK path/keystore path/alias/passwords in the `local.properties`.

## Usage

1. Write your plugin integration code in the `<plugin_name>-core` module by implementing
   [Murglar](core/src/main/kotlin/com/badmanners/murglar/lib/core/service/Murglar.kt),
   [LoginResolver](core/src/main/kotlin/com/badmanners/murglar/lib/core/login/LoginResolver.kt) and
   [NodeResolver](core/src/main/kotlin/com/badmanners/murglar/lib/core/node/NodeResolver.kt) interfaces.
2. Complete `<plugin_name>-android` module with:
    - 24dp vector icon of the service in the `<plugin_name>-android/src/main/res/drawable/icon.xml`
    - `AndroidManifest.xml` in the `<plugin_name>-android/src/main/AndroidManifest.xml` - can be empty
   (with only `<manifest/>` tag) if your plugin doesn't support http/deeplinks or contain intent-filters otherwise:
    ```xml
    <manifest xmlns:android="http://schemas.android.com/apk/res/android">
        <application>
            <activity
                android:name="com.badmanners.murglar.plugin.LinkRedirectActivity"
                android:exported="true">
                <intent-filter>
                    <action android:name="android.intent.action.VIEW" />
                    <category android:name="android.intent.category.DEFAULT" />
                    <category android:name="android.intent.category.BROWSABLE" />
                    <data android:scheme="https" />
                    <data android:host="sample.com" />
                    <data android:host="www.sample.com" />
                    <data android:pathPattern="/artist/.*" />
                    <data android:pathPattern="/album/.*" />
                    <data android:pathPattern="/playlist/.*" />
                    <data android:pathPattern="/track/.*" />
                </intent-filter>
                <intent-filter>
                    <action android:name="android.intent.action.VIEW" />
                    <category android:name="android.intent.category.DEFAULT" />
                    <category android:name="android.intent.category.BROWSABLE" />
                    <data android:scheme="https" />
                    <data android:host="sample.page.link" />
                    <data android:pathPrefix="/" />
                </intent-filter>
            </activity>
    </application>
    </manifest>
    ```
3. Run `./gradlew clean build`.
4. Grab your plugins:
   - JAR - `<plugin_name>-core/build/libs/murglar-plugin-<plugin_name>-<version>.jar`
   - APK - `<plugin_name>-android/build/outputs/apk/release/murglar-plugin-<plugin_name>-<version>.apk`

## Sample

For complete sample and explanation see
[murglar-plugin-sample](https://github.com/badmannersteam/murglar-plugin-sample).