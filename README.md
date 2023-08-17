[![](https://jitpack.io/v/badmannersteam/murglar-plugins.svg)](https://jitpack.io/#badmannersteam/murglar-plugins)

# Murglar plugins

Murglar supports plugins, that add support for new music services, cloud drives, FTPs and other audio content sources.

Every plugin consists of 2 parts:
- **core** part - plugin implementation, that can be bundled to *.jar (for use in pure java env like desktop app/telegram bot/etc)
- **android** part - apk, that bundles core part for use with Murglar for Android

So this repo contains following parts of plugins system:
- [core](core) - main API module, that provides plugin interfaces and base implementation  
- [core-plugin-gradle-plugin](core-plugin-gradle-plugin) - Gradle plugin for developing core part
- [android-plugin-base](android-plugin-base) - android library project, which is used as base for android 
- [android-plugin-gradle-plugin](android-plugin-gradle-plugin) - Gradle plugin for developing android part

For sample and explanation check [murglar-plugin-sample](https://github.com/badmannersteam/murglar-plugin-sample).