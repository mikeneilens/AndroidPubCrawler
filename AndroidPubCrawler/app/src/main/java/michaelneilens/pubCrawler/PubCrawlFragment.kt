package michaelneilens.pubCrawler

/**
 * Created by michaelneilens on 07/03/2018.
 */

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import michaelneilens.pubCrawler.IOInterfaces.ListOfPubsRequester
import michaelneilens.pubCrawler.IOInterfaces.PubCrawlPublicRequester
import michaelneilens.pubCrawler.IOInterfaces.PubCrawlUpdateRequester
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogConfirmListener
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogEditListener
import michaelneilens.pubCrawler.EventListenerInterfaces.PubCrawlListener
import michaelneilens.pubCrawler.MenuInterfaces.PubCrawlToolBarProcessor

class PubCrawlFragment: AbstractFragment(),  ListOfPubsRequester,
                                             PubCrawlToolBarProcessor,
                                             PubCrawlListener,
                                             DialogEditListener,
                                             DialogConfirmListener,
                                             PubCrawlUpdateRequester,
                                             PubCrawlPublicRequester
{
    var pubCrawl = PubCrawl()
    private var mListOfPubs = ListOfPubs(listOf(), "")
    private var hasNotStartedBefore = true

    private val pubCrawlIOAddToFavourites = PubCrawlIOAddToFavourites("add this pub crawl to your favourites")
    private val pubCrawlIOCopy = PubCrawlIOCopy("copy this pub crawl")
    private val pubCrawlIODelete = PubCrawlIODelete("delete this pub crawl")
    private val pubCrawlIOGetEmailText = PubCrawlIOGetEmailText("create email text for this pub crawl")
    private val pubCrawlIORemovePub = PubCrawlIORemovePub("remove a pub from this pub crawl")
    private val pubCrawlIORename = PubCrawlIORename("rename this pub crawl")
    private val pubCrawlIOUpdateSequence = PubCrawlIOUpdateSequence("save the list of pubs")
    private val listOfPubsIOGetList = ListOfPubsIOGetList("retrieve the pubs for this pub crawl")

    private lateinit var mAdapter: PubCrawlRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hasNotStartedBefore) {
            getInitialListOfPubs()
            hasNotStartedBefore = false
        }
    }

    private fun getInitialListOfPubs() {
        requestListOfPubs()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_listofpubs, container, false)
        setTheAdapter(view)
        activity.title = "Pub Crawl"

        return view
    }

    private fun setTheAdapter(view:View) {
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            mAdapter = PubCrawlRecyclerViewAdapter(mapListItemsFrom(pubCrawl, mListOfPubs), this)
            view.adapter = mAdapter
        }
    }

    override fun onPubClicked(ndx: Int) {
        showPubDetailFragment(mListOfPubs.pubs[ndx])
    }

    private fun showPubDetailFragment(pub: PubDetail) {
        val pubDetailFragment = PubDetailFragment()
        pubDetailFragment.pub = pub
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.pubcrawl_fragment_frame, pubDetailFragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onPause() {
        super.onPause()
        listOfPubsIOGetList.cancelRequest()
        pubCrawlIOAddToFavourites.cancelRequest()
        pubCrawlIOCopy.cancelRequest()
        pubCrawlIODelete.cancelRequest()
        pubCrawlIOGetEmailText.cancelRequest()
        pubCrawlIORemovePub.cancelRequest()
        pubCrawlIORename.cancelRequest()
        pubCrawlIOUpdateSequence.cancelRequest()
    }

    private fun requestListOfPubs()  {
        listOfPubsIOGetList.makeRequestForPubCrawl(pubCrawl,this)
        showProgressBar()
    }

    override fun receivedNew(listOfPubs: ListOfPubs) {
        hideProgressBar()
        this.mListOfPubs = listOfPubs

        activity.runOnUiThread {
            run {
                mAdapter.listItems = mapListItemsFrom(pubCrawl, listOfPubs)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun mapListItemsFrom(pubCrawl:PubCrawl, listOfPubs: ListOfPubs):List<ListItemsWithHeading> {

        val pubCrawlHeading = ListHeading(HEADING_PUBCRAWLNAME)
        val pubCrawlNameListItem = EditableListItem(pubCrawl.name,pubCrawl.updateService.isNotEmpty(), pubCrawl.removeService.isNotEmpty())
        val pubCrawlListWithHeading = ListItemsWithHeading(pubCrawlHeading,listOf(pubCrawlNameListItem))

        val pubsHeading = ListHeading(HEADING_PUBS)
        val mapFunction = getMapFunction(listOfPubs)
        val pubListItems = listOfPubs.pubs.mapIndexed(mapFunction)
        val pubsListItemsWithHeading = ListItemsWithHeading(pubsHeading,pubListItems)

        return listOf(pubCrawlListWithHeading, pubsListItemsWithHeading)
    }

    private fun getMapFunction(listOfPubs: ListOfPubs):(Int, PubDetail)->ListItem {
        return { ndx:Int, value: PubDetail -> MovableListItem(value.name, listOfPubs.distanceToPreviousPubText(ndx), value.removePubService.isNotEmpty()) }
    }

    override fun showMap() {
        if (mListOfPubs.pubs.isNotEmpty()) {


            val parcelArrayList: ArrayList<PubDetail> = arrayListOf()
            for (pub in mListOfPubs.pubs) {
                parcelArrayList.add(pub)
            }

            val intent = Intent(activity, MapActivity::class.java)
            intent.putExtra(MapActivity.EXTRAS_PUBS, parcelArrayList)

            startActivity(intent)
        } else {
            val dialog = DialogWarning().create(activity,"Unable to show map","There are no pubs to show")
            dialog.show()
        }
    }

    override fun editNamePressed() {
        val dialog = DialogBoxEdit(this).create(activity,"Edit pub crawl name","Please enter a new name", pubCrawl.name)
        dialog.show()
    }

    override fun donePressed(text: String) {
        if (text.isNotEmpty()) {
            renamePubCrawl(text)
        }
    }

    private fun renamePubCrawl(text:String)  {
        pubCrawlIORename.makeRequest(pubCrawl, text, this)
        showProgressBar()
    }

    override fun cancelPressed() {}

    override fun pubCrawlUpdated() {
        val conData = Bundle()
        conData.putString("result", "Pub crawl renamed")
        val intent = Intent()
        intent.putExtras(conData)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }

    override fun deletePubCrawlPressed() {
        val dialog = DialogBoxConfirm(this).create(activity,"Warning","Are you sure you wish to remove this pub crawl?",0)
        dialog.show()
    }

    override fun yesPressed(text: String, ndx:Int) {
        deletePubCrawl()
    }

    private fun deletePubCrawl()  {
        pubCrawlIODelete.makeRequest(pubCrawl, this)
        showProgressBar()
    }

    override fun pubCrawlDeleted() {
        println("Delete done")

        val conData = Bundle()
        conData.putString("result", "Pub crawl deleted")
        val intent = Intent()
        intent.putExtras(conData)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }

    override fun addToFavourites() {
        pubCrawlIOAddToFavourites.makeRequest(pubCrawl, this)
        showProgressBar()
    }

    override fun pubCrawlAddedToFavourites() {
        val conData = Bundle()
        conData.putString("result", "Pub crawl added to favourite")
        val intent = Intent()
        intent.putExtras(conData)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }

    override fun copyPubCrawl()  {
        pubCrawlIOCopy.makeRequest(pubCrawl, this)
        showProgressBar()
    }

    override fun pubCrawlCopied() {
        val conData = Bundle()
        conData.putString("result", "Pub crawl copied")
        val intent = Intent()
        intent.putExtras(conData)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }

    override fun createEmail()  {
        pubCrawlIOGetEmailText.makeRequest(pubCrawl,this)
        showProgressBar()
    }

    override fun obtainedEmailText(htmlText: String, plainText:String) {
        hideProgressBar()
        startEmailActivity("Pub crawl: " + pubCrawl.name + "", htmlText, plainText)
    }

    override fun sort()  {
        mListOfPubs = mListOfPubs.reorder()
        saveListOfPubsSequence()
    }

    private fun startEmailActivity(heading:String, htmlText:String, plainText:String){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, "")
            intent.putExtra(Intent.EXTRA_CC, "")
            intent.putExtra(Intent.EXTRA_SUBJECT, heading)
            intent.putExtra(Intent.EXTRA_TEXT, plainText)
            if (Build.VERSION.SDK_INT >= 16) {
                intent.putExtra(Intent.EXTRA_HTML_TEXT, htmlText)
            }

            startActivity(Intent.createChooser(intent, "How to send mail?"))
    }

    override fun movePubUp(ndx:Int) {
        if (ndx > 0) {
            mListOfPubs = mListOfPubs.swap(ndx, ndx - 1 )
            saveListOfPubsSequence()
        }
    }

    override fun movePubDown(ndx:Int) {
        if ((ndx +1) < mListOfPubs.pubs.size) {
            mListOfPubs = mListOfPubs.swap(ndx, ndx + 1 )
            saveListOfPubsSequence()
        }
    }
    private fun saveListOfPubsSequence() {
        pubCrawlIOUpdateSequence.makeRequest(pubCrawl,mListOfPubs, this)
        showProgressBar()
    }

    override fun deletePub(ndx:Int) {
        val pub = mListOfPubs.pubs[ndx]
        deletePubFromPubCrawl(pub)
    }

    private fun deletePubFromPubCrawl(pub:PubDetail) {
        pubCrawlIORemovePub.makeRequest(pub, this)
        showProgressBar()
    }

    companion object {
        private const val HEADING_PUBCRAWLNAME = "Pub Crawl Name"
        private const val HEADING_PUBS = "Pubs"
        const val ACTIVITY_ID = 10
    }

}