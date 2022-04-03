package com.android.cedecsi.ui.location

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.cedecsi.R
import com.android.cedecsi.databinding.ActivityMainBinding
import com.android.cedecsi.rest.RestExecute
import com.android.cedecsi.ui.navigation.NavigationActivity
import com.android.cedecsi.ui.schedule.ScheduleActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOG_TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var gpsProvider: GPSProvider
    private var location: Location? = null
    private lateinit var restExecute: RestExecute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gpsProvider = GPSProvider(this)
        restExecute = RestExecute()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {

    }

    private fun setupListeners() {
        binding.btnCoordenadas.setOnClickListener {
            gpsProvider.checkPermission()
        }
        gpsProvider.onLocation = {
            location = it
            binding.txtLatitud.text = "Latitud: ${it.latitude}"
            binding.txtLongitud.text = "Latitud: ${it.longitude}"
        }
        binding.btnUpload.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }
        binding.btnScheduling.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        gpsProvider.onStop()
    }

}