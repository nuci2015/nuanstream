plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

cloudstream {
    description = "Provider CB01"
    authors = listOf("Nunciè")
    iconUrl = ""
    language = "it"
    tvTypes = listOf("TvSeries", "Movie", "Anime")
    status = 3
    apiVersion = 3
}

dependencies {
    implementation(kotlin("stdlib"))
}
