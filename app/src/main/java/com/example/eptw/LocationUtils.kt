package com.example.eptw

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*

object LocationUtils {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context, callback: (latitude: String, longitude: String) -> Unit) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object :
            LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location: Location? = result.lastLocation
                location?.let {
                    callback(it.latitude.toString(), it.longitude.toString())
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }, Looper.getMainLooper())
    }
}