package michaelneilens.pubCrawler

import android.location.Location
import michaelneilens.pubCrawler.Extensions.getWithDefault
import michaelneilens.pubCrawler.Extensions.swap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ListOfPubs {
    val pubs:List<PubDetail>
    val morePubsService:String

    constructor(){
        pubs = listOf()
        morePubsService = ""
    }

    constructor(listOfPubsJSON: JSONObject) {
        val pubsJSON = listOfPubsJSON.getWithDefault("Pubs", JSONArray())

        val mapFunction = {ndx:Int, _:String ->
            val pubJSON = pubsJSON.getJSONObject(ndx)
            PubDetail(pubJSON)}

        pubs = List(pubsJSON.length()){""}.mapIndexed(mapFunction)
        morePubsService = listOfPubsJSON.getWithDefault("MorePubsService", "")
    }

    constructor(pubs:List<PubDetail>, morePubsService:String) {
        this.pubs = pubs
        this.morePubsService = morePubsService
    }

    fun plus(listOfPubs: ListOfPubs): ListOfPubs {
        val newPubs = this.pubs + listOfPubs.pubs
        val newMorePubService = listOfPubs.morePubsService
        return ListOfPubs(newPubs, newMorePubService)
    }

    fun distanceToPreviousPubText(ndx:Int):String {

        return if (ndx > 0) {
                    val pubA = pubs[ndx]
                    val pubB = pubs[ndx - 1]

                    val distance = distanceBetween(pubA, pubB)

                    val distanceRounded = Math.round(distance * 10).toDouble() / 10
                    distanceRounded.toString() + " miles from " + pubB.name
                } else {
                    ""
                }
    }

    private fun distanceBetween(pubA:PubDetail ,pubB:PubDetail):Double {
        val startPoint = Location("locationA")
        startPoint.latitude =  pubA.latitude
        startPoint.longitude = pubA.longitude

        val endPoint = Location("locationA")
        endPoint.latitude = pubB.latitude
        endPoint.longitude = pubB.longitude

        return startPoint.distanceTo(endPoint) / METRES_IN_A_MILE
    }

    fun swap(ndx1:Int, ndx2:Int):ListOfPubs {
        val swappedPubs = pubs.swap(ndx1, ndx2)
        return ListOfPubs(swappedPubs, morePubsService)
    }

    fun reorder(): ListOfPubs {
        //this attempts to put pubs in distance order.
        //Starting at the first pub in the list, it finds the nearest pub and adds that to the new list and removes it from the old list.
        //It repeats this for the pub it has added to the list until there are no pubs left in the old list.
        //There's probably a funky recursive way to do this better.
        val newPubs = arrayListOf<PubDetail>()
        newPubs.add(pubs[0])
        val oldPubs = ArrayList(pubs)
        oldPubs[0] =(PubDetail())

        for (ndx in 0..(oldPubs.size - 2))  {
            val pubA = newPubs[ndx]
            var minDistance = 999999.0
            var closestPubNdx = 0

            for (ndx2 in 0..(oldPubs.size -1))  {
                val pubB = oldPubs[ndx2]
                val d = distanceBetween(pubA, pubB)
                if (d < minDistance)  {
                    minDistance = d
                    closestPubNdx = ndx2
                }
            }
            val closestPub = oldPubs[closestPubNdx]
            oldPubs[closestPubNdx] = PubDetail()
            newPubs.add(closestPub)
        }
        return ListOfPubs(newPubs, morePubsService)
    }

    companion object {
        private const val METRES_IN_A_MILE = 1609.344
    }
}


