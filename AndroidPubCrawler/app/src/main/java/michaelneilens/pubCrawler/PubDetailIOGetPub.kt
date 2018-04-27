package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONException
import org.json.JSONObject

class PubDetailIOGetPub(requestDescription:String):AbstractIO<PubDetailRequester>(requestDescription) {

    fun makeRequest(urlRequest:String, newRequester:PubDetailRequester) {
        println(urlRequest)
        requester = newRequester
        val urlRequestHttp = urlRequest.replace("https","http")

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        println("pc request completed $requestDescription")
        val receivedPub = PubDetail(json)
        requester?.receivedNew(receivedPub)
    }

}