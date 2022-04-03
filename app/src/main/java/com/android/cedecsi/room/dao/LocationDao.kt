package com.android.cedecsi.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.android.cedecsi.room.entity.Location

@Dao
interface LocationDao: BaseDao<Location> {

    @Query("SELECT * FROM Location")
    fun getAll(): List<Location>

}