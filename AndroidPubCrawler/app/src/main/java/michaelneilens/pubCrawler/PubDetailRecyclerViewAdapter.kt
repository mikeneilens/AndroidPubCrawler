package michaelneilens.pubCrawler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import michaelneilens.pubCrawler.EventListenerInterfaces.PubDetailListener

class PubDetailRecyclerViewAdapter(var listItemsWithHeadings:List<ListItemsWithHeading>, private val pubDetailListener: PubDetailListener?):RecyclerView.Adapter<PubDetailRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {

        val listItem = listItemsWithHeadings.getListItemAt(viewType)

        return when (listItem) {
            is SimpleListItem ->  createSimpleListItemView(parent)
            is ListHeading ->  createHeadingListView(parent)
            is CenteredListItem -> createCenteredListItemView(parent)
            is ExpandingListItem ->  createExpandingListItemView(parent)
            is HygieneListItem ->  createHygieneListItemView(parent)
            is SimpleWithImageListItem ->  createSimpleWithImageListItemView(parent)
            is PubImageListItem ->  createPubImageListItemView(parent)
            is PubCrawlListItem ->  createPubCrawlListItemView(parent)
            is ToggleListItem ->  createToggleListItemView(parent)
            else -> {
                println("pc invalid listItem !!!")
                createSimpleListItemView(parent)
            }
        }
    }

    private fun createExpandingListItemView(parent: ViewGroup): ExpandingItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_expanding, parent, false)
        return  ExpandingItemViewHolder(view)
    }

    private fun createCenteredListItemView(parent: ViewGroup): CenteredItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_centered, parent, false)
        return CenteredItemViewHolder(view)
    }

    private fun createSimpleListItemView(parent:ViewGroup): SimpleItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_simple, parent, false)
        return SimpleItemViewHolder(view)
    }

    private fun createHeadingListView(parent:ViewGroup): HeadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_heading, parent, false)
        return HeadingViewHolder(view)
    }

    private fun createHygieneListItemView(parent:ViewGroup): HygieneItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_hygiene, parent, false)
        return HygieneItemHolder(view)
    }

    private fun createSimpleWithImageListItemView(parent:ViewGroup): SimpleImageItemHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_simple_with_image, parent, false)
        return SimpleImageItemHolder(view)
    }

    private fun createPubImageListItemView(parent:ViewGroup): PubImageItemHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pub_image, parent, false)
        return PubImageItemHolder(view)
    }

    private fun createPubCrawlListItemView(parent:ViewGroup): PubCrawlItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pubcrawl, parent, false)
        return PubCrawlItemHolder(view)
    }

    private fun createToggleListItemView(parent:ViewGroup): ToggleItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_toggle, parent, false)
        return ToggleItemHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {

        val listItem = listItemsWithHeadings.getListItemAt(position)
        val (ndx, heading) = listItemsWithHeadings.getIndexForItemAt(position)

        when (listItem) {
            is HygieneListItem -> {
                val hygieneItemHolder = holder as? HygieneItemHolder
                hygieneItemHolder?.textView?.text = listItem.text
                hygieneItemHolder?.detailTextView?.text = (listItem.textDetail)
                hygieneItemHolder?.hygieneRatingView?.setImageResource(imageResourceMapper(listItem.ratingKey))
            }
            is ListHeading -> {
                val headingViewHolder = holder as? HeadingViewHolder
                headingViewHolder?.textView?.text = listItem.text
            }
            is SimpleListItem -> {
                val simpleItemHolder = holder as? SimpleItemViewHolder
                simpleItemHolder?.textView?.text = listItem.text
            }
            is ExpandingListItem -> {
                val expandingItemViewHolder = holder as? ExpandingItemViewHolder
                expandingItemViewHolder?.textView?.text = listItem.text
            }
            is CenteredListItem -> {
                val centeredItemViewHolder = holder as? CenteredItemViewHolder
                centeredItemViewHolder?.textView?.text = listItem.text
            }
            is ToggleListItem -> {
                val toggleItemHolder = holder as? ToggleItemHolder
                toggleItemHolder?.checkBox?.text = listItem.text
                toggleItemHolder?.checkBox?.isChecked = listItem.isTicked
                when (listItem.text) {
                    "Visited" -> addVisitedListener(toggleItemHolder)
                    "Liked" -> addLikedListener(toggleItemHolder)
                }
            }
            is SimpleWithImageListItem -> {
                val simpleImageItemHolder = holder as? SimpleImageItemHolder
                simpleImageItemHolder?.textView?.text = listItem.text
                simpleImageItemHolder?.imageView?.setImageResource(listItem.resId)
                when (heading.text) {
                    PubDetailFragment.HEADING_ADDRESS -> addShowMapClickListener(holder)
                    PubDetailFragment.HEADING_ONPUBCRAWLS -> addToPubCrawlListener(holder)
                }
            }
            is PubImageListItem -> {
                val pubImageItemHolder = holder as? PubImageItemHolder
                Picasso.with(pubImageItemHolder?.mView?.context).load(listItem.imageURL).into(pubImageItemHolder?.imageView)
                addShowPubImageClickListener(holder)
            }
            is PubCrawlListItem -> {
                val pubCrawlItemHolder = holder as? PubCrawlItemHolder
                pubCrawlItemHolder?.textView?.text = listItem.text
                if (listItem.isDeletable) {
                    addDeletePubCrawlListener(pubCrawlItemHolder,ndx)
                } else {
                    pubCrawlItemHolder?.deleteView?.visibility = View.INVISIBLE
                }
                addPubCrawlListener(holder, ndx)
            }
        }
    }

    private fun addDeletePubCrawlListener(pubCrawlItemHolder: PubCrawlItemHolder?, ndx:Int) {
        pubCrawlItemHolder?.deleteView?.setOnClickListener( { pubDetailListener?.deletePubCrawlPressed(ndx) })
    }
    
    private fun addShowMapClickListener(holder:ViewHolder) {
        holder.mView.setOnClickListener( { pubDetailListener?.onShowMapClicked() })
    }

    private fun addShowPubImageClickListener(holder:ViewHolder) {
        holder.mView.setOnClickListener( { pubDetailListener?.onShowPubImageClicked() })
    }

    private fun addVisitedListener(toggleItemHolder:ToggleItemHolder?) {
        toggleItemHolder?.checkBox?.setOnClickListener( { pubDetailListener?.onUpdateVisistedClicked() })
    }
    
    private fun addLikedListener(toggleItemHolder:ToggleItemHolder?) {
        toggleItemHolder?.checkBox?.setOnClickListener( { pubDetailListener?.onUpdateLikedClicked() })
    }
    
    private fun addToPubCrawlListener(holder:ViewHolder) {
        holder.mView.setOnClickListener({ pubDetailListener?.onAddToPubCrawlClicked() })
    }
    
    private fun addPubCrawlListener(holder:ViewHolder, ndx:Int) {
        holder.mView.setOnClickListener({ pubDetailListener?.onPubCrawlClicked(ndx) })
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount():Int {
        var count = 0
        for (listItemsWithHeading in this.listItemsWithHeadings) {
            count += 1
            for (listItem in listItemsWithHeading.listItems) {
                count += 1
            }
        }
        return count
    }

    open inner class ViewHolder(val mView:View):RecyclerView.ViewHolder(mView)
    inner class HeadingViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }

    inner class ExpandingItemViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }

    inner class SimpleItemViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }

    inner class CenteredItemViewHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
    }

    inner class HygieneItemHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
        val detailTextView = mView.findViewById<View>(R.id.detailText) as TextView
        val hygieneRatingView = mView.findViewById<View>(R.id.hygieneRatingImage) as ImageView
    }

    inner class SimpleImageItemHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
        val imageView = mView.findViewById<View>(R.id.imageView) as ImageView
    }
    inner class PubImageItemHolder( mView:View):ViewHolder(mView) {
        val imageView = mView.findViewById<View>(R.id.imageView) as ImageView
    }
    inner class PubCrawlItemHolder( mView:View):ViewHolder(mView) {
        val textView = mView.findViewById<View>(R.id.text) as TextView
        val deleteView = mView.findViewById(R.id.deleteButton) as ImageView
    }
    inner class ToggleItemHolder( mView:View):ViewHolder(mView) {
        val checkBox = mView.findViewById<View>(R.id.checkBox) as CheckBox
    }

    private fun imageResourceMapper(imageName:String):Int {
        return when (imageName) {
            "fhis_awaiting_inspection_en-gb" -> R.drawable.fhis_awaiting_inspection_en_gb
            "fhis_awaiting_publication_en-gb" -> R.drawable.fhis_awaiting_publication_en_gb
            "fhis_exempt_en_gb" -> R.drawable.fhis_exempt_en_gb
            "fhis_improvement_required_en-gb" -> R.drawable.fhis_improvement_required_en_gb
            "fhis_pass_en-gb" -> R.drawable.fhis_pass_en_gb
            "fhrs_0_en-gb" -> R.drawable.fhrs_0_en_gb
            "fhrs_1_en-gb" -> R.drawable.fhrs_1_en_gb
            "fhrs_2_en-gb" -> R.drawable.fhrs_2_en_gb
            "fhrs_3_en-gb" -> R.drawable.fhrs_3_en_gb
            "fhrs_4_en-gb" -> R.drawable.fhrs_4_en_gb
            "fhrs_5_en-gb" -> R.drawable.fhrs_5_en_gb
            "fhrs_awaitinginspection_en-gb" -> R.drawable.fhrs_awaitinginspection_en_gb
            else ->  R.drawable.fhrs_blank
        }
    }

}
