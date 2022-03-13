package com.android.cedecsi.location

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.cedecsi.R

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOG_TAG = "MainActivity"
    }

    private var txtLatitud: TextView? = null
    private var txtLongitud: TextView? = null
    private var btnCoordenadas: Button? = null
    private lateinit var gpsProvider: GPSProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gpsProvider = GPSProvider(this)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        txtLatitud = findViewById(R.id.txtLatitud)
        txtLongitud = findViewById(R.id.txtLongitud)
        btnCoordenadas = findViewById(R.id.btnCoordenadas)
    }

    private fun setupListeners() {
        btnCoordenadas?.setOnClickListener {
            gpsProvider.checkPermission()
        }
        gpsProvider.onLocation = {
            txtLatitud?.text = "Latitud: ${it.latitude}"
            txtLongitud?.text = "Latitud: ${it.longitude}"
        }
    }

    override fun onDestroy() {
        gpsProvider.onDestroy()
        super.onDestroy()
    }

}