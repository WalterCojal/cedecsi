package com.android.cedecsi.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.cedecsi.R
import com.android.cedecsi.ui.location.GPSProvider
import com.android.cedecsi.ui.location.GpsProviderType

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {

    private var path = ""
    private lateinit var gpsProvider: GPSProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString(FirstFragment.key_path, "")
        }
        gpsProvider = GPSProvider(requireActivity() as AppCompatActivity, GpsProviderType.External)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    private fun setupViews() {

    }

    private fun setupListeners() {
        gpsProvider.onLocation = {
            addTextToImage("Ubicación: ${it.latitude}, ${it.longitude}")
        }
    }

    private fun addTextToImage(text: String) {

    }

}