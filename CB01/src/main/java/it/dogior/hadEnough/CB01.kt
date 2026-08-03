package it.dogior.hadEnough

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class CB01 : MainAPI() {
    override var name = "CB01"
    override var mainUrl = "https://cb01.pe" // O l'indirizzo aggiornato del sito
    override var lang = "it"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    // Aggiungi qui la logica di scraping per getMainPage, search e load
}
