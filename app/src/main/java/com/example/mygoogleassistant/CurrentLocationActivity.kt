package com.example.mygoogleassistant

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mygoogleassistant.databinding.ActivityCurrentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrentLocationActivity : AppCompatActivity(), OnMapReadyCallback {
// locationApi key= AIzaSyArYYgxRmXGoTFBLKn6nxMEXmiIy8hYQFU
// locationApi key2 my first app= AIzaSyDO98Hftuf8I4Dv5PgN10FJl_y8vE1bKCM
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCurrentLocationBinding
    private lateinit var currentLocation:Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCurrentLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        getCurrentLocationUser()
    }

    private fun getCurrentLocationUser() {
        if(ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this,
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),permissionCode )
            return

        }
        val getLocation=fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location ->
            if(location!=null){
                currentLocation=location
                Toast.makeText(applicationContext,currentLocation.latitude.toString() + "  " +
                currentLocation.longitude.toString(),Toast.LENGTH_LONG).show()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissionCode -> if(grantResults.isNotEmpty() && grantResults[0]==
                    PackageManager.PERMISSION_GRANTED){
                getCurrentLocationUser()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val latLng=LatLng(currentLocation.latitude,currentLocation.longitude)
        val markerOption=MarkerOptions().position(latLng).title("Current Location")

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,7f))
        googleMap?.addMarker(markerOption)
       /* mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }
}