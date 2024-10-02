package com.example.mygoogleassistant.Networking

import com.example.mygoogleassistant.data.newsmodel.Article
import com.example.mygoogleassistant.data.newsmodel.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources") sources: String="techcrunch",@Query("apiKey")apiKey:String="f290a0cc816e460cac709baf63d4b4fc"
    ): Response<NewsResponse>
}