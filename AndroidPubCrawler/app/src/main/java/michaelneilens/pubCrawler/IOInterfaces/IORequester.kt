package michaelneilens.pubCrawler.IOInterfaces

interface IORequester {
    fun requestFailed(request:String, message: String)
}