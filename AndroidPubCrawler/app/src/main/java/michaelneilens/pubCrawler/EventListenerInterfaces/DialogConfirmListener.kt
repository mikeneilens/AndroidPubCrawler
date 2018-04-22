package michaelneilens.pubCrawler.EventListenerInterfaces

interface DialogConfirmListener {
    fun yesPressed(text:String, ndx:Int)
    fun cancelPressed()
}