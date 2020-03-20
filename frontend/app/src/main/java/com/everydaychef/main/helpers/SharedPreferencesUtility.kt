package com.everydaychef.main.helpers

import android.content.Context
import android.content.SharedPreferences
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class SharedPreferencesUtility @Inject constructor(private val context: Context) {
    fun setPreference(preference: String, key: String, value: String){
        var  editor : SharedPreferences.Editor = context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun getPreference(preference: String, key: String): String{
        return context.getSharedPreferences(preference, Context.MODE_PRIVATE).getString(key, "")
            .toString()
    }
}