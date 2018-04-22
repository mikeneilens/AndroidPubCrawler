package michaelneilens.pubCrawler

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapOfPubs: GoogleMap
    private var pubs:List<PubDetail> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (savedInstanceState == null) {
            pubs = unParcelPubs()
            title = pubs[0].name
        }
    }

    private fun unParcelPubs():List<PubDetail> {
        return intent.getParcelableArrayListExtra(EXTRAS_PUBS)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapOfPubs = googleMap

        addUserLocationMarker()
        addPubMarkers(pubs)
        setUpCamera(pubs)
    }

    private fun addUserLocationMarker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapOfPubs.setMyLocationEnabled(true)
        }
    }

    private fun addPubMarkers(pubs:List<PubDetail>) {
        for (pub in pubs) {
            val pubLatLng = LatLng(pub.latitude, pub.longitude)
            mapOfPubs.addMarker(MarkerOptions().position(pubLatLng).title(pub.name))
        }

    }
    private fun setUpCamera(pubs:List<PubDetail>){
        if (pubs.isNotEmpty()) {
            val pubLatLng0 = LatLng(pubs[0].latitude, pubs[0].longitude)
            mapOfPubs.moveCamera(CameraUpdateFactory.newLatLng(pubLatLng0))
        }
        mapOfPubs.moveCamera(CameraUpdateFactory.zoomTo(17f))
    }

    companion object {
        const val EXTRAS_PUBS = "Pubs"
    }
}
