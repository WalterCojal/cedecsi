package com.android.cedecsi.room.dao

import com.android.cedecsi.room.entity.Photo

// TODO Add Room Dao implementation
//@Dao
interface PhotoDao {
//    @Query("SELECT * FROM Photo")
    fun getAll(): List<Photo>
//
//    @Query("SELECT * FROM Photo WHERE id IN (:id) LIMIT 1")
    fun findById(id: Int): Photo
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photos: List<Photo>): List<Long>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo): Long
//
//    @Delete
    fun delete(user: Photo)
}