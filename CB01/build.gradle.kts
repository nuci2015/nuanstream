plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

cloudstream {
    description = "Provider CB01"
    authors = listOf("Nunciè")
    iconUrl = ""
    language = "it"
    tvTypes = listOf("TvSeries", "Movie", "Anime")
    status = 3
}

dependencies {
    implementation(kotlin("stdlib"))
}
