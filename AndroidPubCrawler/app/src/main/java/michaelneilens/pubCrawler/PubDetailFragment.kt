package michaelneilens.pubCrawler

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogConfirmListener
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogEditListener
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogListListener
import michaelneilens.pubCrawler.EventListenerInterfaces.PubDetailListener
import michaelneilens.pubCrawler.IOInterfaces.PubDetailRequester
import michaelneilens.pubCrawler.IOInterfaces.PubHygieneRatingRequester


class PubDetailFragment: AbstractFragment()
        , PubDetailRequester
        , PubHygieneRatingRequester
        , PubDetailListener
        , DialogEditListener
        , DialogListListener
        , DialogConfirmListener {


    var pub = PubDetail()
    private var pubHygieneRatings:List<PubHygieneRating> = listOf()
    private var notStartedBefore = true

    private lateinit var adapter: PubDetailRecyclerViewAdapter
    private lateinit var recyclerView:RecyclerView

    private val pubDetailIOGetHygieneRatings = PubDetailIOGetHygieneRatings("get the hygiene rating for this pub")
    private val pubDetailIOGetPub = PubDetailIOGetPub("retrieve details about this pub")
    private val pubDetailIOUpdateVisitedOrLiked =  PubDetailIOUpdateVisitedOrLiked("change the visited or liked status of this pub")
    private val pubDetailIORemoveFromPubCrawl = PubDetailIORemoveFromPubCrawl("remove this pub from a pub crawl")
    private val pubDetailIOAddToPubCrawl  = PubDetailIOAddToPubCrawl("add this pub to a pub crawl")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (notStartedBefore) {
            requestPub(pub)
            notStartedBefore = false
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_pub_detail, container, false)

        if (view is RecyclerView) {
            val context = view.getContext()
            recyclerView = view
            view.layoutManager = LinearLayoutManager(context)
            setAdapter(view)
        }

        activity.title = pub.name
        return view
    }

    private fun setAdapter(recyclerView: RecyclerView) {
        adapter =   if (pub.name.isEmpty()) {
                        val listItems= listOf<ListItemsWithHeading>()
                        PubDetailRecyclerViewAdapter(listItems, this)
                    } else {
                        val listItems = mapListItemsFrom(pub,pubHygieneRatings)
                        PubDetailRecyclerViewAdapter(listItems, this)
                    }

        recyclerView.adapter = adapter
    }

    override fun onPubCrawlClicked(ndx: Int) {
        showPubCrawl(pub.listOfPubCrawls[ndx])
    }

    override fun onAddToPubCrawlClicked() {
        if (pub.listOfOtherPubCrawls.isNotEmpty()) {
            showAddToPubCrawlDialog()
        } else {
            showCreateNewPubCrawlDialog()
        }
    }

    private fun showAddToPubCrawlDialog() {
        val listOfNames = pub.listOfOtherPubCrawls.map{it.name} + mutableListOf("Create a New Pub Crawl")
        val dialog = DialogBoxList(this).create(activity,"Pick a pub crawl", listOfNames)
        dialog.show()
    }

    override fun listItemPressed(ndx: Int) {
        if (ndx < pub.listOfOtherPubCrawls.size) {
            pubDetailIOAddToPubCrawl.makeRequest(pub, ndx, this)
        } else {
            showCreateNewPubCrawlDialog()
        }
    }

    private fun showCreateNewPubCrawlDialog() {
        println("create new pub crawl")
        val dialog = DialogBoxEdit(this).create(activity,"New pub crawl", "Enter the name of the pub crawl")
        dialog.show()
    }

    override fun cancelPressed() {}

    override fun donePressed(text: String) {
        if (!text.isEmpty()) {
            pubDetailIOAddToPubCrawl.makeRequestNewPubCrawl(pub, text, this)
        }
    }

    override fun deletePubCrawlPressed(ndx: Int) {
        val dialog = DialogBoxConfirm(this).create(activity,"Confirm delete","Are you sure you wish to delete this pub crawl",ndx)
        dialog.show()
    }

    override fun yesPressed(text: String, ndx: Int)  {
        showProgressBar()
        pubDetailIORemoveFromPubCrawl.makeRequest(pub, ndx, this)
    }

    private fun showPubCrawl(pubCrawl: PubCrawl) {
        val intent = Intent(activity, PubCrawlActivity::class.java)
        intent.putExtra("PubCrawl",pubCrawl)
        startActivityForResult(intent,PubCrawlFragment.ACTIVITY_ID)
    }

    override fun onShowMapClicked() {
        if(requestInProgress) return

        showMap()
    }

    private fun showMap() {
        val parcelArrayList = arrayListOf(pub)

        val intent = Intent(activity, MapActivity::class.java)
        intent.putExtra(MapActivity.EXTRAS_PUBS,parcelArrayList)

        startActivity(intent)
    }

    override fun onShowPubImageClicked() {
        if(requestInProgress) return

        showPubImage()
    }

    private fun showPubImage() {
        val intent = Intent(activity, PubImageActivity::class.java)
        intent.putExtra(PubImageActivity.PHOTO_URL, pub.photoURL)
        startActivity(intent)
    }

    override fun onUpdateVisistedClicked() {
        updateVisited()
    }

    private fun updateVisited()  {
        showProgressBar()
        pubDetailIOUpdateVisitedOrLiked.makeRequestForVisited(pub, this)
    }

    override fun onUpdateLikedClicked() {
        updateLiked()
    }

    private fun updateLiked() {
        showProgressBar()
        pubDetailIOUpdateVisitedOrLiked.makeRequestForLiked(pub, this)
    }

    override fun onPause() {
        super.onPause()
        pubDetailIOGetHygieneRatings.cancelRequest()
        pubDetailIOGetPub.cancelRequest()
        pubDetailIOUpdateVisitedOrLiked.cancelRequest()
        pubDetailIORemoveFromPubCrawl.cancelRequest()
        pubDetailIOAddToPubCrawl.cancelRequest()
    }

    private fun requestPub(pub: PubDetail)  {
        showProgressBar()
        pubDetailIOGetPub.makeRequest(pub.pubService, this)
    }

    override fun receivedNew(pub: PubDetail, noOfRowsChanged:Boolean) {
        hideProgressBar()
        activity.runOnUiThread {
            run {
                this.pub = pub

                adapter.listItemsWithHeadings = mapListItemsFrom(pub, pubHygieneRatings)
                if (noOfRowsChanged) {
                    recyclerView.adapter = adapter
                }
                else {
                    adapter.notifyDataSetChanged()
                }
            }
        }
        requestHygieneRating(pub)
    }

    private fun mapListItemsFrom(pub: PubDetail, pubHygieneRatings: List<PubHygieneRating> ):List<ListItemsWithHeading>{
        val listOfItemsWithHeading:ArrayList<ListItemsWithHeading> = arrayListOf()

        if (!pub.address.isEmpty()) {
            val addressHeading = ListHeading(HEADING_ADDRESS)
            val addressListItem = SimpleListItem(pub.address)
            val showOnMapListItem = SimpleWithImageListItem("Show on map...",  R.drawable.globe_29)
            val pubImageListItem = PubImageListItem("Show picture...",  pub.photoURL)
            val addressListItemsWithHeading = ListItemsWithHeading(addressHeading, listOf(addressListItem, showOnMapListItem, pubImageListItem))
            listOfItemsWithHeading.add(addressListItemsWithHeading)
        }

        if (!pub.address.isEmpty()) {
            val pubCrawlsHeading = ListHeading(HEADING_ONPUBCRAWLS)

            val mapFunction = { value: PubCrawl -> PubCrawlListItem(value.name, value.removePubCrawlService.isNotEmpty()) }
            val addToPubCrawl =  SimpleWithImageListItem("Add to a pub crawl",R.drawable.bug_29)
            val pubCrawlsItems = pub.listOfPubCrawls.map(mapFunction) + addToPubCrawl
            val pubCrawlsItemsWithHeading = ListItemsWithHeading(pubCrawlsHeading, pubCrawlsItems)
            listOfItemsWithHeading.add(pubCrawlsItemsWithHeading)
        }

        if (!pub.about.isEmpty()) {
            val aboutHeading = ListHeading(HEADING_ABOUT)
            val aboutListItem = ExpandingListItem(pub.about)
            val aboutListItemsWithHeading = ListItemsWithHeading(aboutHeading, listOf(aboutListItem))
            listOfItemsWithHeading.add(aboutListItemsWithHeading)
        }

        if (!pub.telephone.isEmpty()) {
            val telephoneHeading = ListHeading(HEADING_TELEPHONE)
            val telephoneListItem = SimpleWithImageListItem(pub.telephone,  R.drawable.phone_29)
            val telephoneItemsWithHeading = ListItemsWithHeading(telephoneHeading, listOf(telephoneListItem))
            listOfItemsWithHeading.add(telephoneItemsWithHeading)
        }

        if (!pub.openingTimes.isEmpty()) {
            val openingTimesHeading = ListHeading(HEADING_OPENINGTIMES)
            val openingTimesListItem = SimpleListItem(pub.openingTimes)
            val openingTimesItemsWithHeading = ListItemsWithHeading(openingTimesHeading, listOf(openingTimesListItem))
            listOfItemsWithHeading.add(openingTimesItemsWithHeading)
        }

        if (!pub.mealTimes.isEmpty()) {
            val mealTimesHeading = ListHeading(HEADING_MEALTIMES)
            val mealTimesListItem = SimpleListItem(pub.mealTimes)
            val mealTimesItemsWithHeading = ListItemsWithHeading(mealTimesHeading, listOf(mealTimesListItem))
            listOfItemsWithHeading.add(mealTimesItemsWithHeading)
        }

        if (!pub.address.isEmpty()) {
            if (pubHygieneRatings.isEmpty()) {
                val pubHygieneRatingHeading = ListHeading(HEADING_HYGIENERATINGS)
                val hygieneItems = listOf(HygieneListItem("Hygiene rating not available",  "","blank"))
                val hygieneItemsWithHeading = ListItemsWithHeading(pubHygieneRatingHeading, hygieneItems)
                listOfItemsWithHeading.add(hygieneItemsWithHeading)
            } else {
                val pubHygieneRatingHeading = ListHeading(HEADING_HYGIENERATINGS)
                val hygieneItems = pubHygieneRatings.map { HygieneListItem(it.foodHygieneRating + " for " + it.businessName, it.ratingDate, it.ratingKey) }
                val hygieneItemsWithHeading = ListItemsWithHeading(pubHygieneRatingHeading, hygieneItems)
                listOfItemsWithHeading.add(hygieneItemsWithHeading)
            }
        }

        if (!pub.owner.isEmpty()) {
             val ownerHeading = ListHeading(HEADING_OWNER)
             val ownerListItem = SimpleListItem(pub.owner)
             val ownerItemsWithHeading = ListItemsWithHeading(ownerHeading, listOf(ownerListItem))
             listOfItemsWithHeading.add(ownerItemsWithHeading)
        }

        if (pub.beer.isNotEmpty()) {
            val beerHeading = ListHeading(HEADING_REGULARBEERS)
            val beerItems = pub.beer.map{ SimpleListItem(it) }
            val beerItemsWithHeading = ListItemsWithHeading(beerHeading, beerItems)
            listOfItemsWithHeading.add(beerItemsWithHeading)
        }

        if (pub.guest.isNotEmpty()) {
            val guestHeading = ListHeading(HEADING_GUESTBEERS)
            val guestItems = pub.guest.map{ SimpleListItem(it) }
            val guestItemsWithHeading = ListItemsWithHeading(guestHeading, guestItems)
            listOfItemsWithHeading.add(guestItemsWithHeading)
        }

        if (pub.feature.isNotEmpty()) {
            val featuresHeading = ListHeading(HEADING_FEATURES)
            val featuresItems = pub.feature.map{ SimpleListItem(it) }
            val featuresItemsWithHeading = ListItemsWithHeading(featuresHeading, featuresItems)
            listOfItemsWithHeading.add(featuresItemsWithHeading)
        }

        if (pub.facility.isNotEmpty()) {
            val facilitiesHeading = ListHeading(HEADING_FACILITIES)
            val facilitiesItems = pub.facility.map{ SimpleListItem(it) }
            val facilitiesItemsWithHeading = ListItemsWithHeading(facilitiesHeading, facilitiesItems)
            listOfItemsWithHeading.add(facilitiesItemsWithHeading)
        }

        if (!pub.address.isEmpty()) {
            val visitedHeading = ListHeading(HEADING_VISITED)
            val visitedListItem = ToggleListItem("Visited", "",pub.visited == "y")
            val likedListItem = ToggleListItem("Liked", "",pub.liked == "y")
            val visitedItemsWithHeading = ListItemsWithHeading(visitedHeading, listOf(visitedListItem, likedListItem))
            listOfItemsWithHeading.add(visitedItemsWithHeading)

            val blankHeading = ListHeading("")
            val blankListItemWithHeading = ListItemsWithHeading(blankHeading, listOf())
            listOfItemsWithHeading.add(blankListItemWithHeading)
        }
        return listOfItemsWithHeading
    }

    private fun requestHygieneRating(pub: PubDetail) {
        pubDetailIOGetHygieneRatings.makeRequest(pub.hygieneRatingService, this)
    }

    override fun receivedNew(pubHygieneRatings: List<PubHygieneRating>) {
        for (h in pubHygieneRatings) {
            println("hygieneRatings " + h.foodHygieneRating )
        }
        activity.runOnUiThread {
            run {
                this.pubHygieneRatings = pubHygieneRatings
                adapter.listItemsWithHeadings = mapListItemsFrom(pub, pubHygieneRatings)
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {

        const val HEADING_ADDRESS = "Address"
        const val HEADING_ONPUBCRAWLS = "On Pub Crawls"
        private const val HEADING_ABOUT = "About"
        private const val HEADING_TELEPHONE = "Telephone"
        private const val HEADING_OPENINGTIMES = "Opening Times"
        private const val HEADING_MEALTIMES = "Meal Times"
        private const val HEADING_HYGIENERATINGS = "Local Food Hygiene Ratings"
        private const val HEADING_OWNER = "Owner"
        private const val HEADING_REGULARBEERS = "Regular Beers"
        private const val HEADING_GUESTBEERS = "Guest Beers"
        private const val HEADING_FACILITIES = "Facilities"
        private const val HEADING_FEATURES = "Features"
        const val HEADING_VISITED = "Visit History"


    }
}