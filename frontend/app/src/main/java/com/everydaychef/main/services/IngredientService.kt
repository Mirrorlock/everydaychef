package com.everydaychef.main.services

import com.everydaychef.main.models.Ingredient
import retrofit2.Call
import retrofit2.http.GET


interface IngredientService {
    @GET("ingredients")
    fun getAllIngredients(): Call<ArrayList<Ingredient>>
}