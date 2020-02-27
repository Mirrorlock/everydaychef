package com.everydaychef.main.repositories

import com.everydaychef.main.models.Recipe
import com.everydaychef.main.services.RecipeService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(private val recipeService: RecipeService){
    fun getAllRecipes(): Call<ArrayList<Recipe>> {
        return recipeService.getAllRecipes()
    }
}