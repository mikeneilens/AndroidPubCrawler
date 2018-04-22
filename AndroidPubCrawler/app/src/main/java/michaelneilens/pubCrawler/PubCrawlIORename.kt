package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlUpdateRequester
import org.json.JSONObject

class PubCrawlIORename(val requestDescription:String):WebService.WebServiceRequester {

    private var requester: PubCrawlUpdateRequester?

    init {
        requester = null
    }

    fun makeRequest(pubCrawl:PubCrawl, name:String, newRequester: PubCrawlUpdateRequester) {
        requester = newRequester
        val url = pubCrawl.updateService + name
        val urlRequestHttp = url.replace("https","http")
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            requester?.pubCrawlUpdated()
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