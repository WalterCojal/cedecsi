package com.android.cedecsi.rest

import com.android.cedecsi.room.entity.Photo

interface IPhotoRepository {
    suspend fun save(photo: Photo): Long
}