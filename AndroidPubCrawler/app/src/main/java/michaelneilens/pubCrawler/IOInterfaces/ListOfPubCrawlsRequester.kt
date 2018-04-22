package michaelneilens.pubCrawler.IOInterfaces

import michaelneilens.pubCrawler.PubCrawl

interface ListOfPubCrawlsRequester:IORequester {
    fun receivedNew(listOfPubCrawls: List<PubCrawl>)
}