package michaelneilens.pubCrawler

import android.app.Fragment
import android.os.Bundle
import michaelneilens.pubCrawler.IOInterfaces.IORequester


/**
 * Created by Michael Neilens on 17/03/2018.
 */
open class AbstractFragment: Fragment(), IORequester {
    private val progressFragment:Fragment = ProgressFragment()
    var requestInProgress = false
    lateinit var userSetting:UserSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSetting = UserSetting(activity)
        addProgressBar()
    }

    override fun onPause() {
        super.onPause()
        hideProgressBar()
    }

    private fun addProgressBar() {
        val ft = fragmentManager.beginTransaction()
        when (activity) {
            is MainActivity ->  ft.add(R.id.fragment_frame, progressFragment)
            is PubCrawlActivity ->  ft.add(R.id.pubcrawl_fragment_frame, progressFragment)
        }
        ft.commit()
        requestInProgress = true
        hideProgressBar()
    }
    protected fun showProgressBar() {
        if (!requestInProgress) {
            try {

                val ft = fragmentManager.beginTransaction()
                ft.show(progressFragment)
                ft.commit()
                requestInProgress = true
            } catch (e:Exception) {
                println("Fragment Manager error $e in showProgressBar" )
            }
        }
    }
    protected fun hideProgressBar() {
        if (requestInProgress) {
            try {
                val ft = fragmentManager.beginTransaction()
                ft.hide(progressFragment)
                ft.commit()
                requestInProgress = false

            } catch (e:Exception) {
                println("Fragment Manager error $e in hideProgressBar" )
            }
        }
    }
    override fun requestFailed(request:String, message:String) {
        hideProgressBar()
        activity.runOnUiThread {
            run {
                showIOErrorDialog(request, message)
            }
        }
    }
    private fun showIOErrorDialog(request:String, message:String) {
        val dialog = DialogWarning().create(activity, "Could not $request. " , message)
        dialog.show()
    }
}