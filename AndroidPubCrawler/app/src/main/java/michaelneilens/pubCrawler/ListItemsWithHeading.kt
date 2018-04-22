package michaelneilens.pubCrawler

/**
 * Created by michaelneilens on 11/03/2018.
 *
 * */

data class ListItemsWithHeading(val heading: ListHeading, val listItems:List<ListItem>)
open class ListItem (val text:String)

//The RecyclerViewAdapters all use either a list of ListItems or List of ListItemsWithHeading as their source.
//A different subclass is used to indicate what kind of of viewHolders to use for each row in the recycler.
class ListHeading(text:String): ListItem(text)
class CenteredListItem(text:String): ListItem(text)
class ClickableListItem(text:String): ListItem(text)
class DetailedClickableListItem(text:String, val textDetail:String ):ListItem(text)
class EditableListItem(text:String, val isEditable:Boolean, val isRemovable:Boolean):ListItem(text)
class ExpandingListItem (text:String): ListItem(text)
class HygieneListItem(text:String, val textDetail:String, val ratingKey:String): ListItem(text)
class MovableListItem(text:String, val textDetail:String, val isMoveAble:Boolean):ListItem(text)
class PubCrawlListItem(text:String, val isDeletable:Boolean): ListItem(text)
class SearchPubsListItem (text:String, val locationEnabled:Boolean): ListItem(text)
class SearchPubCrawlsListItem (text:String): ListItem(text)
class SimpleWithImageListItem (text:String="Show on map...", val resId:Int): ListItem(text)
class PubImageListItem (text:String="Show on map...", val imageURL:String): ListItem(text)
class SimpleListItem(text:String): ListItem(text)
class ToggleListItem(text:String, val key:String, val isTicked:Boolean): ListItem(text)

/*
//Position is the absolute position in the list in these functions.
Eg. if an list of ListItems has one ListItemWith Heading then the first heading is at position zero and the first listItem is at position 1.
*/
fun List<ListItemsWithHeading>.getListItemAt(position:Int): ListItem {
    var count = 0
    for (listItemsWithHeading in this) {
        if (count == position) {
            return listItemsWithHeading.heading
        }
        for (listItem in listItemsWithHeading.listItems) {
            count += 1
            if (count == position) {
                return listItem
            }
        }
        count += 1
    }
    return ListItem("")
}
fun List<ListItemsWithHeading>.getIndexForItemAt(position:Int): Pair<Int, ListHeading> {
    var count = 0
    for ( listItemsWithHeading in this) {
        if (count == position) {
            return Pair(-1, listItemsWithHeading.heading)
        }
        var ndx = 0
        for (listItem in listItemsWithHeading.listItems) {
            count += 1
            if (count == position) {
                return Pair(ndx, listItemsWithHeading.heading)
            }
            ndx += 1
        }
        count += 1
    }

    return Pair(-1,  ListHeading("") )
}

fun List<ListItemsWithHeading>.isFirstItemForaHeading(position:Int):Boolean {
    var cumulativePosition = 0
    for (listItemsWithHeading in this) {
        if (listItemsWithHeading.listItems.isEmpty()) {
            return false
        }
        if ( position == (cumulativePosition + 1) ) {
            return true
        }
        cumulativePosition += listItemsWithHeading.listItems.size
        if ( position < cumulativePosition ) {
            return false
        }
        cumulativePosition += 1
    }
    return false
}
fun List<ListItemsWithHeading>.isLastItemForaHeading(position:Int):Boolean {
    var cumulativePosition = 0
    for (listItemsWithHeading in this) {
        if (listItemsWithHeading.listItems.isEmpty()) {
            return false
        }
        cumulativePosition +=  listItemsWithHeading.listItems.size
        if ( position < cumulativePosition ) {
            return false
        }
        if ( position == cumulativePosition ) {
            return true
        }
        cumulativePosition += 1
    }
    return false
}