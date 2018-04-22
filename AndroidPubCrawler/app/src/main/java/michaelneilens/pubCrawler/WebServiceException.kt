package michaelneilens.pubCrawler

sealed class WebServiceException: Exception() {
    class InvalidRequest:Exception()
}