package michaelneilens.pubCrawler

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

class DialogWarning {
    fun create(activity: Activity, title:String, message:String ): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.addNeutralButton()
        return builder.create()
    }
    private fun AlertDialog.Builder.addNeutralButton() {
        this.setNeutralButton("OK",{
            //don't do anything but leave the template code in just in case!
            _:DialogInterface, _:Int -> Unit
        })
    }
}