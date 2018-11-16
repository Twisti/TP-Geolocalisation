package com.ladent.tp.geolocalisation

import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import java.util.*


class MainActivity : AppCompatActivity() {
    //l'objet provenant de l'api google, qui va nous permettre de recuperer les dernieres localisations de l'utilisateur
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    // Il faut choisir la meme permission que celle choisi dans le manifest.
    //  private val permissionLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionLocation = Manifest.permission.ACCESS_COARSE_LOCATION



    lateinit var locationManager: LocationManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //l'objet provenant de l'api google, qui va nous permettre de recuperer les dernieres localisations de l'utilisateur
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

         val geocoder = Geocoder(this, Locale.getDefault())


        buttonLastLoca.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    this.permissionLocation)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(this.permissionLocation), 1)
            }else{

                buttonLastLoca.setOnClickListener {
                    if (!isLocationEnabled()){
                        val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(viewIntent)
                    }else {
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                if (location != null) {
                                    var message = "long " + location.longitude + " -  lat " + location.latitude
                                    textView.text = message

                                     var addresses = geocoder.getFromLocation( location.latitude,location.longitude,1)
                                     textAdress.text = addresses.get(0).getAddressLine(0)
                                } else {
                                    Toast.makeText(this, "no location found", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                }
            }
        }
    }


    private fun isPermissionEnable() : Boolean{
        return ContextCompat.checkSelfPermission(this,
            this.permissionLocation)== PackageManager.PERMISSION_GRANTED
        }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


}


