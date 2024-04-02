package com.example.androidtvtestapp.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.IBinder
import androidx.tvprovider.media.tv.Channel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import com.example.androidtvtestapp.model.Video
import com.example.androidtvtestapp.network.Api
import com.example.androidtvtestapp.network.ExternalApiOptions
import com.example.androidtvtestapp.network.RetrofitClient
import com.google.gson.Gson

class CardUpdateService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val existingChannelId = getExistingChannel(applicationContext, "Channel Name")
        existingChannelId?.let { startCardUpdate(it) }

        return START_STICKY
    }

    private fun updateChannel(existingChannelId: Long) {
        val builder = Channel.Builder()
        builder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
            .setDisplayName("New Channel Name")

        applicationContext.contentResolver.update(
            TvContractCompat.buildChannelUri(existingChannelId),
            builder.build().toContentValues(),
            null,
            null
        )

        val api = RetrofitClient.instance.create(Api::class.java)
        val response = api.getVideos(3, ExternalApiOptions.API_KEY, 10, 9).execute()

        if (response.isSuccessful) {
            val responseResult = response.body()
            val videos = responseResult?.videos ?: emptyList()

            val gson = Gson()

            applicationContext.contentResolver.update(
                TvContractCompat.buildChannelUri(existingChannelId),
                builder.build().toContentValues(),
                null,
                null
            )

            TvContractCompat.requestChannelBrowsable(applicationContext, existingChannelId)
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
                    video, existingChannelId, getIntentUri(gson.toJson(video)), uriArray[i]
                )

                i++

                if (i > max) {
                    i = 0
                }
            }
        }

        val intent = Intent("com.example.UPDATE_CARDS_ACTION")
        sendBroadcast(intent)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun startCardUpdate(existingChannelId: Long) {
        if (existingChannelId > -1) {
            updateChannel(existingChannelId)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, CardUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        val intervalMillis: Long = 60 * 1000
        val triggerAtMillis = System.currentTimeMillis() + intervalMillis
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
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

    private fun getExistingChannel(context: Context, channelName: String): Long? {
        val projection = arrayOf(TvContractCompat.Channels._ID)
        val selection = "${TvContractCompat.Channels.COLUMN_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(channelName)
        val sortOrder = null

        val cursor: Cursor? = context.contentResolver.query(
            TvContractCompat.Channels.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val channelIdIndex = it.getColumnIndex(TvContractCompat.Channels._ID)
                return it.getLong(channelIdIndex)
            }
        }

        return null
    }
}