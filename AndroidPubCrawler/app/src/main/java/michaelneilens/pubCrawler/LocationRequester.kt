package michaelneilens.pubCrawler

import android.location.Location

interface LocationRequester {
    fun processNewLocation(location: Location?)
    fun processLocationAccessDenied()
}