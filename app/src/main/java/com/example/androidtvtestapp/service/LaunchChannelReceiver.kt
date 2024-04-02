package com.example.androidtvtestapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LaunchChannelReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, ChannelsLoaderService::class.java)
        context.startService(serviceIntent)
    }
}