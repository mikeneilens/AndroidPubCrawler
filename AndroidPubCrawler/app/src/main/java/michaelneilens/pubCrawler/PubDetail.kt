package michaelneilens.pubCrawler

import android.os.Parcel
import android.os.Parcelable
import michaelneilens.pubCrawler.Extensions.getWithDefault
import michaelneilens.pubCrawler.Extensions.mapJSONArrayToStringArray
import org.json.JSONArray
import org.json.JSONObject

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
        name = pubJSON.getWithDefault("Name","")
        town = pubJSON.getWithDefault("Town", "")
        distance = pubJSON.getWithDefault("Distance", "")
        sequence = pubJSON.getWithDefault("Sequence", 0)
        pubService = pubJSON.getWithDefault("PubService" ,"")
        removePubService = pubJSON.getWithDefault("RemovePubService", "")
        address = pubJSON.getWithDefault("Address", "" )
        photoURL = pubJSON.getWithDefault("PhotoURL", "")
        telephone = pubJSON.getWithDefault("Telephone", "")
        openingTimes = pubJSON.getWithDefault("OpeningTimes", "")
        mealTimes = pubJSON.getWithDefault("MealTimes", "")
        owner = pubJSON.getWithDefault("Owner", "")
        about= pubJSON.getWithDefault("About", "")
        changeLikedService = pubJSON.getWithDefault("ChangeLikedService", "")
        changeVisitedService = pubJSON.getWithDefault("ChangeVisitedService", "")
        hygieneRatingService = pubJSON.getWithDefault("HygieneRatingService", "")
        pubsNearByService = pubJSON.getWithDefault("PubsNearByService", "")
        visited= pubJSON.getWithDefault("Visited", "n")
        liked = pubJSON.getWithDefault("Liked", "n")
        createPubCrawlService = pubJSON.getWithDefault("CreatePubCrawlService", "")
        latitude = pubJSON.getWithDefault("Lat", 0.0)
        longitude = pubJSON.getWithDefault("Lng", 0.0)


        facility = pubJSON.mapJSONArrayToStringArray("Facilities")
        feature = pubJSON.mapJSONArrayToStringArray( "Features")
        beer = pubJSON.mapJSONArrayToStringArray( "RegularBeers" )

        val guestBeerDesc = pubJSON.getWithDefault("GuestBeerDesc", "")
        guest = if ( guestBeerDesc.isEmpty()) {
                    pubJSON.mapJSONArrayToStringArray( "GuestBeers" )
                }
                else {
                    listOf(guestBeerDesc) + pubJSON.mapJSONArrayToStringArray("GuestBeers" )
                }

        val pubCrawlJSONArray = pubJSON.getWithDefault("PubCrawl",JSONArray())

        val convertToPubCrawl = jsonToPubCrawlMapping(pubCrawlJSONArray)

        listOfPubCrawls = List(pubCrawlJSONArray.length()){""}.mapIndexed(convertToPubCrawl)

        val otherPubCrawlJSONArray = pubJSON.getWithDefault("OtherPubCrawl", JSONArray())

        val convertToOtherPubCrawl = jsonToPubCrawlMapping(otherPubCrawlJSONArray)
        listOfOtherPubCrawls = List(otherPubCrawlJSONArray.length()){""}.mapIndexed(convertToOtherPubCrawl)

        nextPubService = pubJSON.getWithDefault("NextPubService", "")

    }

    private fun jsonToPubCrawlMapping(jsonArray:JSONArray):(Int, String)-> PubCrawl {
        return { ndx:Int, _:String ->
            val jsonObject = jsonArray.getJSONObject(ndx)
            PubCrawl(jsonObject) }
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