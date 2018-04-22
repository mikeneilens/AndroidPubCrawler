package michaelneilens.pubCrawler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import michaelneilens.pubCrawler.EventListenerInterfaces.LocationProcessor

class PCLocationManager(val activity:Activity, val context:Context) {

    var locationEnabled = false
    var locationProcessor: LocationProcessor? = null

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var turnedOn = false

    fun turnOnLocationServices() {
        if (!turnedOn) {
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = createLocationListener()
            updateLocationManagerMinTime(0)
            turnedOn = !turnedOn
        }
    }
    private fun createLocationListener():LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                println("pc onStatusChanged")
            }

            override fun onProviderEnabled(provider: String) {
                println("pc onProviderEnabled")
            }

            override fun onProviderDisabled(provider: String) {
                println("pc onProviderDisabled")
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
            }
        }
    }

    private fun makeUseOfNewLocation(location:Location){
        val latitude = location.latitude
        val longitude = location.longitude
        println("pc new latitude $latitude new longitude $longitude")
        updateLocationManagerMinTime(30000)

        locationProcessor?.useNewLocation(latitude,longitude)
    }

    private fun updateLocationManagerMinTime(minTime:Long) {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationEnabled = false
            println("pc permission denied")
            return
        } else {
            locationManager?.let {
                if  (!it.isProviderEnabled(LocationManager.NETWORK_PROVIDER ) && !it.isProviderEnabled(LocationManager.GPS_PROVIDER ) ) {
                    locationEnabled = false
                    println("pc permission denied")
                    return
                }
            }
        }
        locationEnabled = true
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, 50f, locationListener)
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 50f, locationListener)
    }

    fun turnOffLocationServices() {
        if (turnedOn) {
            locationManager?.removeUpdates(locationListener)
            turnedOn = !turnedOn
        }
    }
}