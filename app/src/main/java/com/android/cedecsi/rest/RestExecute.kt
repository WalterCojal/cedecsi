package com.android.cedecsi.rest

import android.location.Location
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestExecute {

    private val dataSource = RetrofitProvider.provideRetrofit().create(DataSource::class.java)

    fun validate(doc: String, onResult: (ApiResult?) -> Unit) {
        val call = dataSource.validateRuc(doc, RetrofitProvider.token)
        call.enqueue(object: Callback<ApiResult> {
            override fun onResponse(call: Call<ApiResult>, response: Response<ApiResult>) {
                Log.e("Service", response.message())
                if (response.isSuccessful && response.body() != null) {
                    onResult(response.body()!!)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ApiResult>, t: Throwable) {
                Log.e("Service", "error", t)
                onResult(null)
            }
        })
    }

    fun uploadCoordinates(location: Location, onResult: (Boolean) -> Unit) {
        val body = HashMap<String, Any>()
        body["latitud"] = location.latitude
        body["longitud"] = location.longitude

        val call = dataSource.uploadCoordinates(body)
        call.enqueue(object: Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                Log.e("Service", response.message())
                if (response.isSuccessful && response.body() != null) {
                    onResult(response.body()!!)
                } else {
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Service", "error", t)
                onResult(false)
            }
        })
    }

}