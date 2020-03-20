package com.everydaychef.auth

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.everydaychef.EverydayChefApplication
import com.everydaychef.main.MainActivity
import com.everydaychef.main.repositories.UserRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor /*constructor(context: Context)*/: Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
//        var context = EverydayChefApplication.mContext
//        var edit = context!!.getSharedPreferences(UserRepository.authSharedPref, Context.MODE_PRIVATE)
        var newRequest = chain!!.request()
        Log.d("PRINT", "Checking user signed in")
//        val token: String? = edit.getString("token", "")
        val token: String? = UserRepository.token
        if(token != null && token.isNotEmpty()){
            val method: String = UserRepository.method
//            val method: String = edit.getString("method", "")!!
            Log.d("PRINT", "User signed in! Setting headers!")
            newRequest = newRequest.newBuilder()
                .addHeader("Authorization",  "Bearer $token")
                .addHeader("AuthenticationMethod", method)
                .build()
        }
        return chain.proceed(newRequest)
    }
}