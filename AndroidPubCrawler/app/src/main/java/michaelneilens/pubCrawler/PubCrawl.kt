package michaelneilens.pubCrawler

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by michaelneilens on 16/03/2018.
 */
class PubCrawl:Parcelable {
    val name:String
    private val isPublic:Boolean
    val pubsService:String
    val addPubCrawlService:String
    val removePubCrawlService:String
    val copyService:String
    private val updateSettingsService:String
    val removeService:String
    val updateService:String
    val addUserService:String
    val sequencePubsService:String
    val emailTextService:String
    private val sequence:Int

    constructor(pubCrawlJSON: JSONObject){
        name = try { pubCrawlJSON.getString("Name") } catch(e: JSONException) {""}
        pubsService = try { pubCrawlJSON.getString("PubsService") } catch(e: JSONException) { ""}
        addPubCrawlService = try { pubCrawlJSON.getString("AddPubCrawlService") } catch(e: JSONException) {""}
        removePubCrawlService = try { pubCrawlJSON.getString("RemovePubCrawlService") } catch(e: JSONException) {""}
        copyService = try { pubCrawlJSON.getString("CopyService") } catch(e: JSONException) {""}
        updateSettingsService = try { pubCrawlJSON.getString("UpdateSettingsService") } catch(e: JSONException) {""}
        removeService = try { pubCrawlJSON.getString("RemoveService") } catch(e: JSONException) {""}
        updateService = try { pubCrawlJSON.getString("UpdateService") } catch(e: JSONException) {""}
        addUserService = try { pubCrawlJSON.getString("AddUserService") } catch(e: JSONException) {""}
        sequencePubsService = try { pubCrawlJSON.getString("SequencePubsService") } catch(e: JSONException) {""}
        emailTextService = try { pubCrawlJSON.getString("EmailTextService") } catch(e: JSONException) {""}
        sequence = try { pubCrawlJSON.getInt("Sequence") } catch(e: JSONException) {0}
        isPublic = try { pubCrawlJSON.getString("IsPublic") == "y" } catch(e: JSONException) {false}
    }

    constructor() {
        name = ""
        isPublic = false
        pubsService = ""
        addPubCrawlService = ""
        removePubCrawlService = ""
        copyService = ""
        updateSettingsService = ""
        removeService = ""
        updateService = ""
        addUserService = ""
        sequencePubsService = ""
        emailTextService = ""
        sequence = 0
    }

    constructor(parcel: Parcel)  {
        name = parcel.readString()
        pubsService = parcel.readString()
        addPubCrawlService = parcel.readString()
        removePubCrawlService = parcel.readString()
        copyService = parcel.readString()
        updateSettingsService = parcel.readString()
        removeService = parcel.readString()
        updateService = parcel.readString()
        addUserService = parcel.readString()
        sequencePubsService = parcel.readString()
        emailTextService = parcel.readString()
        sequence = parcel.readInt()
        isPublic = parcel.readString()=="y"
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(pubsService)
        parcel.writeString(addPubCrawlService)
        parcel.writeString(removePubCrawlService)
        parcel.writeString(copyService)
        parcel.writeString(updateSettingsService)
        parcel.writeString(removeService)
        parcel.writeString(updateService)
        parcel.writeString(addUserService)
        parcel.writeString(sequencePubsService)
        parcel.writeString(emailTextService)
        parcel.writeInt(sequence)
        parcel.writeString( if (isPublic) "y" else  "n" ) //parcels can't contain booleans
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PubCrawl> {


        override fun createFromParcel(parcel: Parcel): PubCrawl {
            return PubCrawl(parcel)
        }

        override fun newArray(size: Int): Array<PubCrawl?> {
            return arrayOfNulls(size)
        }
    }
}