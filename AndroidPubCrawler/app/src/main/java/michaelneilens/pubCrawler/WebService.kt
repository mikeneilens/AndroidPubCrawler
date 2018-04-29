package michaelneilens.pubCrawler

import michaelneilens.pubCrawler.IOInterfaces.WebServiceHandler
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WebService(private val client: OkHttpClient = OkHttpClient() ): WebServiceHandler {

    interface WebServiceRequester {
        fun processResponse(json: JSONObject)
        fun requestFailed(e: Exception)
    }

    private var webServiceRequest:WebServiceRequest? = null

    override fun createRequest(url:String, queryParameters:Map<String,String>?, method:String?, headers:Map<String,String>?, body: JSONObject?) {
        webServiceRequest = WebServiceRequest(url, queryParameters, method, headers, body)
    }

    override fun execute(requester:WebServiceRequester) {
        val request = webServiceRequest?.request

        if (request == null) {
            requester.requestFailed(WebServiceException.InvalidRequest())
        } else {
            call(request, requester)
        }
    }

    private fun call(request:Request, requester:WebServiceRequester) {
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

    class WebServiceRequest (url:String, queryParameters:Map<String,String>?=null, method:String?="GET", headers:Map<String,String>?=null, body: JSONObject?=null) {

        val request:Request?
        private val jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        init{
            val initialHttpUrl =  HttpUrl.parse(url)

            request =
                    if (initialHttpUrl is HttpUrl) {

                        val httpUrl = addQueryParameters(initialHttpUrl, queryParameters)
                        val requestBody = createRequestBody(body)
                        val requestHeaders = createRequestHeaders(headers)

                        val requestBuilder = Request.Builder().url(httpUrl)
                        if (requestHeaders is Headers)  requestBuilder.headers(requestHeaders)
                        when (method) {
                            "GET",null -> requestBuilder.get()
                            "POST" -> if (requestBody is RequestBody) requestBuilder.post(requestBody)
                            "PUT" ->  if (requestBody is RequestBody) requestBuilder.put(requestBody)
                            "PATCH" -> if (requestBody is RequestBody) requestBuilder.patch(requestBody)
                            "DELETE" -> requestBuilder.delete()
                        }

                        requestBuilder.build()
                    } else {
                        null
                    }
        }

        private fun addQueryParameters(httpUrl:HttpUrl, parameters:Map<String,String>?):HttpUrl {
            val httpUrlBuilder =  httpUrl.newBuilder()

            if (parameters is Map<String, String>) {
                for ((key, value) in parameters) {
                    httpUrlBuilder?.addQueryParameter(key, value)
                }
            }

            return httpUrlBuilder.build()
        }

        private fun createRequestBody(body: JSONObject?):RequestBody? {
            return if (body == null) {
                null
            } else {
                val jsonString = body.toString()
                RequestBody.create(jsonMediaType,jsonString)
            }
        }

        private fun createRequestHeaders(headers:Map<String,String>?):Headers? {
            return  if (headers == null) {
                null
            } else {
                Headers.of(headers)
            }
        }
    }
}