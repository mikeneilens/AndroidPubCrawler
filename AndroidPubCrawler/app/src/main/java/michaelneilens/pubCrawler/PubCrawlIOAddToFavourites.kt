package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlPublicRequester
import org.json.JSONObject

class PubCrawlIOAddToFavourites(val requestDescription:String):WebService.WebServiceRequester {

    private var requester: PubCrawlPublicRequester?

    init {
        requester = null
    }

    fun makeRequest(pubCrawl:PubCrawl, newRequester:PubCrawlPublicRequester) {
        requester = newRequester
        val url = pubCrawl.addUserService
        val urlRequestHttp = url.replace("https","http")

        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            requester?.pubCrawlAddedToFavourites()
        } else {
            requester?.requestFailed(requestDescription, message.text)
        }
    }

    override fun requestFailed(e: Exception) {
        if (e is WebServiceException.InvalidRequest)
            requester?.requestFailed(requestDescription,"Invalid request")
        else
            requester?.requestFailed(requestDescription,"")
    }
    fun cancelRequest() {
        requester = null
    }
}