package michaelneilens.pubCrawler

import android.os.Parcel
import android.os.Parcelable
import michaelneilens.pubCrawler.Extensions.getWithDefault

import org.json.JSONObject

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
        name = pubCrawlJSON.getWithDefault("Name","")
        pubsService = pubCrawlJSON.getWithDefault("PubsService","")
        addPubCrawlService = pubCrawlJSON.getWithDefault("AddPubCrawlService","")
        removePubCrawlService = pubCrawlJSON.getWithDefault("RemovePubCrawlService","")
        copyService = pubCrawlJSON.getWithDefault("CopyService", "")
        updateSettingsService = pubCrawlJSON.getWithDefault("UpdateSettingsService", "")
        removeService = pubCrawlJSON.getWithDefault("RemoveService", "")
        updateService = pubCrawlJSON.getWithDefault("UpdateService", "")
        addUserService = pubCrawlJSON.getWithDefault("AddUserService", "")
        sequencePubsService = pubCrawlJSON.getWithDefault("SequencePubsService", "")
        emailTextService = pubCrawlJSON.getWithDefault("EmailTextService", "")
        sequence = pubCrawlJSON.getWithDefault("Sequence", 0)
        isPublic = pubCrawlJSON.getWithDefault("IsPublic","") == "y"
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