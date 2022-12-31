package com.example.travelsurveyapp

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import java.util.*

class locationservice2 : Service() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var notificationManager:NotificationManager
    private lateinit var notification:NotificationCompat.Builder

    var scope=  CoroutineScope(SupervisorJob() +  Dispatchers.IO)
    var auth= FirebaseAuth.getInstance()
    var reference=FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString())

    var locationRequest = LocationRequest.create().apply {
        interval = 5000
        fastestInterval = 2000
        maxWaitTime = 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    /*var notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    var notification= NotificationCompat.Builder(this,"location")
        .setContentTitle("Tracking location...")
        .setContentText("location : null")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setOngoing(true)*/

    var locationCallback = object : LocationCallback() {
        @SuppressLint("MissingPermission")
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            var task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener {
                if (it != null) {
                    Toast.makeText(applicationContext, "${it.latitude} , ${it.longitude}", Toast.LENGTH_SHORT).show()
                    var updatednotification=notification.setContentText("location : (${it.latitude} , ${it.longitude})")
                    notificationManager.notify(1001,updatednotification.build())

                    var loc = mutableListOf<String>(it.latitude.toString(),it.longitude.toString() , Calendar.getInstance().time.toString())
                    reference.child("coordinates").push().setValue(loc)
                }
            }



        }


    }

   /* init {
        auth=FirebaseAuth.getInstance()
        reference= FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString())
        Log.i("mytag","service has been created")
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

       /* locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            maxWaitTime = 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            @SuppressLint("MissingPermission")
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                var task = fusedLocationProviderClient.lastLocation
                task.addOnSuccessListener {
                    if (it != null) {
                        Toast.makeText(applicationContext, "${it.longitude}", Toast.LENGTH_SHORT).show()

                        var loc = mutableListOf<String>(it.latitude.toString(),it.longitude.toString() , Calendar.getInstance().time.toString())
                        reference.child("coordinates").push().setValue(loc)
                    }
                }



            }


        } */
    }*/

    override fun onCreate() {
        super.onCreate()
        //notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

     /*   notification= NotificationCompat.Builder(this,"location")
            .setContentTitle("Tracking location...")
            .setContentText("location : null")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)*/

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("mytag","service has ben started")
        var CHANNELID = "Foreground Service ID"
        var channel = NotificationChannel(
                CHANNELID,
        CHANNELID,
        NotificationManager.IMPORTANCE_LOW
        )
        notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        notification =  NotificationCompat.Builder(this, CHANNELID)
            .setContentText("App is tracking your live location")
            .setContentTitle("Tracking.....")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)






        scope.launch {

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        startForeground(1001, notification.build())




        //startForeground(1,notification.build())
        return super.onStartCommand(intent, flags, startId)
        //return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        Log.i("mytag","destroying...........service")
    }




    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}