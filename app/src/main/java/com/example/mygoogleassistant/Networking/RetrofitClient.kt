package com.example.mygoogleassistant.Networking

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://newsapi.org/"
    private const val API_KEY = "f290a0cc816e460cac709baf63d4b4fc"

    // OkHttpClient with Interceptor to add API key to the headers
   /* private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY") // Adding the API key
                .build()
            chain.proceed(newRequest)
        }
        .build()*/

    // Retrofit instance
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }

}
