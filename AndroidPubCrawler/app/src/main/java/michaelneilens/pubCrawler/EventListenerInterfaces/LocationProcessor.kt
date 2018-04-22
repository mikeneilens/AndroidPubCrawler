package michaelneilens.pubCrawler.EventListenerInterfaces

interface LocationProcessor {
    fun useNewLocation(latitude:Double, longitude:Double)
}