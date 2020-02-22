package com.everydaychef.main.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.Family
import com.everydaychef.main.models.User
import com.everydaychef.main.services.FamilyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FamilyRepository @Inject constructor(private val familyService: FamilyService){

    fun getMembers(id: Int){
        familyService.getMembers(id).enqueue(object: Callback<Set<User>> {
            override fun onFailure(call: Call<Set<User>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Set<User>>?, response: Response<Set<User>>?) {

            }
        })
    }

    fun createFamily(familyName: String, currentUser: MutableLiveData<CurrentUser>,
                     message: MutableLiveData<String>) {
        familyService.changeUserFamily(currentUser.value!!.id, familyName)
        .enqueue(object : Callback<Family> {
            override fun onFailure(call: Call<Family>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error in createFamily(): " + t.toString())
                Log.println(Log.ERROR, "process_create_family", "Error in createFamily(): "
                        + t.toString())
            }

            override fun onResponse(call: Call<Family>?, response: Response<Family>?) {
                if(response?.code() == 200) {
                    Log.println(Log.DEBUG, "PRINT", "Returned response for create fam: " +
                            response.body().toString())
                    val newCurrentUser: CurrentUser = currentUser.value!!
                    newCurrentUser.user.family = response.body()
                    currentUser.value = newCurrentUser
                }else{
                    message.value = "There was an error!"
                }
            }
        })

    }

}