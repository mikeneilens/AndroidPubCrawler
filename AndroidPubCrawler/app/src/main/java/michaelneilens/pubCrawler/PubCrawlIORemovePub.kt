package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class PubCrawlIORemovePub (val requestDescription:String):WebService.WebServiceRequester {

    private var requester: ListOfPubsRequester?

    init {
        requester = null
    }

    fun makeRequest(pub: PubDetail ,newRequester: ListOfPubsRequester) {
        requester = newRequester
        val urlRequest = pub.removePubService.replace("https://","http://")
        deleteForUrl(urlRequest)
    }

    private fun deleteForUrl(urlRequest:String) {
        println("urlRequest: $urlRequest")
        val request = WebServiceRequest(urlRequest)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val receivedListOfPubs = ListOfPubs(json)
            requester?.receivedNew(receivedListOfPubs)
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