package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class PubCrawlIOUpdateSequence(val requestDescription:String):WebService.WebServiceRequester {

    private var requester: ListOfPubsRequester?

    init {
        requester = null
    }

    fun makeRequest(pubCrawl:PubCrawl, listOfPubs: ListOfPubs, newRequester: ListOfPubsRequester) {
        requester = newRequester
        val csv = createCSV(listOfPubs)

        val urlRequest = pubCrawl.sequencePubsService.replace("https://","http://") + csv
        saveForUrl(urlRequest)
    }

    private fun createCSV(listOfPubs:ListOfPubs):String {
        var csv=""
        for (i in 0..(listOfPubs.pubs.size -1)) {
            for (j in 0..(listOfPubs.pubs.size -1)) {
                if (listOfPubs.pubs[j].sequence == i)  {
                    if  (csv != "")  {
                        csv += ","
                    }
                    csv += j.toString()
                }
            }
        }
        return csv
    }

    private fun saveForUrl(urlRequest:String) {
        println("urlRequest: $urlRequest")
        val request = WebServiceRequest(urlRequest)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val message = WebServiceMessage(json)

        if (message.status == 0) {
            val listOfPubs = ListOfPubs(json)
            requester?.receivedNew(listOfPubs)
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