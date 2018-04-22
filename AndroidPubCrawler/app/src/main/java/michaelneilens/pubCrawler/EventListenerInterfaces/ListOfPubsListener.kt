package michaelneilens.pubCrawler.EventListenerInterfaces

import android.view.View

/**
 * Created by michaelneilens on 26/03/2018.
 */
interface ListOfPubsListener {
    fun onPubClicked(ndx: Int)
    fun onMoreClicked()
    fun onQueryTextSubmit(query: String)
    fun onNearMeClick()
}