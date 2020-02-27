package com.everydaychef

import android.app.Application
import com.everydaychef.modules.AppModule

class EverydayChefApplication: Application(){
    val appComponent = DaggerApplicationComponent.builder()
        .appModule(AppModule(this))
        .build()!!
}