package michaelneilens.pubCrawler

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogConfirmListener

class DialogBoxConfirm(private val listener:DialogConfirmListener) {

    fun create(activity: Activity, title: String, message: String, ndx: Int): AlertDialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(title)
        builder.setMessage(message)

        builder.addNegativeButton()
        builder.addPositiveButton( title, ndx)

        return builder.create()
    }

    private fun AlertDialog.Builder.addPositiveButton(title:String, ndx:Int) {
        this.setPositiveButton("Yes", { _:DialogInterface, _:Int -> listener.yesPressed(title, ndx) })
    }

    private fun AlertDialog.Builder.addNegativeButton() {
        this.setNegativeButton("No",  { _:DialogInterface, _:Int -> listener.cancelPressed() })
    }
}