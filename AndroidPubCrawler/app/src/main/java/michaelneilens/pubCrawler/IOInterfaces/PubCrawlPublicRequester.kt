package michaelneilens.pubCrawler.IOInterfaces

interface PubCrawlPublicRequester:IORequester {
    fun pubCrawlAddedToFavourites()
    fun pubCrawlCopied()
    fun obtainedEmailText(htmlText:String, plainText:String)
}