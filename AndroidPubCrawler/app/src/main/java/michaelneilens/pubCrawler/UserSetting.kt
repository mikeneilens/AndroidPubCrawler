package michaelneilens.pubCrawler

import android.content.Context
import java.util.*

class UserSetting(val context:Context) {
    private val uId:String

    private val UID_KEY = "uId"
    val options:List<Option> = listOf(Option("pubs","Only Pubs","yes")
                                             ,Option("realAle","Only pubs serving real ale","yes")
                                              ,Option("memberDiscount","Member Discounts","no")
                                              ,Option("garden","Pub Garden","no")
                                              ,Option("LunchtimeMeal","Serving lunchtime meals","no")
                                              ,Option("EveningMeal","Serving evening meals","no")
    )

    init {
        uId = getUId()
        refreshOptions()
    }

    private fun getUId():String {
        val sharedPref = context.getSharedPreferences("com.example.michaelneilens.michaelneilens.pubCrawler",Context.MODE_PRIVATE)
        return if ( sharedPref.getString(UID_KEY, "").isEmpty() ) {
                    val newUId = getNewUId()
                    storeUId(newUId)
                    newUId
                } else {
                    sharedPref.getString(UID_KEY, "")
                }
    }
    private fun storeUId(uId:String) {
        val sharedPref = context.getSharedPreferences("com.example.michaelneilens.michaelneilens.pubCrawler",Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(UID_KEY, uId)
            apply()
        }

    }

    private fun getNewUId():String {
        //Creates a string 16 characters long containing random letters
        return List(16){""}.map { (Random().nextInt(26) + 65).toChar() }.joinToString("")
    }

    private fun refreshOptions() {
        val sharedPref = context.getSharedPreferences("com.example.michaelneilens.michaelneilens.pubCrawler",Context.MODE_PRIVATE)
        for (option in options) {
            option.value = sharedPref.getString(option.key, "")
            if (option.value.isEmpty()) {
                option.value = option.default
                option.save(context)
            }
        }
    }

    private fun uIDQueryParm():String {
        return "&$UID_KEY=$uId"
    }

    private fun optionsQueryParms():String {
       return options.map{it.queryParm()}.joinToString("")
    }

    fun queryParms():String {
        return uIDQueryParm() + optionsQueryParms()
    }

    class Option(val key:String, val description:String, val default:String, var value:String="") {
        fun queryParm():String {
            return "&$key=$value"
        }
        fun toggleValue() {
            value =  if (value == "yes") { "no" } else { "yes" }
        }
        fun save(context:Context){
            val sharedPref = context.getSharedPreferences("com.example.michaelneilens.michaelneilens.pubCrawler",Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(key, value)
                apply()
            }
        }
    }
}