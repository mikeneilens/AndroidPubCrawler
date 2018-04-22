package michaelneilens.pubCrawler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import michaelneilens.pubCrawler.EventListenerInterfaces.SettingsToggleListner


class SettingsRecyclerViewAdapter(var listItems:List<ListItem>, private val settingsToggleListener:SettingsToggleListner?): RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder {

        val listItem = listItems[viewType]

        
        return when (listItem) {
                is ListHeading ->  createHeadingListView(parent)
                is ToggleListItem ->  createToggleOnListItemView(parent)
                else -> {
                    println("pc invalid list item!!!!")
                    createToggleOnListItemView(parent)
                }
        }
    }

    private fun createToggleOnListItemView(parent: ViewGroup): ToggleItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_toggle, parent, false)
        return ToggleItemHolder(view)
    }
    private fun createHeadingListView(parent: ViewGroup): HeadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_heading, parent, false)
        return HeadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {

        val listItem = listItems[position]

        when (listItem) {
            is ListHeading -> {
                val headingViewHolder = holder as? HeadingViewHolder
                headingViewHolder?.textView?.text = listItem.text
            }
            is ToggleListItem -> {
                val toggleItemHolder = holder as? ToggleItemHolder
                toggleItemHolder?.checkBox?.text = listItem.text
                toggleItemHolder?.checkBox?.isChecked = listItem.isTicked
                addSettingToggleListener(toggleItemHolder, position)
            }
        }
    }
    private fun addSettingToggleListener(toggleItemHolder:ToggleItemHolder?, position:Int) {
        toggleItemHolder?.checkBox?.setOnClickListener( {
            val toggleListItem = listItems[position] as ToggleListItem
            settingsToggleListener?.setingTogglePressed(toggleListItem.key)
        })
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount():Int {
        return listItems.size
    }

    open inner class ViewHolder(mView:View):RecyclerView.ViewHolder(mView)
    inner class HeadingViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }
    inner class ToggleItemHolder( mView:View):ViewHolder(mView) {
        val checkBox = mView.findViewById<View>(R.id.checkBox) as CheckBox
    }

}
