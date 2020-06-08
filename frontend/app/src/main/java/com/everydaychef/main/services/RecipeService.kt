package com.everydaychef.main.services

import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.Recipe
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


interface RecipeService {
    @GET("recipes")
    fun getAllRecipes(): Call<ArrayList<Recipe>>


//    fun getLocalImage(@Path("pictureUrl") pictureUrl: String): Call<File>

    @PUT("recipe/{recipeId}/user/{userId}/{rate}")
    fun rateRecipe(@Path("recipeId") recipeId: Int,
                   @Path("userId") userId: Int, @Path("rate") rate: String): Call<Recipe>

    @Multipart
    @POST("recipe")
    fun create(@Part("creatorId") creatorId: Int, @Part("recipeName") recipeName: RequestBody,
               @Part("recipeDescription") recipeDescription: RequestBody,
               @Part file: MultipartBody.Part
               /*@Part("ingredients") ingredients: Map<String, Array<Int>>*/): Call<Recipe>

    @PUT("recipe/{id}/ingredients")
    fun updateIngredients(@Path("id") id: Int, @Body ingredientsBody: Map<String, Array<Int>>) : Call<Recipe>

    @POST("recipe/{recipeId}/shopping_list/{shoppingListId}")
    fun addToShoppingList(@Path("recipeId") recipeId: Int,
                          @Path("shoppingListId") shoppingListId: Int): Call<Any>

//    @PUT("recipe/{id}/ingredients")
//    fun updateIngredients(@Path())


}