package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class ListOfPubsIOGetList(val requestDescription:String):WebService.WebServiceRequester {

    var requester: ListOfPubsRequester? = null

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
        val request = WebServiceRequest(url = urlRequest)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val receivedListOfPubs = ListOfPubs(json)
            requester?.receivedNew(receivedListOfPubs)
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