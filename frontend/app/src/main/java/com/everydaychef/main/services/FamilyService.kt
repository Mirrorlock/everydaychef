package com.everydaychef.main.services

import com.everydaychef.main.models.Family
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.models.User
import retrofit2.Call
import retrofit2.http.*

interface FamilyService {

    @GET("family/{id}/members")
    fun getMembers(@Path("id") id: Int): Call<Set<User>>

    @GET("family/{id}/nonmembers")
    fun getNonMembers(@Path("id") familyId: Int): Call<ArrayList<User>>

    @GET("family/{id}/ingredients")
    fun getFamilyIngredients(@Path("id") familyId: Int): Call<ArrayList<Ingredient>>

    @GET("family/{id}/recommended_recipes")
    fun getRecommendedRecipes(@Path("id") familyId: Int): Call<ArrayList<Recipe>>

    @PUT("user/{userId}/family/{familyName}")
    fun changeUserFamily(@Path("userId") userId: Int,
                         @Path("familyName") familyName: String) : Call<Family>

    @POST("family/{familyId}/ingredients/{ingredientId}")
    fun addIngredient(@Path("familyId") familyId: Int,
                              @Path("ingredientId") ingredientId: Int): Call<Family>

    @DELETE("family/{familyId}/ingredients/{ingredientId}")
    fun deleteIngredient(@Path("familyId") familyId: Int,
                         @Path("ingredientId") ingredientId: Int): Call<ArrayList<Ingredient>>
}