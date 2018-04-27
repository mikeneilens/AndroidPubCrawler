package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class PubCrawlIORemovePub (requestDescription:String):AbstractIO<ListOfPubsRequester>(requestDescription) {

    fun makeRequest(pub: PubDetail ,newRequester: ListOfPubsRequester) {
        requester = newRequester
        val urlRequest = pub.removePubService.replace("https://","http://")
        deleteForUrl(urlRequest)
    }

    private fun deleteForUrl(urlRequest:String) {
        println("urlRequest: $urlRequest")
        executeRequest(urlRequest)
    }

    override fun responseOK(json: JSONObject) {
        val receivedListOfPubs = ListOfPubs(json)
        requester?.receivedNew(receivedListOfPubs)
    }

}