package com.android.cedecsi.ui.schedule.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cedecsi.MyApp
import com.android.cedecsi.databinding.ActivityTrackingBinding
import com.android.cedecsi.room.dao.LocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackingBinding
    private val adapter = LocationAdapter()
    private lateinit var locationDao: LocationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationDao = (application as MyApp).locationDao
        setupViews()
        setupListeners()
        getItems()
    }

    private fun setupViews() {
        binding.rvLocations.layoutManager = LinearLayoutManager(this)
        binding.rvLocations.adapter = adapter
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getItems() {
        lifecycleScope.launch(Dispatchers.IO) {
            val items = locationDao.getAll()
            launch(Dispatchers.Main) {
                adapter.items = items
            }
        }
    }

}