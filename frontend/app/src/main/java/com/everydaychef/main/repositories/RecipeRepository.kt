package com.everydaychef.main.repositories

import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.services.RecipeService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(private val recipeService: RecipeService){
    var recipes = ArrayList<Recipe>()

    var favRecipes = ArrayList<Recipe>()
    fun getAllRecipes(): Call<ArrayList<Recipe>> {
        return recipeService.getAllRecipes()
    }

//
//    fun getLikedRecipes(userId: Int): Call<ArrayList<Recipe>>{
//        return userService.getLikedRecipes()
//    }

    fun rateRecipe(recipeId: Int, userId: Int, isLike: Boolean): Call<Recipe>{
        return recipeService.rateRecipe(recipeId, userId, if(isLike) "like" else "dislike")
    }

    fun createRecipe(creatorId: Int, recipeName: String, recipeDescription: String, imageBody: MultipartBody.Part) : Call<Recipe>{
        var recipeNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), recipeName)
        var recipeDescriptionBody = RequestBody.create(MediaType.parse("multipart/form-data"), recipeDescription)
        return recipeService.create(creatorId, recipeNameBody, recipeDescriptionBody, imageBody)
    }


    fun updateIngredients(recipeId: Int, ingredients: ArrayList<Ingredient>): Call<Recipe>{
        var ingredientsBody = mapOf("ingredients" to ingredients.map{ingredient -> ingredient.id}.toTypedArray())
        return recipeService.updateIngredients(recipeId, ingredientsBody)
    }

    fun addToShoppingList(recipeId: Int, shoppingListId: Int) : Call<Any>{
        return recipeService.addToShoppingList(recipeId, shoppingListId)
    }



//    fun getLocalImage(pictureUrl: String) : Call<File>{
//        return recipeService.getLocalImage(pictureUrl)
//    }
}