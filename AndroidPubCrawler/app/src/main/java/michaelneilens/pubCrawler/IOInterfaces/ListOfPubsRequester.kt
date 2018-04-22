package michaelneilens.pubCrawler.IOInterfaces

import michaelneilens.pubCrawler.ListOfPubs

interface ListOfPubsRequester:IORequester {
    fun receivedNew(listOfPubs: ListOfPubs)
}