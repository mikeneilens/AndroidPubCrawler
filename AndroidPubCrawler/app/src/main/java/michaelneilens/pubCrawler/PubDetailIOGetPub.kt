package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONException
import org.json.JSONObject

class PubDetailIOGetPub(val requestDescription:String):WebService.WebServiceRequester {

    var requester: PubDetailRequester?

    init {
        requester = null
    }

    fun makeRequest(urlRequest:String, newRequester:PubDetailRequester) {
        println(urlRequest)
        requester = newRequester
        val urlRequestHttp = urlRequest.replace("https","http")
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            println("pc request completed $requestDescription")
            val receivedPub = PubDetail(json)
            requester?.receivedNew(receivedPub)
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