package com.everydaychef.main.helpers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageUtility @Inject constructor(private val popupUtility: PopupUtility) {
    private var message = MutableLiveData<String>()

    private fun showMessage(popupUtility: PopupUtility){
        if(message.value != null){
            if(message.value!!.isNotEmpty()) {
                popupUtility.displayShortDefault(message.value!!)
                message.value = ""
            }
        }
    }

    fun subscribe(lifecycleOwner: LifecycleOwner){
        message.observe(lifecycleOwner, Observer{
            showMessage(popupUtility)
        })
    }

    fun setMessage(message: String){
        this.message.value = message
    }



}