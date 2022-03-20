package com.android.cedecsi.location

import android.Manifest
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.cedecsi.R
import com.android.cedecsi.rest.RestExecute
import org.w3c.dom.Text

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

    private var edtDoc: EditText? = null
    private var btnConsult: Button? = null
    private var txtResult: TextView? = null

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

        edtDoc = findViewById(R.id.edtDoc)
        btnConsult = findViewById(R.id.btnConsult)
        txtResult = findViewById(R.id.txtResult)
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
        btnConsult?.setOnClickListener {
            txtResult?.text = "Cargando..."
            restExecute.validate(edtDoc?.text?.toString() ?: "") {
                if (it != null) {
                    if (it.success) {
                        txtResult?.text = """
                        Nombre: ${it.data.nombre_o_razon_social},
                        RUC: ${it.data.ruc}
                    """
                    } else {
                        txtResult?.text = it.message
                    }

                }
            }
        }
        btnUpload?.setOnClickListener {
            if (location != null) restExecute.uploadCoordinates(location!!) {
                if (it) {
                    Toast.makeText(this, "Envío exitoso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Envío falló!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        gpsProvider.onDestroy()
        super.onDestroy()
    }

}