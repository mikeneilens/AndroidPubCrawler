package michaelneilens.pubCrawler

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by michaelneilens on 13/03/2018.
 */
class PubDetail:Parcelable {

    var name:String
    val town:String
    val distance:String
    val sequence:Int
    var longitude:Double
    var latitude:Double
    val pubService:String
    val removePubService:String
    val address:String
    val photoURL:String
    val telephone:String
    val openingTimes:String
    val mealTimes:String
    val owner:String
    val about:String
    val beer:List<String>
    val guest:List<String>
    val feature:List<String>
    val facility:List<String>
    val changeLikedService:String
    val changeVisitedService:String
    val hygieneRatingService:String
    private val pubsNearByService:String
    val visited:String
    val liked:String
    val createPubCrawlService:String
    val listOfPubCrawls:List<PubCrawl>
    val listOfOtherPubCrawls:List<PubCrawl>
    private val nextPubService:String

    constructor(pubJSON:JSONObject){
        name = try { pubJSON.getString("Name") } catch(e:JSONException) {""}
        town = try { pubJSON.getString("Town") } catch(e:JSONException) {""}
        distance = try { pubJSON.getString("Distance") } catch(e:JSONException) {""}
        sequence = try { pubJSON.getInt("Sequence") } catch(e:JSONException) {0}
        pubService = try { pubJSON.getString("PubService") } catch(e:JSONException) {""}
        removePubService = try { pubJSON.getString("RemovePubService") } catch(e:JSONException) {""}
        address = try { pubJSON.getString("Address") } catch(e:JSONException) {""}
        photoURL = try { pubJSON.getString("PhotoURL") } catch(e:JSONException) {""}
        telephone = try { pubJSON.getString("Telephone") } catch(e:JSONException) {""}
        openingTimes = try { pubJSON.getString("OpeningTimes") } catch(e:JSONException) {""}
        mealTimes = try { pubJSON.getString("MealTimes") } catch(e:JSONException) {""}
        owner = try { pubJSON.getString("Owner") } catch(e:JSONException) {""}
        about= try { pubJSON.getString("About") } catch(e:JSONException) {""}
        changeLikedService = try { pubJSON.getString("ChangeLikedService") } catch(e:JSONException) {""}
        changeVisitedService = try { pubJSON.getString("ChangeVisitedService") } catch(e:JSONException) {""}
        hygieneRatingService = try { pubJSON.getString("HygieneRatingService") } catch(e:JSONException) {""}
        pubsNearByService = try { pubJSON.getString("PubsNearByService") } catch(e:JSONException) {""}
        visited= try { pubJSON.getString("Visited") } catch(e:JSONException) {"n"}
        liked = try { pubJSON.getString("Liked") } catch(e:JSONException) {"n"}
        createPubCrawlService = try { pubJSON.getString("CreatePubCrawlService") } catch(e:JSONException) {""}
        latitude = try { pubJSON.getDouble("Lat") } catch(e:JSONException) {0.0}
        longitude = try { pubJSON.getDouble("Lng") } catch(e:JSONException) {0.0}

        fun mapJSONArray(jsonObject:JSONObject, key:String):List<String> {
            val tempItems:ArrayList<String> = arrayListOf()

            val jsonArray = try {  jsonObject.getJSONArray(key) } catch(e:JSONException) {JSONArray()}

            for (i in 0..(jsonArray.length() - 1)) {
                val newItem = jsonArray.get(i) as String
                tempItems.add(newItem)
            }
            return tempItems
        }

        facility = mapJSONArray(pubJSON, "Facilities")
        feature = mapJSONArray(pubJSON, "Features")
        beer = mapJSONArray(pubJSON, "RegularBeers" )

        val guestBeerDesc = try { pubJSON.getString("GuestBeerDesc") } catch(e:JSONException) {""}
        guest = if ( guestBeerDesc.isEmpty()) {
                    mapJSONArray(pubJSON, "GuestBeers" )
                }
                else {
                    listOf(guestBeerDesc) + mapJSONArray(pubJSON, "GuestBeers" )
                }

        val tempPubCrawls:ArrayList<PubCrawl> = arrayListOf()
        val pubCrawlJSONArray = try { pubJSON.getJSONArray("PubCrawl") } catch(e:JSONException) {JSONArray()}

        for (i in 0..(pubCrawlJSONArray.length() - 1)) {
            val jsonObject = pubCrawlJSONArray.getJSONObject(i)
            val pubCrawl = PubCrawl(jsonObject)
            tempPubCrawls.add(pubCrawl)
        }
        listOfPubCrawls = tempPubCrawls
        val tempOtherPubCrawls:ArrayList<PubCrawl> = arrayListOf()

        val otherPubCrawlJSONArray = try { pubJSON.getJSONArray("OtherPubCrawl") } catch(e:JSONException) {JSONArray()}

        for (i in 0..(otherPubCrawlJSONArray.length() - 1)) {
            val jsonObject = otherPubCrawlJSONArray.getJSONObject(i)
            val pubCrawl = PubCrawl(jsonObject)
            tempOtherPubCrawls.add(pubCrawl)
        }

        listOfOtherPubCrawls = tempOtherPubCrawls

        nextPubService = try { pubJSON.getString("NextPubService") } catch(e:JSONException) {""}

    }
    constructor() {
        name = ""
        town = ""
        distance = ""
        sequence = 0
        pubService =""
        removePubService = ""
        address = ""
        photoURL = ""
        telephone = ""
        openingTimes = ""
        mealTimes = ""
        owner = ""
        about = ""
        beer = listOf()
        guest = listOf()
        feature = listOf()
        facility = listOf("")
        changeLikedService = ""
        changeVisitedService = ""
        hygieneRatingService = ""
        pubsNearByService = ""
        latitude=0.0
        longitude=0.0
        visited = "n"
        liked = "n"
        createPubCrawlService = ""
        listOfPubCrawls = listOf()
        listOfOtherPubCrawls = listOf()
        nextPubService = ""
    }
/*
    fun parcel():PubDetailParcel {
        val pubParcel = PubDetailParcel()
        pubParcel.name = this.name
        pubParcel.latitude = this.latitude.toDouble()
        pubParcel.longitude = this.longitude.toDouble()
        return pubParcel
    }
*/
    constructor(parcel: Parcel)  {
        name = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        town = ""
        distance = ""
        sequence = 0
        pubService =""
        removePubService = ""
        address = ""
        photoURL = ""
        telephone = ""
        openingTimes = ""
        mealTimes = ""
        owner = ""
        about = ""
        beer = listOf()
        guest = listOf()
        feature = listOf()
        facility = listOf("")
        changeLikedService = ""
        changeVisitedService = ""
        hygieneRatingService = ""
        pubsNearByService = ""
        visited = "n"
        liked = "n"
        createPubCrawlService = ""
        listOfPubCrawls = listOf()
        listOfOtherPubCrawls = listOf()
        nextPubService = ""

    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PubDetail> {
        override fun createFromParcel(parcel: Parcel): PubDetail {
            return PubDetail(parcel)
        }

        override fun newArray(size: Int): Array<PubDetail?> {
            return arrayOfNulls(size)
        }
    }
}