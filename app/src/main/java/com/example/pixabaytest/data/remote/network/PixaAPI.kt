package com.example.pixabaytest.data.remote.network

import com.example.pixabaytest.data.remote.model.PixaModel
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaAPI {

    @GET("api/")
    suspend fun searchImages(
        @Query("key") key: String = "32824571-46e17f060bfba1410037393a4",
        @Query("q") keyWord: String,
        @Query("page") page: Int
    ): PixaModel

    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey: String = "32824571-46e17f060bfba1410037393a4",
        @Query("page") page: Int
    ): PixaModel
}