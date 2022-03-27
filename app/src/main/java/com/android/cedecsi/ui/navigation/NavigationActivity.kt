package com.android.cedecsi.ui.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.cedecsi.R
import com.android.cedecsi.ui.location.GPSProvider
import com.android.cedecsi.ui.location.GpsProviderType
import com.android.cedecsi.util.hasGoogleServices

class NavigationActivity : AppCompatActivity() {

    lateinit var gpsProvider: GPSProvider
    private var gpsProviderType = GpsProviderType.External
    // TODO Uncomment to set binding
//    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        TODO Uncomment to bind the view
//        binding = ActivityNavigationBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_navigation)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        gpsProviderType = if (hasGoogleServices()) GpsProviderType.External
        else GpsProviderType.Internal
        gpsProvider = GPSProvider(this, gpsProviderType)
        // TODO Uncomment to setup navigation and toolbar
//        setSupportActionBar(binding.navToolbar)
//        val navGraph = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(navGraph.graph)
//        binding.navToolbar.setupWithNavController(navGraph, appBarConfiguration)

    }

    private fun setupListeners() {

    }

    override fun onStop() {
        super.onStop()
        gpsProvider.onStop()
    }

}