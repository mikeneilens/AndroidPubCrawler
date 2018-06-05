package michaelneilens.pubCrawler

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

class LocationProviderForNewAPI(activity: AppCompatActivity): LocationProvider(activity) {

    override fun getLocation(requester:LocationRequester?) {
        super.getLocation(requester)
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? -> requester?.processNewLocation(location) }
        } else {
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED))
        {
            getLocation(requester)
        } else {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_LOCATION_PERMISSION_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if ((requestCode == LocationProvider.MY_LOCATION_PERMISSION_REQUEST) && (permissionWasGranted(grantResults) ) ){
            getLocation(requester)
        } else {
            requester?.processLocationAccessDenied()
        }
    }

    private fun permissionWasGranted(grantResults:IntArray):Boolean {
        //permission was granted if a grantResult contains a zero
        val hasPermission: (Int) -> Boolean = { it == 0 }
        return grantResults.any (hasPermission)
    }

}