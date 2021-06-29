package com.smitcoderx.softageproject.UI.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.smitcoderx.softageproject.Models.LocationDetails
import com.smitcoderx.softageproject.R
import com.smitcoderx.softageproject.Service.LocationService
import com.smitcoderx.softageproject.UI.MainActivity
import com.smitcoderx.softageproject.UI.ProjectViewModel
import com.smitcoderx.softageproject.Utils.Constants.ACTION_START_LOCATION_SERVICE
import com.smitcoderx.softageproject.Utils.Constants.ACTION_STOP_LOCATION_SERVICE
import com.smitcoderx.softageproject.Utils.Constants.FINE_REQUEST
import com.smitcoderx.softageproject.Utils.Constants.FOREGROUND_REQUEST
import com.smitcoderx.softageproject.Utils.Constants.INTENT_ACTION
import com.smitcoderx.softageproject.Utils.Constants.LATITUDE
import com.smitcoderx.softageproject.Utils.Constants.LONGITUDE
import com.smitcoderx.softageproject.Utils.Constants.TAG
import com.smitcoderx.softageproject.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: ProjectViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        checkPermission()

        viewModel = (activity as MainActivity).viewModel
        binding.fabService.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.FOREGROUND_SERVICE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.FOREGROUND_SERVICE),
                    FOREGROUND_REQUEST
                )
            } else {
                val manager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    askGPS()
                } else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startLocationService()
                }
            }
        }

        binding.fabServiceStop.setOnClickListener {
            stopLocationService()
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            receiver, IntentFilter(INTENT_ACTION)
        )
    }

    private fun isLocationRunning(): Boolean {
        val activityManager: ActivityManager =
            requireActivity().getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service: ActivityManager.RunningServiceInfo in activityManager.getRunningServices(
                Integer.MAX_VALUE
            )) {
                if (LocationService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    fun startLocationService() {
        if (!isLocationRunning()) {
            val intent = Intent(context, LocationService::class.java)
            intent.action = ACTION_START_LOCATION_SERVICE
            requireActivity().startService(intent)
            binding.live.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.tvLive.text = "Service Started"
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun stopLocationService() {
        if (isLocationRunning()) {
            val intent = Intent(context, LocationService::class.java)
            intent.action = ACTION_STOP_LOCATION_SERVICE
            requireActivity().startService(intent)
            binding.live.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
            binding.tvLive.text = "Service Stopped"

        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_REQUEST
            )
        }
    }

    private val receiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle = intent!!.getBundleExtra(LONGITUDE)
            val location = bundle!!.getParcelable<Location>(LATITUDE)!!
            val latitude = location.latitude
            val longitude = location.longitude

            var status = ""
            if (appInForeground(context!!)) {
                status = "Foreground"
                Log.d(TAG, "onReceive: Foreground")
            } else if (appInBackground(context)) {
                status = "Background"
                Log.d(TAG, "onReceive: Background")
            }
            binding.apply {
                tvLatitude.text = "Latitude: $latitude"
                tvLongitude.text = "Longitude: $longitude"
                binding.tvStatus.text = "Status: $status"
            }

            insertDetails(latitude.toString(), longitude.toString(), status)


            Log.d(TAG, "Location Updates $latitude, $longitude ")

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askGPS() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Your GPS seems to be disabled. To Start the service enable the GPS")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                startLocationService()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    fun appInForeground(context: Context): Boolean {
        val activityManager =
            context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false
        return runningAppProcesses.any { it.processName == context.packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
    }

    fun appInBackground(context: Context): Boolean {
        val activityManager =
            context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false
        return runningAppProcesses.any { it.processName == context.packageName && it.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        return sdf.format(Date())
    }


    fun insertDetails(latitude: String, longitude: String, status: String) {
        val details =
            LocationDetails(null, latitude, longitude, setDate(), status)

        viewModel.insert(details)
    }

}