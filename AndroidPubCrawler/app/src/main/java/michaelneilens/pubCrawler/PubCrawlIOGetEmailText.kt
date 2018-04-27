package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.Extensions.getWithDefault
import michaelneilens.pubCrawler.IOInterfaces.PubCrawlPublicRequester
import org.json.JSONException
import org.json.JSONObject

class PubCrawlIOGetEmailText(requestDescription:String): AbstractIO<PubCrawlPublicRequester>(requestDescription) {

    fun makeRequest(pubCrawl:PubCrawl, newRequester: PubCrawlPublicRequester) {
        requester = newRequester
        val url = pubCrawl.emailTextService
        val urlRequestHttp = url.replace("https","http")

        executeRequest(urlRequestHttp)
    }

    override fun responseOK(json: JSONObject) {
        val htmlText = json.getWithDefault("EmailText","")
        val plainText = json.getWithDefault("PlainText","")
        requester?.obtainedEmailText(htmlText, plainText)
    }

}