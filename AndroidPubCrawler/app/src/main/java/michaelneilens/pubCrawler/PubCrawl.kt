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
        name = pubCrawlJSON.getWithDefault(nameKey,"")
        pubsService = pubCrawlJSON.getWithDefault(pubsServiceKey,"")
        addPubCrawlService = pubCrawlJSON.getWithDefault(addPubCrawlServiceKey,"")
        removePubCrawlService = pubCrawlJSON.getWithDefault(removePubCrawlServiceKey,"")
        copyService = pubCrawlJSON.getWithDefault(copyServiceKey, "")
        updateSettingsService = pubCrawlJSON.getWithDefault("UpdateSettingsService", "")
        removeService = pubCrawlJSON.getWithDefault(removeServiceKey, "")
        updateService = pubCrawlJSON.getWithDefault(updateServiceKey, "")
        addUserService = pubCrawlJSON.getWithDefault(addUserServiceKey, "")
        sequencePubsService = pubCrawlJSON.getWithDefault(sequencePubServiceKey, "")
        emailTextService = pubCrawlJSON.getWithDefault(emailTextServiceKey, "")
        sequence = pubCrawlJSON.getWithDefault(sequenceKey, 0)
        isPublic = pubCrawlJSON.getWithDefault(isPublicKey,"") == "y"
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
        const val nameKey = "Name"
        const val isPublicKey = "IsPublic"
        const val pubsServiceKey = "PubsService"
        const val copyServiceKey = "CopyService"
        const val addUserServiceKey = "AddUserService"
        const val emailTextServiceKey = "EmailTextService"
        const val addPubCrawlServiceKey = "AddPubCrawlService"
        const val removePubCrawlServiceKey = "RemovePubCrawlService"
        const val removeServiceKey = "RemoveService"
        const val updateServiceKey = "UpdateService"
        const val sequencePubServiceKey = "SequencePubsService"
        const val sequenceKey = "Sequence"
    }
}