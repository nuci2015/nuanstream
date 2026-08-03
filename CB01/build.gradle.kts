plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

cloudstream {
    setEnglish(false)
    description = "Provider CB01"
    authors = arrayOf("Nunciè")
    iconUrl = ""
    language = "it"
    tvTypes = arrayOf("TvSeries", "Movie", "Anime")
    status = 3
    apiVersion = 3
}

dependencies {
    implementation(kotlin("stdlib"))
}
