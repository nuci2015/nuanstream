import com.lagradost.cloudstream3.gradle.CloudstreamExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(plugin = "com.lagradost.cloudstream3.gradle")

android {
    namespace = "com.lagradost.cloudstream3.plugins.cb01"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
        freeCompilerArgs.addAll(
            "-Xno-call-assertions",
            "-Xno-param-assertions",
            "-Xno-receiver-assertions"
        )
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("com.github.Blatzar:NiceHttp:0.4.11")
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
}
