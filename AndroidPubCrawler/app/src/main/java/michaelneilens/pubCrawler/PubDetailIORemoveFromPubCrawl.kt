package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONException
import org.json.JSONObject

class PubDetailIORemoveFromPubCrawl(val requestDescription:String):WebService.WebServiceRequester {

    var requester: PubDetailRequester?

    init {
        requester = null
    }
    fun makeRequest(pub:PubDetail, ndx:Int, newRequester: PubDetailRequester) {
        requester = newRequester
        delete(pub.listOfPubCrawls[ndx].removePubCrawlService)
    }

    private fun delete(url:String) {
        println("Updating visited $url")
        val urlRequestHttp = url.replace("https","http")
        val request = WebServiceRequest(urlRequestHttp)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val messageJSON =  try{ json.getJSONObject("Message") } catch(e: JSONException) { JSONObject() }
        val status = try{ messageJSON.getInt("Status") } catch(e: JSONException) {-999}
        val text = try{ messageJSON.getString("Text") } catch(e: JSONException) {""}
        if (status == 0) {
            val receivedPub = PubDetail(json)
            requester?.receivedNew(receivedPub, noOfRowsChanged = true)
        } else {
            requester?.requestFailed(requestDescription,text)
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