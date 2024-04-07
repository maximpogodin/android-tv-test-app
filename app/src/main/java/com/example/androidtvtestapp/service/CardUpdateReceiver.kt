package com.example.androidtvtestapp.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.androidtvtestapp.R

class CardUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, CardUpdateService::class.java)
        context.startService(serviceIntent)
        setAlarm(context)
    }

    fun setAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CardUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)
        val timeInSecs = context.resources.getInteger(R.integer.timer_to_card_update)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            (System.currentTimeMillis() / 1000L + timeInSecs) * 1000L,
            pendingIntent
        )
    }
}