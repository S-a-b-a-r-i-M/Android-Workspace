pluginManagement {
    repositories {
        gradlePluginPortal()  // ← where kotlin("jvm") lives
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "newcodegen"