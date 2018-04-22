package michaelneilens.pubCrawler

/**
 * Created by michaelneilens on 13/03/2018.
 */
import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class ListOfPubsIOGetListMore(val requestDescription:String):WebService.WebServiceRequester {

    private var originalListOfPubs:ListOfPubs= ListOfPubs()
    private var requester:ListOfPubsRequester?
    private val URL_SCHEME = "http"

    init {
        requester = null
    }

    fun makeRequest(listOfPubs:ListOfPubs, newRequester: ListOfPubsRequester) {
        requester = newRequester
        originalListOfPubs = listOfPubs
        val urlRequest = originalListOfPubs.morePubsService.replace("https", URL_SCHEME)

        val request = WebServiceRequest(urlRequest)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val receivedListOfPubs = ListOfPubs(json)
            val newListOfPubs = originalListOfPubs.plus(receivedListOfPubs)
            requester?.receivedNew(newListOfPubs)
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