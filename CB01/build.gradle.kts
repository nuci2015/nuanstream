import com.lagradost.cloudstream3.gradle.CloudstreamExtension

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(plugin = "com.lagradost.cloudstream3.gradle")

android {
    namespace = "com.lagradost.cloudstream3.plugins.cb01"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xno-call-assertions"
    }
}

configure<CloudstreamExtension> {
    description = "Provider CB01 - Film e Serie TV in italiano"
    authors = listOf("Nunciè")
    iconUrl = "https://www.google.com/s2/favicons?domain=cb01.uno&sz=%size%"
    language = "it"
    tvTypes = listOf("TvSeries", "Movie", "Anime")
    status = 1
}

dependencies {
    val cloudstream by configurations
    val implementation by configurations

    cloudstream("com.lagradost:cloudstream3:pre-release")

    implementation(kotlin("stdlib"))
    implementation("com.github.Blatzar:NiceHttp:0.4.11")
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
}
