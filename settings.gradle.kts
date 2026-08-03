pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

plugins {
    id("com.lagradost.cloudstream3.gradle") version "32895ae"
}

rootProject.name = "nuanstream"
include(":CB01")
