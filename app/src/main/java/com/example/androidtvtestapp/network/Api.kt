package com.example.androidtvtestapp.network

import com.example.androidtvtestapp.model.VideoList
import com.example.androidtvtestapp.model.VideoUrl
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("video/list")
    fun getVideos(
        @Query("client_id") clientId: Int,
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("provider_id") providerId: Int
    ): Call<VideoList>

    @GET("video/url")
    fun getVideoUrl(
        @Query("vid") videoId: Int,
        @Query("vfid") videoFileId: Int,
        @Query("redirect") redirect: Int,
        @Query("client_id") clientId: Int,
        @Query("api_key") apiKey: String,
    ): Call<VideoUrl>
}