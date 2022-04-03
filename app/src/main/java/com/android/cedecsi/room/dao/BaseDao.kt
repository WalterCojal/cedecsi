package com.android.cedecsi.room.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photos: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: T): Long

    @Delete
    fun delete(user: T)
}