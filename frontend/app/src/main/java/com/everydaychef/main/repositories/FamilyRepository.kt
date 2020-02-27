package com.everydaychef.main.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Family
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.User
import com.everydaychef.main.services.FamilyService
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FamilyRepository @Inject constructor(private val familyService: FamilyService,
                                private val messageUtility: MessageUtility){


    fun createFamily(familyName: String, currentUser: MutableLiveData<CurrentUser>,
                     message: MutableLiveData<String>) {
        familyService.changeUserFamily(currentUser.value!!.id, familyName)
            .enqueue(CallbackUtility<Family>("createFamily",
                "Family created successfully!", messageUtility){family ->
                val newCurrentUser: CurrentUser = currentUser.value!!
                newCurrentUser.user.family = family
                currentUser.value = newCurrentUser
            })

       /* .enqueue(object : Callback<Family> {
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
        })*/
    }

    fun getNonMembers(familyId: Int, message: MutableLiveData<String>): ArrayList<User>{
        var uninvitedUsers = ArrayList<User>()
        val receiveUsersThread = Thread{
            var response = familyService.getNonMembers(familyId).execute()
            if(response.isSuccessful){
                uninvitedUsers = response.body()
            }else{
//                message.value = "There was an Error!"
            }
        }
        receiveUsersThread.start()
        receiveUsersThread.join()
        Log.println(Log.DEBUG,"PRINT", "Uninvited users thread received are " +
                uninvitedUsers.toString())
        return uninvitedUsers
    }

    fun deleteIngredient(familyId: Int, ingredientId: Int,
                         message: MutableLiveData<String>){
        familyService.deleteIngredient(familyId, ingredientId)
            .enqueue(CallbackUtility("deleteIngredient", "Item deleted successfully!",
                messageUtility){})

        /*.enqueue(object: Callback<ArrayList<Ingredient>>{
            override fun onFailure(call: Call<ArrayList<Ingredient>>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error in deleteIngredient(): " + t.toString())
                Log.println(Log.ERROR, "process_create_family", "Error in deleteIngredient(): "
                        + t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<Ingredient>>?,
                response: Response<ArrayList<Ingredient>>?
            ) {
                if (response != null) {
                    if(response.isSuccessful){
                        messageUtility.setMessage("Success!")
                    }else{
                        message.value = "Error in server!"
                    }
                }else{
                    message.value = "There was an Error"
                }
            }*/

//        })
    }

    fun getFamilyIngredients(familyId: Int, ingredients: MutableLiveData<ArrayList<Ingredient>>,
                       message: MutableLiveData<String>) {
        familyService.getFamilyIngredients(familyId).enqueue(object: Callback<ArrayList<Ingredient>>{
            override fun onFailure(call: Call<ArrayList<Ingredient>>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error in getIngredients(): " + t.toString())
                Log.println(Log.ERROR, "process_create_family", "Error in getIngredients(): "
                        + t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<Ingredient>>?,
                response: Response<ArrayList<Ingredient>>?
            ) {
                if (response != null) {
                    if(response.isSuccessful){
                        ingredients.value = response.body()
                    }else{
                        message.value = "Error in server!"
                    }
                }else{
                    message.value = "There was an Error"
                }
            }

        })
    }




    fun addIngredients(familyId: Int, ingredientId: Int, message: MutableLiveData<String>,
                       currentUser: MutableLiveData<CurrentUser>) {
        familyService.addIngredient(familyId, ingredientId).enqueue(object: Callback<Family>{
            override fun onFailure(call: Call<Family>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error in getIngredients(): " + t.toString())
                Log.println(Log.ERROR, "process_create_family", "Error in getIngredients(): "
                        + t.toString())
            }

            override fun onResponse(call: Call<Family>?, response: Response<Family>?) {
                if (response != null) {
                    if(response.isSuccessful){
                        currentUser.value!!.user.family = response.body()
                        message.value = "Added items!"
                    }else{
                        message.value = "Error in server!"
                    }
                }else{
                    message.value = "There was an Error"
                }
            }

        })
    }

}