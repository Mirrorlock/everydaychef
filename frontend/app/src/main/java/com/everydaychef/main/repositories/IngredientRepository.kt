package com.everydaychef.main.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.User
import com.everydaychef.main.services.IngredientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientRepository @Inject constructor(private val ingredientService: IngredientService,
                                               private val messageUtility: MessageUtility){
    fun getAllIngredients(): Call<ArrayList<Ingredient>>{
        return ingredientService.getAllIngredients()
    }
}