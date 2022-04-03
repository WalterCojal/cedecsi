package com.android.cedecsi.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class Location (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var latitude: Double,
    var longitude: Double,
    var date: Long
)