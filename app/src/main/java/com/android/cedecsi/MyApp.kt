package com.android.cedecsi

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.android.cedecsi.room.CedecsiDatabase
import com.android.cedecsi.room.dao.LocationDao
import com.android.cedecsi.room.dao.PhotoDao

class MyApp: Application(), Configuration.Provider {

    private lateinit var database: CedecsiDatabase
    lateinit var photoDao: PhotoDao
    lateinit var locationDao: LocationDao

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, workManagerConfiguration)
        database = CedecsiDatabase.getDatabase(this)
        photoDao = database.getPhotoDao()
        locationDao = database.getLocationDao()
    }

}