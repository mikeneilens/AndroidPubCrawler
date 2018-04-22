package michaelneilens.pubCrawler.EventListenerInterfaces

/**
 * Created by michaelneilens on 26/03/2018.
 */
interface ListOfPubCrawlsListener {
    fun onPubCrawlClicked(ndx:Int)
    fun onQueryTextSubmit(query: String)
    fun onResetPressed()
}