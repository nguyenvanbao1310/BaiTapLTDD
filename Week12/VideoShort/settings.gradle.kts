pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.1.3"
        id("org.jetbrains.kotlin.android") version "1.9.0" // Thêm plugin Kotlin
        id("com.google.gms.google-services") version "4.4.0"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Thêm repository cho Firebase UI nếu cần
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "VideoShort"
include(":app")