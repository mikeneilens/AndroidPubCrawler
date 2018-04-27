package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.IORequester
import org.json.JSONObject

abstract class AbstractIO<ioRequester:IORequester>(val requestDescription:String):WebService.WebServiceRequester {

    var requester:ioRequester? = null
    protected val URL_SCHEME = "http"

    init {
        requester = null
    }

    fun executeRequest(urlRequestHttp:String) {
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    fun cancelRequest() {
        requester = null
    }

    final override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            responseOK(json)
        } else {
            requester?.requestFailed(requestDescription, message.text)
        }
    }

    final override fun requestFailed(e: Exception) {
        if (e is WebServiceException.InvalidRequest)
            requester?.requestFailed(requestDescription,"Invalid request")
        else
            requester?.requestFailed(requestDescription,"")
    }

    abstract fun responseOK(json:JSONObject)
}