package com.android.cedecsi.ui.schedule

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.work.*
import com.android.cedecsi.BuildConfig
import com.android.cedecsi.databinding.ActivityScheduleBinding
import com.android.cedecsi.ui.location.GPSProvider
import com.android.cedecsi.ui.schedule.tracking.TrackingActivity
import java.util.*
import java.util.concurrent.TimeUnit

class ScheduleActivity : AppCompatActivity() {

    companion object {
        const val tracking_key = "tracking-key"
        const val tracking_tag = "location"
    }

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var notification: TrackingNotification

    private val constraints = Constraints.Builder()
//        .setRequiredNetworkType(NetworkType.CONNECTED)
//        .setRequiresStorageNotLow(true)
//        .setRequiresBatteryNotLow(true)
        .build()

    private lateinit var worker: PeriodicWorkRequest

    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (
            it[GPSProvider.FINE_LOCATION_PERMISSION] == true
            && it[GPSProvider.COARSE_LOCATION_PERMISSION] == true
        ) {
            startWork()
        } else {
            Toast.makeText(this, "Debe conceder los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        notification = TrackingNotification(this)

        worker = PeriodicWorkRequestBuilder<TrackingWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(tracking_tag)
            .build()

        setupViews()
        setupListeners()

        observeWork(worker.id)
    }

    private fun setupViews() {
        checkStatus().let {
            binding.btnStart.isVisible = !it
            binding.btnCancel.isVisible = it
            if (!it) {
                workManager.cancelAllWork()
            }
        }
    }

    private fun checkPermission() {
        if (
            ContextCompat.checkSelfPermission(this, GPSProvider.FINE_LOCATION_PERMISSION) !=
            PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, GPSProvider.COARSE_LOCATION_PERMISSION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(arrayOf(
                GPSProvider.FINE_LOCATION_PERMISSION,
                GPSProvider.COARSE_LOCATION_PERMISSION
            ))
        } else {
            startWork()
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnStart.setOnClickListener {
            checkPermission()
        }
        binding.btnCancel.setOnClickListener {
            cancelWork()
        }
        binding.btnItems.setOnClickListener {
            startActivity(Intent(this, TrackingActivity::class.java))
        }
    }

    private fun startWork() {
        workManager.enqueueUniquePeriodicWork(
            "locationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            worker
        )
        notification.showNotification(notification.trackingIntent())
        setStatus(true)
        setupViews()
    }

    private fun cancelWork() {
        workManager.cancelAllWorkByTag(tracking_tag)
        notification.cancelNotifications()
        setStatus(false)
        setupViews()
    }

    private fun observeWork(id: UUID) {
        workManager.getWorkInfoByIdLiveData(id)
            .observe(this) { info ->
                if (info != null && info.state.isFinished) {
                    setStatus(false)
                    info.outputData.getString("location")?.let {
                        Log.i("Location", it)
                    }
                } else {
                    setStatus(true)
                }
            }
    }

    private fun checkStatus(): Boolean {
        return preferences.getBoolean(tracking_key, false)
    }

    private fun setStatus(tracking: Boolean) {
        with(preferences.edit()) {
            putBoolean(tracking_key, tracking)
            apply()
        }
    }

}