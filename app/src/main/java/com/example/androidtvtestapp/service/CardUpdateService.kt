package com.example.androidtvtestapp.service

import android.app.Service
import android.content.Intent
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process

class CardUpdateService : Service() {

    override fun onCreate() {
        val thread = HandlerThread(
            "ServiceStartArguments",
            Process.THREAD_PRIORITY_BACKGROUND
        )
        thread.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serviceIntent = Intent(applicationContext, ChannelsLoaderService::class.java)
        serviceIntent.setAction("UPDATE_CHANNEL")
        applicationContext.startService(serviceIntent)

        return START_STICKY
    }
}