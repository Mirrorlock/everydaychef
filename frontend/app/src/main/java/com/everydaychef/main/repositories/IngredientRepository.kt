package com.everydaychef.main.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.User
import com.everydaychef.main.services.IngredientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientRepository @Inject constructor(private val ingredientService: IngredientService){
    fun getAllIngredients(message: MutableLiveData<String>): ArrayList<Ingredient>{
            var ingredients = ArrayList<Ingredient>()
            val receivedItemsThread = Thread{
                ingredients = ingredientService.getAllIngredients()
                    .execute().body()
            }

            receivedItemsThread.start()
            receivedItemsThread.join()
            if(ingredients.isEmpty()){
                message.value = "There was an Error!"
            }
            Log.println(Log.DEBUG,"PRINT", "Items received are " +
            ingredients.toString())
            return ingredients
    }
}