package com.everydaychef.main.ui.recipe.new_recipe

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.Recipe
import com.everydaychef.main.repositories.IngredientRepository
import com.everydaychef.main.repositories.RecipeRepository
import com.everydaychef.main.repositories.UserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class NewRecipeViewModel @Inject constructor(private val recipeRepository: RecipeRepository,
                                             private val ingredientRepository: IngredientRepository,
                                             private val userRepository: UserRepository,
                                             private val messageUtility: MessageUtility): ViewModel() {

    var selectedImage: MultipartBody.Part? = null
    val GALLERY_REQUEST_CODE: Int = 1
    var selectedIngredientsIndexes = ArrayList<Int>()
    var ingredients = MutableLiveData<List<Ingredient>>()
    var receivedIngredients = false
    var newRecipe = MutableLiveData<Recipe>()

    init {
        selectedIngredientsIndexes = ArrayList()
    }

    fun createNewRecipe(recipeName: String, recipeDescription: String) {
        recipeRepository.createRecipe(userRepository.currentUserLd.value!!.user.id,
                recipeName, recipeDescription, selectedImage!!)
            .enqueue(CallbackUtility<Recipe>("createNewRecipe",
            "Created recipe $recipeName", messageUtility = messageUtility ){
            Log.d("PRINT", "Created recipe: $it")
            newRecipe.value = it
        })


    }

    fun updateIngredients(){
        Log.d("PRINT", "Called update ingredients!")
        val selectedIngredients = selectedIngredientsIndexes.map{index -> ingredients.value!![index]}
        recipeRepository.updateIngredients(newRecipe.value!!.id, selectedIngredients as ArrayList<Ingredient>)
            .enqueue(CallbackUtility<Recipe>("updateIngredients",
                messageUtility= messageUtility){
                Log.d("PRINT", "Received updated recipe: $it")
                newRecipe.value = it
            })
    }

    fun getAllIngredients() {
        ingredientRepository.getAllIngredients()
            .enqueue(CallbackUtility<ArrayList<Ingredient>>
                ("getAllIngredientsNames", messageUtility = messageUtility){
                Log.d("PRINT", "Found ingredients: $it")
                receivedIngredients = true
                ingredients.value = it
            })
    }

    fun getPreviouslyChecked(): BooleanArray {
        val previouslyChecked = BooleanArray(ingredients.value!!.size)
        selectedIngredientsIndexes.forEach { index -> previouslyChecked[index] = true }
        return previouslyChecked
    }

}