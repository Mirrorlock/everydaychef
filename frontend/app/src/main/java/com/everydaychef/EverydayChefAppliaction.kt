package com.everydaychef

import android.app.Application
import android.content.Context
import com.everydaychef.modules.AppModule


class EverydayChefApplication: Application(){
    companion object{
        //        val ApiBaseURL = "https://everydaychef.home.kutiika.net/"
        const val API_BASE_URL = "http://192.168.1.8/"
    }

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}