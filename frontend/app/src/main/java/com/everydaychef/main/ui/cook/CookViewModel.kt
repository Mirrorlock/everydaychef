package com.everydaychef.main.ui.cook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.RecipeRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class CookViewModel @Inject constructor(private val userRepository: UserRepository,
                                        private val familyRepository: FamilyRepository,
                                        private val recipeRepository: RecipeRepository,
                                        private val messageUtility: MessageUtility) : ViewModel() {
    var recipes = MutableLiveData<ArrayList<Recipe>>()

    fun getRecommendedRecipes() {
        recipeRepository.getAllRecipes()
            .enqueue(CallbackUtility<ArrayList<Recipe>>("getRecommendedRecipes",
                "Recipes filtered successfully!", messageUtility){
                recipes.value = it
            })
    }
}
