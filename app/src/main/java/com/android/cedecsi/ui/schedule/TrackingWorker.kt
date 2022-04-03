package com.android.cedecsi.ui.schedule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.android.cedecsi.MyApp
import com.android.cedecsi.room.entity.Location
import com.android.cedecsi.ui.location.GPSProvider
import com.android.cedecsi.ui.location.GpsProviderType
import com.android.cedecsi.util.hasGoogleServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class TrackingWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    var locationManager: LocationManager
            = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var locationByGps: android.location.Location? = null
    private var locationByNetwork: android.location.Location? = null

    private val gpsLocationListener: LocationListener =
        LocationListener { location -> locationByGps = location }

    private val networkLocationListener: LocationListener =
        LocationListener { location -> locationByNetwork = location }

    override suspend fun doWork(): Result {
        val locationDao = (context.applicationContext as MyApp).locationDao

        getGPSLocation()?.let {
            val id = locationDao.insert(Location(
                latitude = it.latitude,
                longitude = it.longitude,
                date = Date().time
            ))
            Log.i("Insert", "Location: $id")
            return Result.success(workDataOf("location" to "${it.latitude}, ${it.longitude}"))
        }
        return Result.failure()
    }

    @SuppressLint("MissingPermission")
    private fun getGPSLocation(): android.location.Location? {
        val hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        //--------------------------------------------------------------------------------------//

        if (!hasGPS && !hasNetwork) {
            Toast.makeText(context, "Debe activar su GPS", Toast.LENGTH_SHORT).show()
            return null
        }

        if (hasGPS) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                GPSProvider.TIME_UPDATE,
                GPSProvider.DISTANCE_UPDATE,
                gpsLocationListener
            )
        }

        if (hasNetwork) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                GPSProvider.TIME_UPDATE,
                GPSProvider.DISTANCE_UPDATE,
                networkLocationListener
            )
        }

        //--------------------------------------------------------------------------------------//

        val lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocationByGps != null) {
            locationByGps = lastKnownLocationByGps
        }

        val lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastKnownLocationByNetwork != null) {
            locationByNetwork = lastKnownLocationByNetwork
        }

        //--------------------------------------------------------------------------------------//

        return if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps!!.accuracy > locationByNetwork!!.accuracy) {
                locationByGps
            } else {
                locationByNetwork
            }
        } else null
    }

}