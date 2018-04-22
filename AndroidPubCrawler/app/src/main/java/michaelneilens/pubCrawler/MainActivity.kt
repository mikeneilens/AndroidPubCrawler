package michaelneilens.pubCrawler

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem

import michaelneilens.pubCrawler.EventListenerInterfaces.LocationProcessor


class MainActivity : AppCompatActivity() {

    var listOfPubsFragment:ListOfPubsFragment? = null
    private var listOfPubCrawlsFragment:ListOfPubCrawlsFragment? = null
    private var settingsFragment:SettingsFragment? = null
    var lastFragmentSelected:Fragment? = null
    private var pubCrawlLink:String = ""

    private lateinit var pcLocationManager:PCLocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            getLinkCrawlId()
            setupBottomNavigation()

            pcLocationManager = PCLocationManager(this,this )
            pcLocationManager.turnOnLocationServices()

            loadInitialFragment()
        } else {
            setupBottomNavigation()
        }

        setSupportActionBar(findViewById(R.id.main_toolbar))

        title = "Pub Crawler"
    }

    private fun getLinkCrawlId() {
        val linkData = intent.data
        if (linkData != null) {
            val crawlId = linkData.getQueryParameter("crawlId")
            if (crawlId != null) {
                pubCrawlLink = crawlId
            }
        }
    }

    private fun loadInitialFragment() {
        if (pubCrawlLink.isEmpty()) {
            loadFragment(listOfPubsFragment, ListOfPubsFragment::class.java.name)
        } else {
            loadFragment(listOfPubCrawlsFragment, ListOfPubCrawlsFragment::class.java.name)
        }
    }

    fun providePubCrawlLink(lastSearchText:String):String {
        return  if (pubCrawlLink.isNotEmpty()) {
                    val linkToUse = pubCrawlLink
                    pubCrawlLink = ""
                    linkToUse
                } else {
                    lastSearchText
                }
    }

    override fun onResume() {
        println("pc MainActivity resumed")
        super.onResume()
        pcLocationManager.turnOnLocationServices()
    }

    override fun onStop() {
        println("pc MainActivity stopped")
        super.onStop()
        pcLocationManager.turnOffLocationServices()
    }
    fun setLocationProcessor(locationProcessor: LocationProcessor) {
        pcLocationManager.locationProcessor = locationProcessor
    }
    fun removeLocationProcessor() {
        pcLocationManager.locationProcessor = null
    }
    fun locationEnabled():Boolean {
        return pcLocationManager.locationEnabled
    }

    private fun setupBottomNavigation() {

        setInitialItemSelected()

        navigation.setOnNavigationItemSelectedListener( {
            item: MenuItem ->
                when (item.itemId) {
                    R.id.action_listofpubs -> {
                         loadFragment(listOfPubsFragment, ListOfPubsFragment::class.java.name)
                    }
                    R.id.action_listofpubcrawl -> {
                         loadFragment(listOfPubCrawlsFragment, ListOfPubCrawlsFragment::class.java.name)
                    }
                    R.id.action_filter -> {
                         loadFragment(settingsFragment, SettingsFragment::class.java.name)
                    }
                    else -> false
                }

        })
    }
    private fun setInitialItemSelected() {
        if (pubCrawlLink.isNotEmpty()) {
            navigation.selectedItemId = R.id.action_listofpubcrawl
        } else {
            navigation.selectedItemId = R.id.action_listofpubs
        }
    }

    private fun loadFragment(fragment:Fragment?, typeName:String):Boolean {
        if (isAlreadySelected(fragment)) {
            return false
        }

        if (requestIsInProgress(lastFragmentSelected)) {
            return false
        }

        val fragmentToShow =  createFragment(fragment, typeName)
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragmentToShow)
        ft.commit()
        lastFragmentSelected = fragmentToShow

        return true
    }

    private fun isAlreadySelected(fragment:Fragment?):Boolean {
        return  (lastFragmentSelected == fragment && lastFragmentSelected != null)
    }

    private fun requestIsInProgress(fragment:Fragment?):Boolean {
        if (fragment is AbstractFragment) {
            if (fragment.requestInProgress) {
                return true
            }
        }
        return false
    }

    private fun createFragment(fragment:Fragment?,typeName:String):Fragment? {
        if (fragment == null) {
            when (typeName) {
                ListOfPubsFragment::class.java.name -> {
                    listOfPubsFragment = ListOfPubsFragment()
                    return listOfPubsFragment
                }
                ListOfPubCrawlsFragment::class.java.name -> {
                    listOfPubCrawlsFragment = ListOfPubCrawlsFragment()
                    return listOfPubCrawlsFragment
                }
                SettingsFragment::class.java.name -> {
                    settingsFragment = SettingsFragment()
                    return settingsFragment
                }
                else -> {
                    println("pc invalid fragment type $typeName " )
                    return null
                }
            }
        } else {
            return fragment
        }
    }

}
