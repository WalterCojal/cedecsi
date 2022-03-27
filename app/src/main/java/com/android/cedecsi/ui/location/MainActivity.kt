package com.android.cedecsi.ui.location

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.cedecsi.R
import com.android.cedecsi.rest.RestExecute
import com.android.cedecsi.ui.navigation.NavigationActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOG_TAG = "MainActivity"
    }

    private var txtLatitud: TextView? = null
    private var txtLongitud: TextView? = null
    private var btnCoordenadas: Button? = null
    private var btnUpload: Button? = null
    private lateinit var gpsProvider: GPSProvider
    private var location: Location? = null
    private lateinit var restExecute: RestExecute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gpsProvider = GPSProvider(this)
        restExecute = RestExecute()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        txtLatitud = findViewById(R.id.txtLatitud)
        txtLongitud = findViewById(R.id.txtLongitud)
        btnCoordenadas = findViewById(R.id.btnCoordenadas)
        btnUpload = findViewById(R.id.btnUpload)
    }

    private fun setupListeners() {
        btnCoordenadas?.setOnClickListener {
            gpsProvider.checkPermission()
        }
        gpsProvider.onLocation = {
            location = it
            txtLatitud?.text = "Latitud: ${it.latitude}"
            txtLongitud?.text = "Latitud: ${it.longitude}"
        }
        btnUpload?.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        gpsProvider.onStop()
    }

}