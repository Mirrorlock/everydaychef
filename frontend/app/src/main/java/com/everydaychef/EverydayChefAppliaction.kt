package com.everydaychef

import android.app.Application
import android.content.Context
import com.everydaychef.main.helpers.firebase_messaging.ChannelsUtility
import com.everydaychef.modules.AppModule


class EverydayChefApplication: Application(){
    companion object{
        //        val ApiBaseURL = "https://everydaychef.home.kutiika.net/"
        const val API_BASE_URL = "http://192.168.1.13/"
    }

    lateinit var appComponent: ApplicationComponent
    lateinit var channelsUtility: ChannelsUtility

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
        channelsUtility = ChannelsUtility()
        channelsUtility.createNotificationChannels(this)
    }
}