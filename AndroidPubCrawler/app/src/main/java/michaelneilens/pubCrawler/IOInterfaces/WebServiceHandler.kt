package michaelneilens.pubCrawler.IOInterfaces

import michaelneilens.pubCrawler.WebService
import org.json.JSONObject

interface WebServiceHandler {
    fun createRequest(url:String, queryParameters:Map<String,String>?=null, method:String?="GET", headers:Map<String,String>?=null, body: JSONObject?=null)
    fun execute(requester: WebService.WebServiceRequester)
}
