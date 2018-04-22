package michaelneilens.pubCrawler.IOInterfaces

interface PubCrawlUpdateRequester:IORequester {
    fun pubCrawlDeleted()
    fun pubCrawlUpdated()
}