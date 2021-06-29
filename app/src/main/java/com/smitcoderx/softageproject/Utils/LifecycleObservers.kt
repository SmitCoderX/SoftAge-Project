package com.smitcoderx.softageproject.Utils

import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.smitcoderx.softageproject.UI.Home.HomeFragment
import com.smitcoderx.softageproject.Utils.Constants.LATITUDE
import com.smitcoderx.softageproject.Utils.Constants.LONGITUDE

class LifecycleObservers : LifecycleObserver {

    fun registerLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppStart() {
        Log.e("lifecycle event", "ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPause() {
        Log.e("lifecycle event", "ON_PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppStop() {
        Log.e("lifecycle event", "ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroy() {
        Log.e("lifecycle event", "ON_DESTROY")
      /*  val intent = Intent()
        val bundle = intent.getBundleExtra(LONGITUDE)
        val location = bundle!!.getParcelable<Location>(LATITUDE)!!
        val latitude = location.latitude
        val longitude = location.longitude
        HomeFragment().insertDetails(latitude.toString(), longitude.toString(), "Killed")*/
    }


}