package com.everydaychef.main.services

import com.everydaychef.main.models.Recipe
import retrofit2.Call
import retrofit2.http.GET


interface RecipeService {
    @GET("recipes")
    fun getAllRecipes(): Call<ArrayList<Recipe>>

}