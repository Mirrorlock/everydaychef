package com.everydaychef.main.helpers

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class PopupUtility(private val context: Context) {

    fun displayShortDefault(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun displayShortTop(message: String){
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    fun displayLongDefault(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun displayLongTop(message: String){
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
}