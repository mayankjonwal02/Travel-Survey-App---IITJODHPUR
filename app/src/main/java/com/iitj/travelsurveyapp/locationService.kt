package com.iitj.livelocation

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class locationService(name: String?) : IntentService(name){
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var locationclient: locationclient
    private var servicescope= CoroutineScope(SupervisorJob() +  Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        locationclient = defaultLocationClient(applicationContext,LocationServices.getFusedLocationProviderClient(applicationContext))


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        auth=FirebaseAuth.getInstance()
        reference=FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString()).child("coordinates")
        start()
        when(intent?.action){
            ACTION_START-> start()
            ACTION_STOP -> stop()
        }



        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        servicescope.cancel()
        stop()
    }

    private fun start()
    {

        var notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var notification=NotificationCompat.Builder(this,"location")
            .setContentTitle("Tracking location...")
            .setContentText("location : null")
            .setOngoing(true)

        locationclient.getLocationUpdates(2000)
            .catch { e->e.printStackTrace() }
            .onEach { location ->
                var lat=location.latitude.toString().takeLast(3)
                var long=location.longitude.toString().takeLast(3)
                var updatednotification=notification.setContentText("location : ($lat , $long)")
                notificationManager.notify(1,updatednotification.build())
                //var loc = mutableListOf<String>(location.latitude.toString(),location.longitude.toString() , Calendar.getInstance().time.toString())
                //reference.push().setValue(loc)
                //Toast.makeText(applicationContext, "location : $lat , $long", Toast.LENGTH_SHORT).show()

            }
            .launchIn(servicescope)

        startForeground(1,notification.build())

    }

    private fun stop(){
        stopForeground(true)
        stopSelf()

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(p0: Intent?) {
        TODO("Not yet implemented")
    }

    companion object{
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}