package com.example.androidtvtestapp.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.tvprovider.media.tv.Channel
import androidx.tvprovider.media.tv.ChannelLogoUtils
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import com.example.androidtvtestapp.R
import com.example.androidtvtestapp.model.Video
import com.example.androidtvtestapp.network.Api
import com.example.androidtvtestapp.network.ExternalApiOptions
import com.example.androidtvtestapp.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChannelsLoaderService : Service() {
    @SuppressLint("RestrictedApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        CoroutineScope(Dispatchers.IO).launch {
            createChannel()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("RestrictedApi")
    fun createChannel() {
        val api = RetrofitClient.instance.create(Api::class.java)

        val response = api.getVideos(3, ExternalApiOptions.API_KEY, 10, 9).execute()
        if (response.isSuccessful) {
            val responseResult = response.body()
            val videos = responseResult?.videos ?: emptyList()

            val gson = Gson()

            val builder = Channel.Builder()
            builder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
                .setDisplayName("Channel Name")
            val channelUri = applicationContext.contentResolver.insert(
                TvContractCompat.Channels.CONTENT_URI, builder.build().toContentValues()
            )
            val channelId = ContentUris.parseId(channelUri!!)
            val resourceId = R.drawable.channel_logo
            val packageName = applicationContext.packageName
            val channelLogoUri = Uri.parse("android.resource://$packageName/$resourceId")
            ChannelLogoUtils.storeChannelLogo(applicationContext, channelId, channelLogoUri)

            TvContractCompat.requestChannelBrowsable(applicationContext, channelId)
            val previewProgramBuilder = PreviewProgram.Builder()

            val uriArray = arrayOf<Uri>(
                Uri.parse("https://i.ytimg.com/vi/Z8UEnCjNisY/mqdefault.jpg"),
                Uri.parse("https://i.pinimg.com/474x/e6/97/f7/e697f78faac0485c541632f4a063ef77.jpg"),
                Uri.parse("https://pic.epicube.su/Km-yIUFrfUc/karma-hishtnih-zhivotnih.webp"),
                Uri.parse("https://img.youtube.com/vi/P5mhv91nT_c/mqdefault.jpg"),
                Uri.parse("https://wallpapers-fenix.eu/miniatura/141207/235931528.jpg")
            )
            var i = 0
            val max = uriArray.size - 1

            for (video in videos) {
                previewProgramBuilder.buildProgram(
                    video, channelId, getIntentUri(gson.toJson(video)), uriArray[i]
                )

                i++

                if (i > max) {
                    i = 0
                }
            }
        }

        //val serviceIntent = Intent(applicationContext, CardUpdateService::class.java)
        //applicationContext.startService(serviceIntent)
    }

    private fun getIntentUri(json: String) : Uri {
        return Uri.Builder()
            .scheme("androidtvhomescreenchannels")
            .authority("com.android.tv")
            .appendPath("playvideo")
            .appendQueryParameter("videoJson", json)
            .build()
    }

    @SuppressLint("RestrictedApi")
    fun PreviewProgram.Builder.buildProgram(video: Video, channelId: Long, intentUri: Uri, imageUri: Uri) {
        this
            .setChannelId(channelId)
            .setType(TvContractCompat.PreviewPrograms.TYPE_MOVIE)
            .setTitle(video.name)
            .setDescription(video.description)
            .setPosterArtUri(imageUri)
            .setIntentUri(intentUri)
            .setInternalProviderId("348")
        applicationContext.contentResolver.insert(
            TvContractCompat.PreviewPrograms.CONTENT_URI,
            this.build().toContentValues()
        )
    }
}