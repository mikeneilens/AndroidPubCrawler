package michaelneilens.pubCrawler

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogListListener
import michaelneilens.pubCrawler.Extensions.asArray

/**
 * Created by michaelneilens on 26/03/2018.
 */
class DialogBoxList(private val listener:DialogListListener ) {
    fun create(activity: Activity, title:String, listOfNames:List<String> ): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)

        builder.addNegativeButton()
        builder.addListToDialog(listOfNames)

        return builder.create()
    }
    private fun AlertDialog.Builder.addNegativeButton() {
        this.setNegativeButton("Cancel",{
            _: DialogInterface, _:Int -> listener.cancelPressed()
        })
    }
    private fun AlertDialog.Builder.addListToDialog( listOfNames:List<String>) {
        val arrayOfNames = listOfNames.asArray()
        this.setItems(arrayOfNames,{
            _:DialogInterface, id:Int -> listener.listItemPressed(id)
        })
    }


}