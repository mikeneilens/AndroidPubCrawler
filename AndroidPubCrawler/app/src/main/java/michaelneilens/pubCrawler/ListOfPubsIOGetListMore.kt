package michaelneilens.pubCrawler

/**
 * Created by michaelneilens on 13/03/2018.
 */
import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import org.json.JSONObject

class ListOfPubsIOGetListMore(requestDescription:String):AbstractIO<ListOfPubsRequester>(requestDescription) {

    private var originalListOfPubs:ListOfPubs= ListOfPubs()

    fun makeRequest(listOfPubs:ListOfPubs, newRequester: ListOfPubsRequester) {
        requester = newRequester
        originalListOfPubs = listOfPubs
        val urlRequest = originalListOfPubs.morePubsService.replace("https", URL_SCHEME)

        executeRequest(urlRequest)
    }

    override fun responseOK(json: JSONObject) {
        val receivedListOfPubs = ListOfPubs(json)
        val newListOfPubs = originalListOfPubs.plus(receivedListOfPubs)
        requester?.receivedNew(newListOfPubs)    }

}