package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubCrawlPublicRequester
import org.json.JSONException
import org.json.JSONObject

class PubCrawlIOGetEmailText(val requestDescription:String): WebService.WebServiceRequester {

    private var requester: PubCrawlPublicRequester?

    init {
        requester = null
    }

    fun makeRequest(pubCrawl:PubCrawl, newRequester: PubCrawlPublicRequester) {
        requester = newRequester
        val url = pubCrawl.emailTextService
        val urlRequestHttp = url.replace("https","http")
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val htmlText = try {
                json.getString("EmailText")
            } catch (e: JSONException) {
                ""
            }
            val plainText = try {
                json.getString("PlainText")
            } catch (e: JSONException) {
                ""
            }
            requester?.obtainedEmailText(htmlText, plainText)
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