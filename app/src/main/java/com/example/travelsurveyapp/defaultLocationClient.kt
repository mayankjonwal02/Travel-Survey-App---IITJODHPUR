package com.example.livelocation

import android.annotation.SuppressLint
import android.app.TaskStackBuilder.create
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.location.LocationResult

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class defaultLocationClient(private var context: Context,private var client: FusedLocationProviderClient):locationclient {
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {

        return  callbackFlow {
            if(!context.hasLocationPermission())
            {
                throw locationclient.LocationException("missing location permission")
            }
            var locationManager=context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var isgpsenabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            var isnetworkenabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isgpsenabled && !isnetworkenabled)
            {
                throw locationclient.LocationException("GPS disabled")
            }
            var locationRequest = com.google.android.gms.location.LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)
            var locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location -> launch { send(location) } }
                }

            }
            client.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper() )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }

        }

    }
}