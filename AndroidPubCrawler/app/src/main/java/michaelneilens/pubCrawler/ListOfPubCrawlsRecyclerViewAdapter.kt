package michaelneilens.pubCrawler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import michaelneilens.pubCrawler.EventListenerInterfaces.ListOfPubCrawlsListener

class ListOfPubCrawlsRecyclerViewAdapter(var listItems:List<ListItem>, private val listOfPubCrawlsListener: ListOfPubCrawlsListener?):RecyclerView.Adapter<ListOfPubCrawlsRecyclerViewAdapter.ViewHolder>() {

    private var searchView:SearchView? = null

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        val listItem = listItems[viewType]

        return when (listItem) {
            is SearchPubCrawlsListItem -> createSearchListView(parent)
            is ClickableListItem ->  createClickableItemListView(parent)
            else -> {
                println("pc invalid listItem!!!")
                createClickableItemListView(parent)
            }
        }
    }

    private fun createClickableItemListView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_clickable, parent, false)
        return ClickableViewHolder(view)
    }

    private fun createSearchListView(parent:ViewGroup): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search_pubcrawls, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        val listItem = listItems[position]
        when (listItem) {
            is SearchPubCrawlsListItem ->  {
                val searchViewHolder = holder as? SearchViewHolder
                searchView?.setQuery(listItem.text,false)
                addSearchListeners()
                addResetListener(searchViewHolder)
            }
            is ClickableListItem -> {
                val clickableListHolder = holder as? ClickableViewHolder
                clickableListHolder?.textView?.text = listItem.text
                addPubCrawlListener(holder, position)
            }
        }
    }

    private fun addPubCrawlListener(holder:ViewHolder, position:Int) {
        val ndx = position - 1
        holder.mView.setOnClickListener( { listOfPubCrawlsListener?.onPubCrawlClicked(ndx) })
        holder.mView.isHapticFeedbackEnabled = true
    }

    private fun addSearchListeners() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                listOfPubCrawlsListener?.onQueryTextSubmit(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun addResetListener(holder: SearchViewHolder?) {
        holder?.resetButton?.setOnClickListener( { listOfPubCrawlsListener?.onResetPressed() })
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount():Int {
        return listItems.size
    }

    fun removeKeyboard() {
        searchView?.clearFocus()
    }

    open inner class ViewHolder(val mView:View):RecyclerView.ViewHolder(mView)

    inner class SearchViewHolder( mView:View):ViewHolder(mView) {
        val resetButton= mView.findViewById<View>(R.id.resetButton) as Button

        init{
            searchView = mView.findViewById<View>(R.id.searchPubCrawls) as SearchView
            removeKeyboard()
        }
    }
    inner class ClickableViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }
}
