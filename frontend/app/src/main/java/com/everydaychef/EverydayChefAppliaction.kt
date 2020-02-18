package com.everydaychef

import android.app.Application

class EverydayChefApplication: Application(){
    val appComponent = DaggerApplicationComponent.create()
}