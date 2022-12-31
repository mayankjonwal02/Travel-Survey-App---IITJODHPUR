package com.example.livelocation

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface locationclient {

    fun getLocationUpdates(interval:Long): Flow<Location>

    class LocationException(message:String):Exception()
}