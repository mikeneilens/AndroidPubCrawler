package michaelneilens.pubCrawler.IOInterfaces

import michaelneilens.pubCrawler.PubHygieneRating

interface PubHygieneRatingRequester {
    fun receivedNew(pubHygieneRatings: List<PubHygieneRating>)
}