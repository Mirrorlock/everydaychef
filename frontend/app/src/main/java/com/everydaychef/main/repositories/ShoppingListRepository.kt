package com.everydaychef.main.repositories

import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.ShoppingList
import com.everydaychef.main.services.ShoppingListService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListRepository @Inject constructor(private val shoppingListService: ShoppingListService) {
    fun deleteIngredient(shoppingListId: Int, ingredientId: Int): Call<ShoppingList> {
        return shoppingListService.deleteIngredient(shoppingListId, ingredientId)
    }

    fun addItem(shoppingListId: Int, ingredientId: Int): Call<List<Ingredient>>  {
        return shoppingListService.addItem(shoppingListId, ingredientId)
    }

    fun getIngredients(shoppingListId: Int): Call<List<Ingredient>> {
        return shoppingListService.getIngredients(shoppingListId)
    }

    fun create(familyId: Int, name: String): Call<ShoppingList> {
        return shoppingListService.create(familyId, mapOf("name" to name))
    }

    fun deleteShoppingList(id: Int): Call<Boolean> {
        return shoppingListService.deleteShoppingList(id)
    }
}