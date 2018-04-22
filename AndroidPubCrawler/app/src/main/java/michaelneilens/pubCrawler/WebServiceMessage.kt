package michaelneilens.pubCrawler

import org.json.JSONException
import org.json.JSONObject

class WebServiceMessage(json: JSONObject) {
    val messageJSON =  try{ json.getJSONObject("Message") } catch(e: JSONException) { JSONObject() }
    val status = try{ messageJSON.getInt("Status") } catch(e: JSONException) {-999}
    val text = try{ messageJSON.getString("Text") } catch(e: JSONException) {""}
}