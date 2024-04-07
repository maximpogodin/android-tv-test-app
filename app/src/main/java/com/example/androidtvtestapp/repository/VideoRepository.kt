package com.example.androidtvtestapp.repository

import com.example.androidtvtestapp.model.Video
import com.example.androidtvtestapp.model.VideoUrl
import com.example.androidtvtestapp.network.Api
import com.example.androidtvtestapp.network.ExternalApiOptions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoRepository {
    private val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ExternalApiOptions.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

    fun getVideos(limit: Int): List<Video> {
        val response = api.getVideos(3, ExternalApiOptions.API_KEY, limit, 9).execute()

        if (response.isSuccessful) {
            val responseResult = response.body()
            return responseResult?.videos ?: emptyList()
        } else {
            throw RuntimeException("Failed to fetch videos: ${response.message()}")
        }
    }

    fun getVideoUrl(videoId : Int, videoFileId : Int) : VideoUrl? {
        val response = api.getVideoUrl(videoId, videoFileId, 0, 3, ExternalApiOptions.API_KEY).execute()

        if (response.isSuccessful) {
            return response.body()
        }
        else {
            throw RuntimeException("Failed to fetch video url: ${response.message()}")
        }
    }
}