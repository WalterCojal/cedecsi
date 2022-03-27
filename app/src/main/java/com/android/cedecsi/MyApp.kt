package com.android.cedecsi

import android.app.Application
import com.android.cedecsi.room.CedecsiDatabase
import com.android.cedecsi.room.dao.PhotoDao

class MyApp: Application() {


    private lateinit var database: CedecsiDatabase
    lateinit var photoDao: PhotoDao

    override fun onCreate() {
        super.onCreate()
        database = CedecsiDatabase.getDatabase(this)
        photoDao = database.getPhotoDao()
    }

}