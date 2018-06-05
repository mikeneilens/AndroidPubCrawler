package michaelneilens.pubCrawler

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import michaelneilens.pubCrawler.EventListenerInterfaces.ListOfPubCrawlsListener
import michaelneilens.pubCrawler.IOInterfaces.ListOfPubCrawlsRequester

class ListOfPubCrawlsFragment : AbstractFragment(), ListOfPubCrawlsRequester, ListOfPubCrawlsListener {

    private val listOfPubCrawlsIOGetList = ListOfPubCrawlsIOGetList( "retrieve the list of pub crawls")

    private var lastSearchText = ""
    private var listOfPubCrawls = listOf<PubCrawl>()
    private var hasNotStartedBefore = true

    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter: ListOfPubCrawlsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_listofpubcrawls, container, false)
        activity.title = getString(R.string.title_search_for_pubcrawls)

        setTheAdapter(view)

        if (hasNotStartedBefore) {
            hasNotStartedBefore = false
        }

        return view
    }

    private fun getInitialListOfPubCrawls() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            lastSearchText = mainActivity.providePubCrawlLink(lastSearchText)
        }
        requestListOfPubCrawls(lastSearchText)
    }

    override fun onResume() {
        getInitialListOfPubCrawls()
        super.onResume()
    }

    private fun setTheAdapter(view: View) {
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            recyclerView = view
            adapter = ListOfPubCrawlsRecyclerViewAdapter(mapListItemsFrom(listOfPubCrawls), this)
            view.adapter = adapter
        }
    }

    override fun onPause() {
        super.onPause()

        listOfPubCrawlsIOGetList.cancelRequest()
    }

    override fun onPubCrawlClicked(ndx:Int) {
        if (requestInProgress) {return}

        showPubCrawlActivity(listOfPubCrawls[ndx])
    }

    override fun onQueryTextSubmit(query: String) {
        if (requestInProgress) {return}

        adapter.removeKeyboard()
        lastSearchText = query
        requestListOfPubCrawls(query)
    }

    override fun onResetPressed() {
        onQueryTextSubmit("")
    }

    private fun showPubCrawlActivity(pubCrawl: PubCrawl) {
        val intent = Intent(activity, PubCrawlActivity::class.java)
        intent.putExtra(PubCrawlActivity.PUBCRAWL_EXTRA,pubCrawl)
        startActivityForResult(intent,PubCrawlFragment.ACTIVITY_ID)
    }

    private fun requestListOfPubCrawls(search:String)  {
        listOfPubCrawlsIOGetList.makeRequest(search,  userSetting, this)
        showProgressBar()
    }

    override fun receivedNew(listOfPubCrawls: List<PubCrawl>) {
        hideProgressBar()
        this.listOfPubCrawls = listOfPubCrawls

        activity.runOnUiThread {
            run {
                adapter.listItems = mapListItemsFrom(listOfPubCrawls)
                recyclerView.adapter = adapter
            }
        }
    }

    private fun mapListItemsFrom(listOfPubCrawls: List<PubCrawl>):List<ListItem> {
        val mapFunction = { value: PubCrawl -> ClickableListItem(value.name) }
        val searchListItem = SearchPubCrawlsListItem(lastSearchText)
        return  listOf(searchListItem)  + listOfPubCrawls.map(mapFunction)
    }

}