package com.android.cedecsi.room.entity


// TODO Add Room Entity implementation
//@Entity(tableName = "Photo")
data class Photo (
//    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var name: String,
    var path: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)