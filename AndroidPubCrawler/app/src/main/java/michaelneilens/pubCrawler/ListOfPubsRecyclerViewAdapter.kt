package michaelneilens.pubCrawler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import michaelneilens.pubCrawler.EventListenerInterfaces.ListOfPubsListener

class ListOfPubsRecyclerViewAdapter(var listItems:List<ListItem>, private val listOfPubsListener: ListOfPubsListener?):RecyclerView.Adapter<ListOfPubsRecyclerViewAdapter.ViewHolder>() {

    private var searchView:SearchView? = null

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {

        val listItem = listItems[viewType]

        return  when (listItem) {
                    is DetailedClickableListItem -> createDetailedClickableItemListView(parent)
                    is SearchPubsListItem -> createSearchListView(parent)
                    is CenteredListItem -> createCentredListItemView(parent)
                    else ->  createDummyListView(parent)
                }
    }

    private fun createDetailedClickableItemListView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_detailed_clickable, parent, false)
        return  DetailedClickableViewHolder(view)
    }

    private fun createSearchListView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search_pubs, parent, false)
        return SearchViewHolder(view)
    }

    private fun createCentredListItemView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_centered, parent, false)
        return  CentredListItemViewHolder(view)
    }

    private fun createDummyListView(parent:ViewGroup):ViewHolder {
        println("pc invalid list item!!!")
        return createDetailedClickableItemListView(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        val listItem = listItems[position]
        when (listItem) {
            is DetailedClickableListItem -> {
                val detailedClickableViewHolder = holder as? DetailedClickableViewHolder
                detailedClickableViewHolder?.textView?.text = listItem.text
                detailedClickableViewHolder?.detailTextView?.text = listItem.textDetail
                if (listItem.text == ListOfPubsFragment.MORE_PUBS_TEXT) {
                    addMorePubsClickedListener(holder)
                } else {
                    addPubClickedListener(holder, position)
                }
            }
            is SearchPubsListItem -> {
                searchView?.setQuery(listItem.text,false)
                val searchViewHolder = holder as? SearchViewHolder
                addOrHideNearMeListener(searchViewHolder, listItem)
                addSearchListener()

            }
            is CenteredListItem -> {
                val detailedClickableViewHolder = holder as? CentredListItemViewHolder
                detailedClickableViewHolder?.textView?.text = listItem.text
                if (listItem.text == ListOfPubsFragment.MORE_PUBS_TEXT) {
                    addMorePubsClickedListener(holder)
                }
            }
            else -> {
                println("pc invalid list Item!!!")
            }
        }

    }
    private fun addOrHideNearMeListener(holder: SearchViewHolder?,searchPubsListItem:SearchPubsListItem) {
        if (searchPubsListItem.locationEnabled) {
            holder?.nearMeButton?.visibility = View.VISIBLE
            addNearMeListener(holder)
        } else {
            holder?.nearMeButton?.visibility = View.INVISIBLE
        }
    }

    private fun addSearchListener() {
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                listOfPubsListener?.onQueryTextSubmit(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun addNearMeListener(holder:SearchViewHolder?) {
        holder?.nearMeButton?.setOnClickListener( { listOfPubsListener?.onNearMeClick() })
    }

    private fun addPubClickedListener(holder:ViewHolder, position:Int) {
        val ndx = position - 1
        holder.mView.setOnClickListener( { listOfPubsListener?.onPubClicked(ndx) })
    }

    private fun addMorePubsClickedListener(holder:ViewHolder) {
        holder.mView.setOnClickListener( { listOfPubsListener?.onMoreClicked() })
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

    open inner class ViewHolder( val mView:View):RecyclerView.ViewHolder(mView)

    inner class SearchViewHolder( mView:View):ViewHolder(mView) {
        val nearMeButton = mView.findViewById<View>(R.id.nearMeButton) as ImageButton

        init {
            searchView = mView.findViewById<View>(R.id.searchPubs) as SearchView
            removeKeyboard()
        }
    }
    inner class DetailedClickableViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
        val detailTextView = mView.findViewById<View>(R.id.detailText) as TextView
    }
    inner class CentredListItemViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }

}
