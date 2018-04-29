package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.PubHygieneRatingRequester
import org.json.JSONObject

class PubDetailIOGetHygieneRatings(val requestDescription:String):WebService.WebServiceRequester {

    var requester: PubHygieneRatingRequester?
    val webService = WebService()

    init {
        requester = null
    }

    fun makeRequest(urlRequest:String, newRequester:PubHygieneRatingRequester) {
        println(urlRequest)
        requester = newRequester
        val urlRequestHttp = urlRequest.replace("https","http")
        webService.createRequest(urlRequestHttp)
        webService.execute( this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val receivedHygieneRatings = createListFrom(json)
            requester?.receivedNew(receivedHygieneRatings)
        }
    }


    override fun requestFailed(e: Exception) {
        if (e is WebServiceException.InvalidRequest)
            println("pc Invalid request in IOGetHygieneRating")
        else
            println("Error in IOGetHygieneRating")
    }

   fun cancelRequest() {
       println("pc request cancelled $requestDescription")
        requester = null
    }

    private fun createListFrom(json:JSONObject):List<PubHygieneRating> {
        val pubHygieneRatings:ArrayList<PubHygieneRating> = arrayListOf()

        val jsonArray = json.getJSONArray("FoodHygieneRatings")
        for (i in 0..(jsonArray.length() - 1)) {
            val pubHygieneRating = PubHygieneRating(jsonArray.getJSONObject(i))
            pubHygieneRatings.add(pubHygieneRating)
        }
        return pubHygieneRatings
    }

}