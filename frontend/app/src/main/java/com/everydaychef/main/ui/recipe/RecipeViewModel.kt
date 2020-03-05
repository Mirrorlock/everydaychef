package com.everydaychef.main.ui.recipe

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.models.ShoppingList
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.RecipeRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class RecipeViewModel @Inject constructor(private val userRepository: UserRepository,
                                          private val familyRepository: FamilyRepository,
                                          private val recipeRepository: RecipeRepository,
                                          private val messageUtility: MessageUtility): ViewModel() {
    lateinit var recipe: Recipe
    val shoppingLists = MutableLiveData<List<ShoppingList>>()

    fun setRecipe(recipeIndex: Int) {
        recipe = recipeRepository.recipes[recipeIndex]
        Log.d("PRINT", "Displaying recipe: $recipe")
    }

    fun isUserOwner(): Boolean {
        return recipe.creator == userRepository.currentUserLd.value!!.user
    }

    fun addToShoppingList(checkedShoppingListIdx: Int) {
        var sl = shoppingLists.value?.get(checkedShoppingListIdx)
        Log.d("PRINT", "Chosen shopping list is: $sl")
        recipeRepository.addToShoppingList(recipe.id, sl!!.id)
            .enqueue(CallbackUtility<Any>("addToShoppingList",
                "Ingredients added to list!", messageUtility){})
    }

    fun getShoppingLists() {
        familyRepository.getShoppingLists(userRepository.currentUserLd.value!!.user.family.id).
                enqueue(CallbackUtility<List<ShoppingList>>("getShoppingLists",
                    messageUtility = messageUtility){
                    shoppingLists.value = it
                })
    }


}