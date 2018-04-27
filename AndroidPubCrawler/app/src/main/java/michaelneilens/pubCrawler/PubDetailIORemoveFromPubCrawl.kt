package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONException
import org.json.JSONObject

class PubDetailIORemoveFromPubCrawl(requestDescription:String):AbstractIO<PubDetailRequester>(requestDescription) {

    fun makeRequest(pub:PubDetail, ndx:Int, newRequester: PubDetailRequester) {
        requester = newRequester
        delete(pub.listOfPubCrawls[ndx].removePubCrawlService)
    }

    private fun delete(url:String) {
        println("Updating visited $url")
        val urlRequestHttp = url.replace("https","http")

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        val receivedPub = PubDetail(json)
        requester?.receivedNew(receivedPub, noOfRowsChanged = true)
    }

}