package michaelneilens.pubCrawler

/**
 * Created by michaelneilens on 07/03/2018.
 */

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import michaelneilens.pubCrawler.EventListenerInterfaces.SettingsToggleListner

class SettingsFragment: AbstractFragment(), SettingsToggleListner
{

    private var hasNotStartedBefore = true
    private lateinit var adapter: SettingsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hasNotStartedBefore) {
            hasNotStartedBefore = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_listofpubs, container, false)
        setTheAdapter(view)
        activity.title = "Search filters"

        return view
    }

    private fun setTheAdapter(view:View) {
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            adapter = SettingsRecyclerViewAdapter(mapListItemsFrom(), this)
            view.adapter = adapter
        }
    }

    override fun setingTogglePressed(key: String) {
        for (option in userSetting.options) {
            if (option.key == key) {
                option.toggleValue()
                option.save(activity)
            }
        }
        adapter.listItems = mapListItemsFrom()
        adapter.notifyDataSetChanged()
    }

    private fun mapListItemsFrom():List<ListItem> {

        val settingHeading = ListHeading(HEADING_SETTINGS)

        val tempList:ArrayList<ListItem> = arrayListOf()
        for (option in userSetting.options) {
            tempList.add(ToggleListItem(option.description, option.key,option.value == "yes"))
        }

        return listOf(settingHeading) + tempList
    }

    companion object {
        private const val HEADING_SETTINGS = "Select your search filters"
    }
}