package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlUpdateRequester
import org.json.JSONObject

class PubCrawlIODelete(requestDescription:String):AbstractIO<PubCrawlUpdateRequester>(requestDescription) {

    fun makeRequest(pubCrawl:PubCrawl, newRequester:PubCrawlUpdateRequester) {
        requester = newRequester
        println("deleting pub crawl" + pubCrawl.removeService)
        val url = pubCrawl.removeService
        val urlRequestHttp = url.replace("https","http")

        executeRequest(urlRequestHttp)
     }

    override fun responseOK(json: JSONObject) {
        requester?.pubCrawlDeleted()
    }
}