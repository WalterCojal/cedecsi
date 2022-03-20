package com.android.cedecsi.rest

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface DataSource {

    @POST("upload.php")
    fun uploadCoordinates(@Body body: HashMap<String, Any>): Call<Boolean>

    @GET("ruc/{ruc}")
    fun validateRuc(
        @Path("ruc") ruc: String,
        @Query("api_token") token: String
    ): Call<ApiResult>

}