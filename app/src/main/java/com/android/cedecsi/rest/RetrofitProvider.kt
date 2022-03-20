package com.android.cedecsi.rest

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

    companion object {

//        private const val baseUrl = "http://sunass.conceptomercado.com/sunass_rest/"
        private const val baseUrl = "https://apiperu.dev/api/"
        const val token = "ba33d99663d9ff3af4c6297099b4f386a232c8f87d93fcdc0e663f31dd0352aa"

        private fun provideGson(): Gson {
            val builder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            builder.setLenient().create()
            return builder.create()
        }

        private fun provideHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            builder.addInterceptor(logging)

            return builder.build()
        }

        fun provideRetrofit(): Retrofit {
            val builder = Retrofit.Builder().baseUrl(baseUrl)
            builder.addConverterFactory(GsonConverterFactory.create(provideGson()))
            builder.client(provideHttpClient())
            return builder.build()
        }

    }

}