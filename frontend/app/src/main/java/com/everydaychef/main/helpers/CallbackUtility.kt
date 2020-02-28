package com.everydaychef.main.helpers

import android.util.Log
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.Family
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CallbackUtility<T> constructor(private val callerFuncName: String,
                                     private val successMessage: String = "",
                                     private val messageUtility: MessageUtility,
                                     private val successFunc: (responseBody: T) -> Unit
                                     ): Callback<T>{

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        Log.println(Log.DEBUG, "PRINT", "Error in ${callerFuncName}:  ${t.toString()}")
        Log.println(Log.ERROR, "process_create_family",
            "Error in ${callerFuncName}: ${t.toString()}")
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response != null) {
            if(response.isSuccessful){
                successFunc(response.body())
                if(successMessage.isNotEmpty())
                messageUtility.setMessage(successMessage)
            }else{
                messageUtility.setMessage("Error!")
            }
        }else{
            messageUtility.setMessage("Error in server!")
        }
    }
}