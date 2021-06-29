package com.smitcoderx.softageproject.Service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.smitcoderx.softageproject.Db.ProjectDatabase
import com.smitcoderx.softageproject.R
import com.smitcoderx.softageproject.Utils.Constants.ACTION_START_LOCATION_SERVICE
import com.smitcoderx.softageproject.Utils.Constants.ACTION_STOP_LOCATION_SERVICE
import com.smitcoderx.softageproject.Utils.Constants.CHANNEL_ID
import com.smitcoderx.softageproject.Utils.Constants.INTENT_ACTION
import com.smitcoderx.softageproject.Utils.Constants.LATITUDE
import com.smitcoderx.softageproject.Utils.Constants.LOCATION_SERVICE_ID
import com.smitcoderx.softageproject.Utils.Constants.LONGITUDE
import com.smitcoderx.softageproject.Utils.Constants.ONE_MINUTE

class LocationService : Service() {

    private val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult != null && locationResult.lastLocation != null) {
                sendData(locationResult.lastLocation)
            }
        }
    }

    private fun sendData(l: Location) {
        val intent = Intent(INTENT_ACTION)
        val bundle = Bundle()
        bundle.putParcelable(LATITUDE, l)

        intent.putExtra(LONGITUDE, bundle)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet Implemented")
    }

    @SuppressLint("MissingPermission")
    private fun startLocationService() {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
        builder.apply {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setContentTitle("Location Service")
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setContentText("Running")
            setContentIntent(pendingIntent)
            setAutoCancel(false)
            priority = NotificationCompat.PRIORITY_MAX
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Location Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by location Service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        val locationRequest = LocationRequest()
        locationRequest.apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE / 4
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
        startForeground(LOCATION_SERVICE_ID, builder.build())

    }


    private fun stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(callback)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == ACTION_START_LOCATION_SERVICE) {
                    startLocationService()

                } else if (action == ACTION_STOP_LOCATION_SERVICE) {
                    stopLocationService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


}