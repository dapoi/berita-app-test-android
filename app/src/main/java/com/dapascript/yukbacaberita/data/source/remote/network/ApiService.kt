package com.dapascript.yukbacaberita.data.source.remote.network

import com.dapascript.yukbacaberita.data.source.remote.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun getNews(
        @Query("q") q: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = "b5e957cc160649c09671f3dc74c2f3b2"
    ): NewsResponse
}