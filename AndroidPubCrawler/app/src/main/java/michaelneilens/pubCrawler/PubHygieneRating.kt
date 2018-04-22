package michaelneilens.pubCrawler

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by michaelneilens on 17/03/2018.
 */
class PubHygieneRating(json: JSONObject) {
    val businessName:String
    val foodHygieneRating:String
    val ratingKey:String
    val ratingDate:String

    init {
        businessName =  try { json.getString("BusinessName") } catch(e: JSONException) {""}
        foodHygieneRating = try { json.getString("FoodHygieneRating") } catch(e: JSONException) {""}
        ratingKey = try { json.getString("RatingKey") } catch(e: JSONException) {""}
        ratingDate = try { "Reviewed on " + json.getString("RatingDate") } catch(e: JSONException) {""}
    }

    fun displayText():String {
        if (foodHygieneRating.isEmpty()) {
            return "Not available for " + businessName
        } else {
            return "  for " + businessName
        }
    }
    fun displayDate():String {
        if ((ratingDate != "1901-01-01") && (!ratingDate.isEmpty())) {
            return "   (Rated on " + ratingDate + ")"
        } else {
            return ""
        }
    }
    fun displayImageURL():String {
        return "http://ratings.food.gov.uk/images/scores/small/" + ratingKey + ".JPG"
    }
}
