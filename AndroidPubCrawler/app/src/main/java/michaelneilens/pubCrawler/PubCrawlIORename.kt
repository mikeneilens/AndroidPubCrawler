package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlUpdateRequester
import org.json.JSONObject

class PubCrawlIORename(requestDescription:String):AbstractIO<PubCrawlUpdateRequester>(requestDescription) {

    fun makeRequest(pubCrawl:PubCrawl, name:String, newRequester: PubCrawlUpdateRequester) {
        requester = newRequester
        val url = pubCrawl.updateService + name
        val urlRequestHttp = url.replace("https","http")

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        requester?.pubCrawlUpdated()
    }

}