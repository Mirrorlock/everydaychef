package com.everydaychef

import android.app.Application
import android.content.Context
import com.everydaychef.modules.AppModule


class EverydayChefApplication: Application(){
    companion object{
        //        val ApiBaseURL = "https://everydaychef.home.kutiika.net/"
        const val API_BASE_URL = "http://192.168.1.8/"
//        var mContext: Context? = null
    }

//    override fun onCreate() {
//        super.onCreate()
//        mContext = applicationContext
//    }

    val appComponent = DaggerApplicationComponent.builder()
        .appModule(AppModule(this))
        .build()!!
}