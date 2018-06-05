package michaelneilens.pubCrawler

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.ActivityChooserView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import michaelneilens.pubCrawler.EventListenerInterfaces.ListOfPubsListener
import michaelneilens.pubCrawler.EventListenerInterfaces.LocationProcessor


class ListOfPubsFragment: AbstractFragment(), ListOfPubsRequester, ListOfPubsListener, LocationRequester {

    private var lastSearchText = ""
    private var listOfPubs = ListOfPubs(listOf(), "")
    private var hasNotStartedBefore = true

    private lateinit var adapter: ListOfPubsRecyclerViewAdapter
    private val listOfPubsIOGetList= ListOfPubsIOGetList("retrieve list of pubs")
    private val listOfPubsIOGetListMore = ListOfPubsIOGetListMore("retrieve additional pubs")


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        checkFragmentIsCorrect()

        val view = inflater!!.inflate(R.layout.fragment_listofpubs, container, false)
        activity.title = getString(R.string.title_search_for_pubs)

        setTheAdapter(view)

        if (hasNotStartedBefore) {
            getPubsForCurrentLocation()
            hasNotStartedBefore = false
        }

        return view
    }

    private fun checkFragmentIsCorrect() {
        //bit of a botch to deal with a weird back stack problem
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            if (mainActivity.lastFragmentSelected != mainActivity.listOfPubsFragment ) {
                activity.finish()
            }
        }
    }

    private fun setTheAdapter(view:View) {
        if (view is RecyclerView) {
            val context = view.context
            view.layoutManager = LinearLayoutManager(context)

            adapter = ListOfPubsRecyclerViewAdapter(mapListItemsFrom(listOfPubs), this)
            view.adapter = adapter
        }
    }

    private fun getPubsForCurrentLocation() {
        val mainActivity = activity as? MainActivity
        mainActivity?.getLocation(this)
    }

    override fun processNewLocation(location: Location?) {
        if (location != null) {
            requestListOfPubsForCurrentLocation(location.latitude, location.longitude)
        } else {
            requestListOfPubs("London")
        }
    }

    override fun processLocationAccessDenied() {
        requestListOfPubs("London")
    }

    override fun onResume() {
        super.onResume()
        adapter.removeKeyboard()
    }

    override fun onPause() {
        super.onPause()
        listOfPubsIOGetList.cancelRequest()
        listOfPubsIOGetListMore.cancelRequest()
    }

    override fun onPubClicked(ndx:Int) {
        if (requestInProgress) {return}

        showPubDetailFragment(listOfPubs.pubs[ndx])
    }

    override fun onMoreClicked() {
        if (requestInProgress) {return}

        requestMorePubs()
    }

    override fun onQueryTextSubmit(query: String) {
        adapter.removeKeyboard()
        if (requestInProgress) {return}

        lastSearchText = query
        requestListOfPubs(query)
    }
    override fun onNearMeClick() {
        if (requestInProgress) {return}

        lastSearchText = ""
        getPubsForCurrentLocation()
    }

    private fun showPubDetailFragment(pub: PubDetail) {
        val pubDetailFragment = PubDetailFragment()
        pubDetailFragment.pub = pub
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, pubDetailFragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun requestListOfPubs(search:String)  {
        listOfPubsIOGetList.makeRequest(search, userSetting,this)
        showProgressBar()
    }
    private fun requestListOfPubsForCurrentLocation(latitude:Double, longitude:Double) {
        listOfPubsIOGetList.makeRequestForLocation(latitude,longitude, userSetting, this)
        showProgressBar()
    }

    private fun requestMorePubs()  {
        listOfPubsIOGetListMore.makeRequest(listOfPubs,this)
        showProgressBar()
    }

    override fun receivedNew(listOfPubs: ListOfPubs) {
        hideProgressBar()
        this.listOfPubs = listOfPubs

        activity.runOnUiThread {
            run {
                adapter.listItems = mapListItemsFrom(listOfPubs)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun mapListItemsFrom(listOfPubs: ListOfPubs):List<ListItem> {
        val mapFunction = { value: PubDetail -> DetailedClickableListItem(value.name, value.town + ", " + value.distance) }
        val searchListItem = SearchPubsListItem(lastSearchText,true)
        val listItems = listOf(searchListItem)  + listOfPubs.pubs.map(mapFunction)

        return  if (listOfPubs.morePubsService.isEmpty()) {
                    listItems
                } else {
                 listItems + DetailedClickableListItem(MORE_PUBS_TEXT,"")
                }
    }

    companion object {
        const val MORE_PUBS_TEXT = "More Pubs..."
    }
}