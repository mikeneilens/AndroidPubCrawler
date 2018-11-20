package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubCrawlsRequester
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ListOfPubCrawlsIOGetList(requestDescription:String):AbstractIO<ListOfPubCrawlsRequester>(requestDescription) {

    fun makeRequest(search:String, userSetting:UserSetting, newRequest:ListOfPubCrawlsRequester) {
        requester = newRequest
        if (search.isNotEmpty()) {
            val urlRequest = "https://pubcrawlapi.appspot.com/listofpubcrawls/?function=search&search=" + search + userSetting.queryParms()
            getListOfPubCrawlsForUrl(urlRequest)
        } else {
            val urlRequest = "https://pubcrawlapi.appspot.com/listofpubcrawls/?function=list" + userSetting.queryParms()
            getListOfPubCrawlsForUrl(urlRequest)
        }

    }

    private fun getListOfPubCrawlsForUrl(urlRequest:String) {
        println("urlRequest: $urlRequest ")
        executeRequest(urlRequest)
    }

    override fun responseOK(json:JSONObject) {

        val pubCrawlJSONArray =  try{ json.getJSONArray("PubCrawl") } catch(e: JSONException) {JSONArray()}

        //converts json object to a PubCrawl object
        val mapFunction = {ndx:Int, _:String -> val jsonObject = pubCrawlJSONArray.getJSONObject(ndx)
             PubCrawl(jsonObject)}

        val pubCrawls = List(pubCrawlJSONArray.length()){""}.mapIndexed(mapFunction)

        requester?.receivedNew(pubCrawls)
    }
}