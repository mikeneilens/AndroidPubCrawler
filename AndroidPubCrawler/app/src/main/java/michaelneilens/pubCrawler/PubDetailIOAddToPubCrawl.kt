package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONObject

class PubDetailIOAddToPubCrawl(val requestDescription:String):WebService.WebServiceRequester  {

    var requester: PubDetailRequester?

    init {
        requester = null
    }

    fun makeRequestNewPubCrawl(pub:PubDetail, name:String, newRequester: PubDetailRequester) {
        requester = newRequester
        val url = pub.createPubCrawlService
        val urlWithName = url + name
        addToPubCrawl(urlWithName)
    }
    fun makeRequest(pub:PubDetail, ndx:Int, newRequester: PubDetailRequester) {
        requester = newRequester
        val urlRequest = pub.listOfOtherPubCrawls[ndx].addPubCrawlService
        addToPubCrawl(urlRequest)
    }

    private fun addToPubCrawl(urlRequest:String) {
        println(urlRequest)
        val urlRequestHttp = urlRequest.replace("https","http")
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val receivedPub = PubDetail(json)
            requester?.receivedNew(receivedPub, noOfRowsChanged = true)
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