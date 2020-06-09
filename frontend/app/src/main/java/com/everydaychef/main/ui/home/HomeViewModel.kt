package com.everydaychef.main.ui.home

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.RecipeRepository
import com.everydaychef.main.repositories.UserRepository
import com.everydaychef.main.ui.cook.CookViewModel
import javax.inject.Inject
import javax.security.auth.callback.Callback

class HomeViewModel @Inject constructor(private val userRepository: UserRepository,
                                        private val messageUtility: MessageUtility,
                                        private val familyRepository: FamilyRepository,
                                        private val recipeRepository: RecipeRepository
): CookViewModel(userRepository, familyRepository, recipeRepository, messageUtility){

    val favRecipes = MutableLiveData<ArrayList<Recipe>>()
    var currentUser = userRepository.currentUserLd


    fun getFavouriteRecipes(){
        Log.println(Log.DEBUG, "PRINT", "Sending request for userId: " + userRepository.currentUserLd.value!!.id)
        userRepository.getLikedRecipes(userRepository.currentUserLd.value!!.id)
            .enqueue(CallbackUtility<ArrayList<Recipe>>("getFavouriteRecipes",
                messageUtility = messageUtility){
                Log.println(Log.DEBUG, "PRINT", "Found favourite recipes: $it")
                favRecipes.value = it
                recipeRepository.favRecipes = it
            })
    }

    override
    fun rateRecipe(recipe: Recipe, likeButton: ImageView, isLike: Boolean,
                   recipePosition: Int) {
        //the button is supposed to do nothing and just show the number of likes.
    }

}