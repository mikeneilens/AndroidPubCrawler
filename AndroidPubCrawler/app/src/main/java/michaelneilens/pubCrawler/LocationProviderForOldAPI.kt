package michaelneilens.pubCrawler

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

class LocationProviderForOldAPI(activity: AppCompatActivity):LocationProvider(activity) {

    override fun getLocation(requester: LocationRequester?) {
        super.getLocation(requester)
        //Permission checks always return true for API < 23 but always have to check for it.
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && locationIsEnabledForOldAPI() ) {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? -> requester?.processNewLocation(location) }
        } else {
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {

        if (locationIsEnabledForOldAPI()) {
            getLocation(requester)
        } else {
            showConfirmShowLocationSettings()
        }
    }

    private fun locationIsEnabledForOldAPI():Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER ) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER ) )
    }

    private fun showConfirmShowLocationSettings() {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle("This app would like to use your location")
        builder.setMessage("Would you like to change your location settings?")

        builder.setPositiveButton("Yes", { _: DialogInterface, _:Int -> yesPressed()  })
        builder.setNegativeButton("No", { _: DialogInterface, _:Int -> noPressed()  })

        builder.create().show()
    }

    private fun yesPressed() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivityForResult(intent, MY_LOCATION_PERMISSION_REQUEST)
    }
    private fun noPressed() {
        requester?.processLocationAccessDenied()
    }

    //this is the result of asking for permission to change location setting(API < 23   )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getLocation(requester)
    }

}