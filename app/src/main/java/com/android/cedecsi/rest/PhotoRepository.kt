package com.android.cedecsi.rest

import com.android.cedecsi.room.dao.PhotoDao
import com.android.cedecsi.room.entity.Photo

class PhotoRepository(private val photoDao: PhotoDao): IPhotoRepository {

    override suspend fun save(photo: Photo): Long {
        return photoDao.insert(photo)
    }

}