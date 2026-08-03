package it.dogior.hadEnough

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import it.dogior.hadEnough.extractors.MixDropExtractor

class CB01 : MainAPI() {
    override var name = "CB01"
    override var mainUrl = "https://cb01uno.bond"
    override var lang = "it"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/page/$page/").document
        val home = document.select("div.item").mapNotNull { element ->
            val title = element.select("div.title a").text()
            val href = element.select("div.title a").attr("href")
            val posterUrl = element.select("div.image img").attr("src")
            val isMovie = !href.contains("serie")
            
            if (isMovie) {
                newMovieSearchResponse(title, href, TvType.Movie) {
                    this.posterUrl = posterUrl
                }
            } else {
                newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                    this.posterUrl = posterUrl
                }
            }
        }
        return newHomePageResponse(listOf(HomePageList("Film e Serie TV", home)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("div.item").mapNotNull { element ->
            val title = element.select("div.title a").text()
            val href = element.select("div.title a").attr("href")
            val posterUrl = element.select("div.image img").attr("src")
            val isMovie = !href.contains("serie")

            if (isMovie) {
                newMovieSearchResponse(title, href, TvType.Movie) {
                    this.posterUrl = posterUrl
                }
            } else {
                newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                    this.posterUrl = posterUrl
                }
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document
        val title = document.select("h1.title").text()
        val poster = document.select("div.poster img").attr("src")
        val description = document.select("div.desc").text()
        val isMovie = !url.contains("serie")

        val episodes = if (isMovie) {
            listOf(newEpisode(url) {
                this.name = title
            })
        } else {
            document.select("div.episodelist a").map {
                val epName = it.text()
                val epUrl = it.attr("href")
                newEpisode(epUrl) {
                    this.name = epName
                }
            }
        }

        return if (isMovie) {
            newMovieLoadResponse(title, url, TvType.Movie, episodes) {
                this.posterUrl = poster
                this.plot = description
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = description
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        val embedUrl = document.select("iframe").attr("src")

        if (embedUrl.contains("mixdrop")) {
            MixDropExtractor().getUrl(embedUrl, data, subtitleCallback, callback)
        }
        return true
    }
}
