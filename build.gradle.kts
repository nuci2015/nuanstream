plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.lagradost.cloudstream3.gradle") version "1.0.9"
}

subprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

allprojects {
    afterEvaluate {
        project.extensions.findByName("android")?.let { android ->
            android.compileSdkVersion(34)
            android.defaultConfig {
                minSdk(21)
                targetSdk(34)
            }
            android.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}
