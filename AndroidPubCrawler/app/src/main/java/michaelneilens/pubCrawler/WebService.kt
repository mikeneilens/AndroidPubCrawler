package michaelneilens.pubCrawler

import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WebService(private val client: OkHttpClient = OkHttpClient() ){

    interface WebServiceRequester {
        fun processResponse(json: JSONObject)
        fun requestFailed(e: Exception)
    }

    fun execute(webServiceRequest:WebServiceRequest, requester:WebServiceRequester) {

        if (webServiceRequest.request is Request) {
            makeRequest(webServiceRequest.request, requester)
        } else {
            requester.requestFailed(WebServiceException.InvalidRequest())
        }
    }
    private fun makeRequest(request:Request, requester:WebServiceRequester) {
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                requester.requestFailed(e)
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val json = JSONObject(response.body()!!.string())
                    requester.processResponse(json)

                } catch (e: JSONException) {
                    requester.requestFailed(e)
                    e.printStackTrace()
                }
            }
        })
    }
}