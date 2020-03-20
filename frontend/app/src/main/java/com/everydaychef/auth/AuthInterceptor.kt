package com.everydaychef.auth

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.everydaychef.EverydayChefApplication
import com.everydaychef.main.MainActivity
import com.everydaychef.main.helpers.SharedPreferencesUtility
import com.everydaychef.main.repositories.UserRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val sharedPreferencesUtility: SharedPreferencesUtility)
    : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {

        var newRequest = chain!!.request()
        val token: String = sharedPreferencesUtility
            .getPreference(UserRepository.authSharedPref, "token")
        if(token.isNotEmpty()){
            val method: String = sharedPreferencesUtility
                .getPreference(UserRepository.authSharedPref, "method")
            Log.d("PRINT", "User signed in! Setting headers!")
            newRequest = newRequest.newBuilder()
                .addHeader("Authorization",  "Bearer $token")
                .addHeader("AuthenticationMethod", method)
                .build()
        }
        return chain.proceed(newRequest)
    }
}