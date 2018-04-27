package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class PubCrawlIOUpdateSequence(requestDescription:String):AbstractIO<ListOfPubsRequester>(requestDescription) {

    fun makeRequest(pubCrawl:PubCrawl, listOfPubs: ListOfPubs, newRequester:ListOfPubsRequester) {
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
        executeRequest(urlRequest)
    }

    override fun responseOK(json: JSONObject) {
        val listOfPubs = ListOfPubs(json)
        requester?.receivedNew(listOfPubs)
    }

}