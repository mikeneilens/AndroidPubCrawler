package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONObject

class PubDetailIOAddToPubCrawl(requestDescription:String):AbstractIO<PubDetailRequester>(requestDescription)  {

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

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        val receivedPub = PubDetail(json)
        requester?.receivedNew(receivedPub, noOfRowsChanged = true)
    }


}