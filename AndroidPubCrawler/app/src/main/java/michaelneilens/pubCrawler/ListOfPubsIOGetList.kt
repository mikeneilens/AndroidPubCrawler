package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class ListOfPubsIOGetList(requestDescription:String):AbstractIO<ListOfPubsRequester>(requestDescription) {

    fun makeRequest(search:String, userSetting:UserSetting, requester: ListOfPubsRequester) {
        val urlRequest = "http://www.api.neilens.co.uk/ListOfPubs/?search=" + search + userSetting.queryParms()
        getListOfPubsForUrl(urlRequest, requester)
    }

    fun makeRequestForLocation(lat:Double, lng:Double, userSetting:UserSetting, requester: ListOfPubsRequester) {
        val urlRequest = "http://www.api.neilens.co.uk/ListOfPubs/?search=nearby&lat=" + lat +  "&lng=" +lng + userSetting.queryParms()
        getListOfPubsForUrl(urlRequest, requester)
    }

    fun makeRequestForPubCrawl(pubCrawl:PubCrawl, requester: ListOfPubsRequester) {
        val urlRequest = pubCrawl.pubsService.replace("https://","http://")
        getListOfPubsForUrl(urlRequest, requester)
    }

    private fun getListOfPubsForUrl(urlRequest:String, newRequester:ListOfPubsRequester) {
        requester = newRequester
        executeRequest(urlRequest)
    }

    override fun responseOK(json: JSONObject) {
        val receivedListOfPubs = ListOfPubs(json)
        requester?.receivedNew(receivedListOfPubs)
    }

}