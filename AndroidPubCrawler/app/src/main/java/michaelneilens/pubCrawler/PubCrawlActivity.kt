package michaelneilens.pubCrawler

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class PubCrawlActivity : AppCompatActivity() {

    private var pubCrawlFragment = PubCrawlFragment()
    lateinit var pubCrawl:PubCrawl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pub_crawl)

        val toolbar:Toolbar = findViewById(R.id.pubcrawl_toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        pubCrawl = intent.getParcelableExtra(PUBCRAWL_EXTRA)
        pubCrawlFragment.pubCrawl = pubCrawl

        loadFragment(pubCrawlFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.pub_crawl, menu)

        menu?.let {
            setMenuItemsVisibiity(it)
        }

        return true
    }

    private fun setMenuItemsVisibiity(menu:Menu) {
        if (pubCrawl.emailTextService.isEmpty() ) {
            val emailButton =  menu.findItem(R.id.action_email)
            emailButton?.setVisible(false)
        }
        if (pubCrawl.addUserService.isEmpty() ) {
            val favouritesButton =  menu.findItem(R.id.action_favourite)
            favouritesButton?.setVisible(false)
        }
        if (pubCrawl.sequencePubsService.isEmpty() ) {
            val sortButton =  menu.findItem(R.id.action_sort)
            sortButton?.setVisible (false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_map -> {
            pubCrawlFragment.showMap()
            true
        }
        R.id.action_favourite -> {
            pubCrawlFragment.addToFavourites()
            true
        }
        R.id.action_copy -> {
            pubCrawlFragment.copyPubCrawl()
            true
        }
        R.id.action_email -> {
            pubCrawlFragment.createEmail()
            true
        }
        R.id.action_sort -> {
            pubCrawlFragment.sort()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun loadFragment(pubCrawlFragment: PubCrawlFragment) {
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.pubcrawl_fragment_frame, pubCrawlFragment)
        ft.commit()
    }
    companion object {
        const val PUBCRAWL_EXTRA = "PubCrawl"
    }
}
