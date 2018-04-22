package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.ListOfPubCrawlsRequester
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ListOfPubCrawlsIOGetList(val requestDescription:String):WebService.WebServiceRequester {

    private var requester: ListOfPubCrawlsRequester?

    init {
        requester = null
    }

    fun makeRequest(search:String, userSetting:UserSetting, newRequest:ListOfPubCrawlsRequester) {
        requester = newRequest
        if (search.isNotEmpty()) {
            val urlRequest = "http://www.api.neilens.co.uk/ListOfPubCrawls/?function=search&search=" + search + userSetting.queryParms()
            getListOfPubCrawlsForUrl(urlRequest)
        } else {
            val urlRequest = "http://www.api.neilens.co.uk/ListOfPubCrawls/?function=list" + userSetting.queryParms()
            getListOfPubCrawlsForUrl(urlRequest)
        }

    }

    private fun getListOfPubCrawlsForUrl(urlRequest:String) {
        println("urlRequest: $urlRequest ")
        val request = WebServiceRequest(urlRequest)
        WebService().execute(request, this)
    }

    override fun processResponse(json: JSONObject) {
        val pubCrawls:ArrayList<PubCrawl> = arrayListOf()
        val message = WebServiceMessage(json)

        if (message.status != 0) {
            requester?.requestFailed(requestDescription, message.text)
        } else {
            val pubCrawlJSONArray =  try{ json.getJSONArray("PubCrawl") } catch(e: JSONException) {JSONArray()}
            for (i in 0..(pubCrawlJSONArray.length() - 1)) {
                val jsonObject = pubCrawlJSONArray.getJSONObject(i)
                val pubCrawl = PubCrawl(jsonObject)
                pubCrawls.add(pubCrawl)
            }
            requester?.receivedNew(pubCrawls)
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