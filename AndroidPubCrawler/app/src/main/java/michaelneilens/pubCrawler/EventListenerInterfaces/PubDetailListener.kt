package michaelneilens.pubCrawler.EventListenerInterfaces

/**
 * Created by michaelneilens on 26/03/2018.
 */
interface PubDetailListener {
    fun onShowMapClicked()
    fun onShowPubImageClicked()
    fun onUpdateVisistedClicked()
    fun onUpdateLikedClicked()
    fun onPubCrawlClicked(ndx:Int)
    fun onAddToPubCrawlClicked()
    fun deletePubCrawlPressed(ndx:Int)
}