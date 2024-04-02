package com.example.androidtvtestapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CardUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.TIME_TICK") {
            val serviceIntent = Intent(context, CardUpdateService::class.java)
            context.startService(serviceIntent)
        }
    }
}