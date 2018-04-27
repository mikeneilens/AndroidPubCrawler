package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlPublicRequester
import org.json.JSONException
import org.json.JSONObject

class PubCrawlIOCopy(requestDescription:String):AbstractIO<PubCrawlPublicRequester>(requestDescription) {

    fun makeRequest(pubCrawl:PubCrawl,newRequester: PubCrawlPublicRequester) {
        requester = newRequester
        val url = pubCrawl.copyService
        val urlRequestHttp = url.replace("https","http")

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        requester?.pubCrawlCopied()
    }

}