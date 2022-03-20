package com.android.cedecsi.rest

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DataSource {

    @POST("upload.php")
    fun uploadCoordinates(@Body body: HashMap<String, Any>): Call<Boolean>

}