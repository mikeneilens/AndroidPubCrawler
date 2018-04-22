package michaelneilens.pubCrawler

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import michaelneilens.pubCrawler.EventListenerInterfaces.DialogEditListener

/**
 * Created by michaelneilens on 26/03/2018.
 */
class DialogBoxEdit(private val listener:DialogEditListener) {

    fun create(activity: Activity, title:String, message:String, currentText:String = "" ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(activity)

        val dialogView = createDialogView(activity)
        dialogBuilder.setView(dialogView)

        val editText = getEditText(dialogView)
        editText.setText(currentText)

        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)

        dialogBuilder.addPositiveButton(editText)
        dialogBuilder.addNegativeButton()

        return dialogBuilder.create()
    }

    private fun createDialogView(activity:Activity): View {
        val inflator = activity.getLayoutInflater()
        val dialogView = inflator.inflate(R.layout.dialog_edit, null)
        return dialogView
    }

    private fun getEditText(view:View):EditText {
        return view.findViewById(R.id.editText) as EditText
    }

    private fun AlertDialog.Builder.addPositiveButton(editText:EditText) {
        this.setPositiveButton("Done", {
            _:DialogInterface?, _:Int -> listener.donePressed(editText.text.toString())
        })
    }
    private fun AlertDialog.Builder.addNegativeButton() {
        this.setNegativeButton("Cancel",{
            _:DialogInterface, _:Int ->  listener.cancelPressed()
        })
    }

}