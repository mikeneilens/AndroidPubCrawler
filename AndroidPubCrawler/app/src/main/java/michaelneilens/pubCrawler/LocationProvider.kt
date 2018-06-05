package michaelneilens.pubCrawler

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

abstract class LocationProvider(val activity: AppCompatActivity) {

    protected val fusedLocationClient:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    protected var requester:LocationRequester? = null

    open fun getLocation(requester:LocationRequester?) {
        this.requester = requester
    }
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

    }

    companion object {
        const val MY_LOCATION_PERMISSION_REQUEST = 123
    }
}