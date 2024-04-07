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
import com.example.androidtvtestapp.repository.VideoRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChannelsLoaderService : Service() {

    private var mainChannelId : Long = -1
    private var programIds = arrayListOf<Long>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = VideoRepository()
            val videos = repository.getVideos(10)

            if (mainChannelId < 0) {
                mainChannelId = createChannel("Новинки")
            }

            when (intent?.action) {
                "CREATE_CHANNEL" -> {
                    createPrograms(mainChannelId, videos)
                }
                "UPDATE_CHANNEL" -> {
                    updateChannel(mainChannelId)
                    updatePrograms()
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createChannel(channelName : String) : Long {
        val channelBuilder = Channel.Builder()
        channelBuilder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
            .setDisplayName(channelName)
        val channelUri = applicationContext.contentResolver.insert(
            TvContractCompat.Channels.CONTENT_URI, channelBuilder.build().toContentValues()
        )
        val channelId = ContentUris.parseId(channelUri!!)
        val resourceId = R.drawable.channel_logo
        val packageName = applicationContext.packageName
        val channelLogoUri = Uri.parse("android.resource://$packageName/$resourceId")
        ChannelLogoUtils.storeChannelLogo(applicationContext, channelId, channelLogoUri)

        TvContractCompat.requestChannelBrowsable(applicationContext, channelId)

        return channelId
    }

    private fun createPrograms(channelId: Long, videos : List<Video>) {
        val previewProgramBuilder = PreviewProgram.Builder()
        val gson = Gson()

        for (video in videos) {
            val programId = previewProgramBuilder.buildProgram(
                video, channelId, getIntentUri(gson.toJson(video))
            )

            if (programId > -1) {
                programIds.add(programId)
            }
        }
    }

    private fun updatePrograms() {
        val previewProgramBuilder = PreviewProgram.Builder()

        for (programId in programIds) {
            applicationContext.contentResolver.update(
                TvContractCompat.buildPreviewProgramUri(programId),
                previewProgramBuilder.build().toContentValues(), null, null
            )
        }
    }

    private fun updateChannel(channelId: Long) {
        val channelBuilder = Channel.Builder()

        applicationContext.contentResolver.update(
            TvContractCompat.buildChannelUri(channelId),
            channelBuilder.build().toContentValues(),
            null,
            null
        )
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
    private fun PreviewProgram.Builder.buildProgram(video: Video, channelId: Long, intentUri: Uri) : Long {
        this
            .setChannelId(channelId)
            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
            .setTitle(video.name)
            .setDescription(video.description)
            .setPosterArtUri(Uri.parse(video.thumbnailSmall))
            .setIntentUri(intentUri)
            .setInternalProviderId(video.videoProviderId.toString())
        val programUri = applicationContext.contentResolver.insert(
            TvContractCompat.PreviewPrograms.CONTENT_URI,
            this.build().toContentValues()
        )

        return if (programUri != null) {
            ContentUris.parseId(programUri)
        } else {
            -1
        }
    }
}