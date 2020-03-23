package com.everydaychef.main.helpers.firebase_messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class ChannelsUtility {
    companion object{
        val CHANNEL_IMPORTANT_ID = "channel_important"
        val CHANNEL_INFO_ID      = "channel_info"
    }

    fun createNotificationChannels(context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val manager = context.getSystemService(NotificationManager::class.java)
            createChannel(CHANNEL_IMPORTANT_ID, "Important",
                NotificationManager.IMPORTANCE_HIGH,
                "The channel used for messages of high importance", manager)
            createChannel(CHANNEL_INFO_ID, "Info",
                NotificationManager.IMPORTANCE_LOW,
                "The channel used for messages of low importance", manager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, name: String, importance: Int, description: String,
                      manager: NotificationManager){
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        manager.createNotificationChannel(channel)
    }

}