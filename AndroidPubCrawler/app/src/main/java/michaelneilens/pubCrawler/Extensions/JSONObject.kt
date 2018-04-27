package michaelneilens.pubCrawler.Extensions

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getWithDefault(key:String, default:String = ""):String {
    return try { this.getString(key) } catch(e: Exception) {default}
}
fun JSONObject.getWithDefault(key:String, default:Int = 0):Int {
    return try { this.getInt(key) } catch(e: Exception) {default}
}
fun JSONObject.getWithDefault(key:String, default:Double = 0.0):Double {
    return try { this.getDouble(key) } catch(e: Exception) {default}
}
fun JSONObject.getWithDefault(key:String, default:JSONArray):JSONArray {
    return try { this.getJSONArray(key) } catch(e: Exception) {default}
}

fun JSONObject.mapJSONArrayToStringArray(key:String):List<String> {
    val jsonArray = this.getWithDefault(key, JSONArray())
    return List(jsonArray.length()){""}.mapIndexed { index, _ ->
        jsonArray.get(index) as String
    }
}

