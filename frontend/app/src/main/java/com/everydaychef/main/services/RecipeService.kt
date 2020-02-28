package com.everydaychef.main.services

import com.everydaychef.main.models.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface RecipeService {
    @GET("recipes")
    fun getAllRecipes(): Call<ArrayList<Recipe>>

    @PUT("recipe/{recipeId}/user/{userId}/{rate}")
    fun rateRecipe(@Path("recipeId") recipeId: Int,
                   @Path("userId") userId: Int, @Path("rate") rate: String): Call<Recipe>

}