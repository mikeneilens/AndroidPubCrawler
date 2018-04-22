package michaelneilens.pubCrawler.EventListenerInterfaces

/**
 * Created by michaelneilens on 26/03/2018.
 */
interface PubCrawlListener {
    fun onPubClicked(ndx:Int)
    fun editNamePressed()
    fun deletePubCrawlPressed()
    fun movePubUp(ndx:Int)
    fun movePubDown(ndx:Int)
    fun deletePub(ndx:Int)
}