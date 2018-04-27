package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlPublicRequester
import org.json.JSONObject

class PubCrawlIOAddToFavourites(requestDescription:String):AbstractIO<PubCrawlPublicRequester>(requestDescription) {

    fun makeRequest(pubCrawl:PubCrawl, newRequester:PubCrawlPublicRequester) {
        requester = newRequester
        val url = pubCrawl.addUserService
        val urlRequestHttp = url.replace("https","http")

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        requester?.pubCrawlAddedToFavourites()
    }
}