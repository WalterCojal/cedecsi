package com.android.cedecsi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.cedecsi.room.dao.PhotoDao
import com.android.cedecsi.room.entity.Photo

@Database(version = 1, entities = [Photo::class])
abstract class CedecsiDatabase: RoomDatabase() {

    abstract fun getPhotoDao(): PhotoDao

    companion object {

        @Volatile
        private var INSTANCE: CedecsiDatabase? = null
        private const val DATABASE_NAME = "Cedecsi_database"

        fun getDatabase(context: Context): CedecsiDatabase {

            return INSTANCE ?: synchronized(CedecsiDatabase::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CedecsiDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}