package michaelneilens.pubCrawler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import michaelneilens.pubCrawler.EventListenerInterfaces.PubCrawlListener

class PubCrawlRecyclerViewAdapter(var listItems:List<ListItemsWithHeading>, private val pubCrawlListener: PubCrawlListener?):RecyclerView.Adapter<PubCrawlRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        val listItem = listItems.getListItemAt(viewType )
        return when (listItem) {
            is MovableListItem ->  createMovableListItemView(parent)
            is ListHeading ->  createHeadingListView(parent)
            is EditableListItem ->  createEditListView(parent)
            else -> {
                println("pc invalid list item!!!!")
                createMovableListItemView(parent)
            }
        }
    }

    private fun createMovableListItemView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_moveable, parent, false)
        return MovableItemHolder(view)
    }

    private fun createHeadingListView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_heading, parent, false)
        return HeadingHolder(view)
    }

    private fun createEditListView(parent:ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_editable, parent, false)
        return EditableItemHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {

        val listItem = listItems.getListItemAt(position)

        when (listItem) {
            is MovableListItem -> {
                val movableItemHolder = holder as?  MovableItemHolder
                bindMovableListItem(movableItemHolder, listItem, position)
            }
            is ListHeading -> {
                val headingHolder = holder as? HeadingHolder
                headingHolder?.textView?.text = listItem.text
            }
            is EditableListItem -> {
                val editableItemHolder = holder as? EditableItemHolder
                editableItemHolder?.textView?.text = listItem.text
                bindEditItemHolder(editableItemHolder, listItem )
            }
        }
    }

    private fun bindMovableListItem(movableItemHolder: MovableItemHolder?, movableListItem:MovableListItem, position:Int) {

        val (ndx, _) = listItems.getIndexForItemAt(position)
        movableItemHolder?.textView?.text = movableListItem.text
        movableItemHolder?.detailTextView?.text = movableListItem.textDetail

        if (movableListItem.isMoveAble) {
            addUpPressedListener(movableItemHolder, ndx)
            addDownPressedListener(movableItemHolder, ndx)
            addDeletePubPressedListener(movableItemHolder, ndx)
        } else {
            movableItemHolder?.moveUpButton?.visibility= View.INVISIBLE
            movableItemHolder?.moveDownButton?.visibility = View.INVISIBLE
            movableItemHolder?.deleteButton?.visibility = View.INVISIBLE
        }
        if (listItems.isFirstItemForaHeading(position)  ) {
            movableItemHolder?.moveUpButton?.visibility = View.INVISIBLE
        }
        if (listItems.isLastItemForaHeading(position) ) {
            movableItemHolder?.moveDownButton?.visibility = View.INVISIBLE
        }
        addPubListener(movableItemHolder, ndx)
    }

    private fun bindEditItemHolder(editableItemHolder:EditableItemHolder?, editableListItem: EditableListItem) {
        editableItemHolder?.textView?.text = editableListItem.text
        if (editableListItem.isEditable) {
            addEditListener(editableItemHolder)
        } else {
            editableItemHolder?.editButton?.visibility = View.INVISIBLE
        }
        if (editableListItem.isRemovable) {
            addDeletePubCrawlListener(editableItemHolder)
        } else {
            editableItemHolder?.deleteButton?.visibility =  View.INVISIBLE

        }
    }

    private fun addPubListener(holder:MovableItemHolder?, ndx:Int) {
        holder?.mView?.setOnClickListener( { pubCrawlListener?.onPubClicked(ndx) })
    }

    private fun addEditListener(holder:EditableItemHolder?) {
        holder?.editButton?.setOnClickListener({ pubCrawlListener?.editNamePressed() })
    }

    private fun addDeletePubCrawlListener(holder:EditableItemHolder?) {
        holder?.deleteButton?.setOnClickListener( { pubCrawlListener?.deletePubCrawlPressed() })
    }

    private fun addUpPressedListener(holder:MovableItemHolder?, ndx:Int) {
        holder?.moveUpButton?.setOnClickListener( { pubCrawlListener?.movePubUp(ndx) })
    }

    private fun addDownPressedListener(holder:MovableItemHolder?, ndx:Int) {
        holder?.moveDownButton?.setOnClickListener({ pubCrawlListener?.movePubDown(ndx) })
    }

    private fun addDeletePubPressedListener(holder:MovableItemHolder?, ndx:Int) {
        holder?.deleteButton?.setOnClickListener( { pubCrawlListener?.deletePub(ndx) })
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount():Int {
        var count = 0
        for (listItemsWithHeading in this.listItems) {
            count += 1
            for (listItem in listItemsWithHeading.listItems) {
                count += 1
            }
        }
        return count
    }

    open inner class ViewHolder( val mView:View):RecyclerView.ViewHolder(mView)
    inner class HeadingHolder(mView:View): ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }
    inner class EditableItemHolder(mView:View): ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
        val editButton = mView.findViewById<View>(R.id.editButton) as ImageButton
        val deleteButton = mView.findViewById<View>(R.id.deleteButton) as ImageButton
    }
    inner class MovableItemHolder(mView:View): ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
        val detailTextView = mView.findViewById<View>(R.id.detailText) as TextView
        val moveDownButton = mView.findViewById<View>(R.id.downButton) as ImageButton
        val moveUpButton = mView.findViewById<View>(R.id.upButton) as ImageButton
        val deleteButton = mView.findViewById<View>(R.id.deleteButton) as ImageButton
    }
}
