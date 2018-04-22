package michaelneilens.pubCrawler.IOInterfaces
import michaelneilens.pubCrawler.PubDetail

interface PubDetailRequester:IORequester {
    fun receivedNew(pub: PubDetail, noOfRowsChanged:Boolean = true)
}