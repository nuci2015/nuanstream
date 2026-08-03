plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.lagradost.cloudstream3.plugins.cb01"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        // targetSdk rimosso da qui: nelle library è deprecato/ignorato,
        // viene comunque ereditato dal compileSdk in fase di build
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

cloudstream {
    description = "Provider CB01 - Film e Serie TV in italiano"
    authors = listOf("Nunciè")
    iconUrl = "https://www.google.com/s2/favicons?domain=cb01.uno&sz=%size%"
    language = "it"
    tvTypes = listOf("TvSeries", "Movie", "Anime")
    status = 1  // 1 = attivo/funzionante, 3 = beta/instabile
}

dependencies {
    implementation(kotlin("stdlib"))
}
