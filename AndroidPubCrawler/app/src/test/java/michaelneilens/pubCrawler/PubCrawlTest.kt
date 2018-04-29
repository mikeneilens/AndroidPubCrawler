package michaelneilens.pubCrawler

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PubCrawlTest {
    @Test
    fun testConstructor() {
        val jsonObject = createPubCrawlJSON()
        val pubCrawl = PubCrawl(jsonObject)
        assertEquals("Test Name", pubCrawl.name)
        assertEquals("pub service", pubCrawl.pubsService )
        assertEquals("Copy Service", pubCrawl.copyService)
        assertEquals("Add User Service", pubCrawl.addUserService)
        assertEquals("Email Text Service", pubCrawl.emailTextService)
        assertEquals("Add Pub Crawl Service", pubCrawl.addPubCrawlService )
        assertEquals("Remove Pub Crawl Service", pubCrawl.removePubCrawlService )
        assertEquals("Remove Service", pubCrawl.removeService )
        assertEquals("Update Service", pubCrawl.updateService)
        assertEquals("Sequence Pubs Service", pubCrawl.sequencePubsService)
    }
    fun createPubCrawlJSON():JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(PubCrawl.nameKey, "Test Name")
        jsonObject.put(PubCrawl.isPublicKey, "is public") //private
        jsonObject.put(PubCrawl.pubsServiceKey, "pub service")
        jsonObject.put(PubCrawl.copyServiceKey, "Copy Service")
        jsonObject.put(PubCrawl.addUserServiceKey, "Add User Service")
        jsonObject.put(PubCrawl.emailTextServiceKey, "Email Text Service")
        jsonObject.put(PubCrawl.addPubCrawlServiceKey, "Add Pub Crawl Service")
        jsonObject.put(PubCrawl.removePubCrawlServiceKey, "Remove Pub Crawl Service")
        jsonObject.put(PubCrawl.removeServiceKey, "Remove Service")
        jsonObject.put(PubCrawl.updateServiceKey, "Update Service")
        jsonObject.put(PubCrawl.sequencePubServiceKey, "Sequence Pubs Service")
        jsonObject.put(PubCrawl.sequenceKey ,10) //private
        return jsonObject
    }

}