package com.everydaychef.main.ui.cook

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.RecipeRepository
import com.everydaychef.main.repositories.UserRepository
import kotlinx.android.synthetic.main.row_recipe.view.*
import java.io.File
import javax.inject.Inject

open class CookViewModel @Inject constructor(private val userRepository: UserRepository,
                                             private val familyRepository: FamilyRepository,
                                             private val recipeRepository: RecipeRepository,
                                             private val messageUtility: MessageUtility) : ViewModel() {
    var recipes = MutableLiveData<ArrayList<Recipe>>()


    fun getRecommendedRecipes() {
        familyRepository.getRecommendedRecipes(userRepository.currentUserLd.value!!.user.family.id)
            .enqueue(CallbackUtility<ArrayList<Recipe>>("getRecommendedRecipes",
                messageUtility= messageUtility){
                recipes.value = it
                recipeRepository.recipes = it
            })
    }

    open fun rateRecipe(recipe: Recipe, likeButton: ImageView, isLike: Boolean,
                    recipePosition: Int) {
        val successMessage = if(isLike) "Liked recipe" else "Disliked recipe"
        likeButton.isClickable = false
        recipeRepository.rateRecipe(recipe.id, userRepository.currentUserLd.value!!.id, isLike)
            .enqueue(CallbackUtility<Recipe>("rateRecipe",
                successMessage, messageUtility = messageUtility){
                if(isLike) likeButton.setImageResource(R.drawable.heart_on)
                else likeButton.setImageResource(R.drawable.heart_off)
                likeButton.isClickable = true
                recipes.value?.set(recipePosition, it)
                recipes.value = recipes.value
                recipeRepository.recipes = recipes.value!!
            })
    }

    fun hasUserLiked(recipe: Recipe): Boolean {
        return recipe.likedUsers.contains(userRepository.currentUserLd.value!!.user)
    }

}
