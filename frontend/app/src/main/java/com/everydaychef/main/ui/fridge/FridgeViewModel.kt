package com.everydaychef.main.ui.fridge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.IngredientRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject
import javax.security.auth.callback.Callback

class FridgeViewModel @Inject constructor(private val userRepository: UserRepository,
                                          private val familyRepository: FamilyRepository,
                                          private val ingredientRepository: IngredientRepository,
                                          private val messageUtility: MessageUtility
): ViewModel() {

    var familyIngredients = MutableLiveData<ArrayList<Ingredient>>()
    var allOtherIngredients = MutableLiveData<List<Ingredient>>()
    var receivedOtherIngredients = false
    var message = MutableLiveData<String>()
    val currentUser: CurrentUser?
        get() = userRepository.currentUserLd.value

    fun getFamilyIngredients() {
        familyRepository.getFamilyIngredients(userRepository.currentUserLd.value!!.user.family.id,
            familyIngredients, message)
    }

    fun getAllOtherIngredients(){
        ingredientRepository.getAllIngredients()
            .enqueue(CallbackUtility<ArrayList<Ingredient>>("getAllOtherIngredients",
                messageUtility = messageUtility){
                receivedOtherIngredients = true
                var ingredients = it.filter { ingredient -> !familyIngredients.value!!.contains(ingredient) }
                allOtherIngredients.value = ingredients
            })
    }

    fun deleteIngredient(familyId: Int, ingredient: Ingredient) {
        familyRepository.deleteIngredient(familyId, ingredient.id, message)
        val newInvitaionsList = familyIngredients.value
        newInvitaionsList?.remove(ingredient)
        familyIngredients.value = newInvitaionsList
    }

    fun addItems(checkedItems: ArrayList<Int>) {
        for(checkedItem in checkedItems){
            val ingredientId = allOtherIngredients.value!![checkedItem].id
            familyRepository.addIngredients(currentUser!!.user.family.id, ingredientId, message,
                userRepository.currentUserLd)
            var newIngrList = familyIngredients.value
            newIngrList?.add(allOtherIngredients.value!![checkedItem])
            familyIngredients.value = newIngrList
        }
    }

}