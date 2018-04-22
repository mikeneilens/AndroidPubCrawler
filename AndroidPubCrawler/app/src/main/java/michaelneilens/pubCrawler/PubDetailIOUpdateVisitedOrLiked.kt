package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONObject

class PubDetailIOUpdateVisitedOrLiked(val requestDescription:String):WebService.WebServiceRequester {

    private var requester: PubDetailRequester?

    init {
        requester = null
    }

    fun makeRequestForVisited(pub:PubDetail, newRequester: PubDetailRequester) {
        requester = newRequester
        update(pub.changeVisitedService)
    }

    fun makeRequestForLiked(pub:PubDetail, newRequester: PubDetailRequester) {
        requester = newRequester
        update(pub.changeLikedService)
    }

    private fun update(url:String) {
        val urlRequestHttp = url.replace("https","http")
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val receivedPub = PubDetail(json)
            requester?.receivedNew(receivedPub, noOfRowsChanged = false)
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