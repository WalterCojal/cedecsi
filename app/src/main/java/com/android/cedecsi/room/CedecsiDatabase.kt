package com.android.cedecsi.room

import android.content.Context
import com.android.cedecsi.room.dao.PhotoDao
import com.android.cedecsi.room.entity.Photo
// TODO Uncomment to create CedecsiDatabase
//@Database(version = 1, entities = [Photo::class])
//abstract class CedecsiDatabase: RoomDatabase() {
//
//    abstract fun getPhotoDao(): PhotoDao
//
//    companion object {
//
//        @Volatile
//        private var INSTANCE: CedecsiDatabase? = null
//        private const val DATABASE_NAME = "Cedecsi_database"
//
//        fun getDatabase(context: Context): CedecsiDatabase {
//
//            return INSTANCE ?: synchronized(CedecsiDatabase::class.java) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    CedecsiDatabase::class.java,
//                    DATABASE_NAME
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//
//    }
//
//}