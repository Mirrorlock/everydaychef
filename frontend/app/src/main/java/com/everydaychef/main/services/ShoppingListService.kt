package com.everydaychef.main.services

import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.ShoppingList
import retrofit2.Call
import retrofit2.http.*
import javax.inject.Inject

interface ShoppingListService{
    @GET("shopping_list/{id}/ingredients")
    fun getIngredients(@Path("id") shoppingListId: Int): Call<List<Ingredient>>

    @PUT("shopping_list/{shopping_list_id}/ingredient/{ingredient_id}")
    fun addItem(@Path("shopping_list_id") shoppingListId: Int,
                @Path("ingredient_id") ingredientId: Int): Call<List<Ingredient>>

    @POST("shopping_list/family/{id}")
    fun create(@Path("id") familyId: Int, @Body body: Map<String, String>): Call<ShoppingList>

    @DELETE("shopping_list/{id}")
    fun deleteShoppingList(@Path("id") id: Int): Call<Boolean>

    @DELETE("shopping_list/{shopping_list_id}/ingredient/{ingredient_id}")
    fun deleteIngredient(@Path("shopping_list_id") shoppingListId: Int,
                         @Path("ingredient_id") ingredientId: Int): Call<ShoppingList>
}
