package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import org.json.JSONObject

class PubDetailIOUpdateVisitedOrLiked(requestDescription:String):AbstractIO<PubDetailRequester>(requestDescription) {

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
        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        val receivedPub = PubDetail(json)
        requester?.receivedNew(receivedPub, noOfRowsChanged = false)
    }

}