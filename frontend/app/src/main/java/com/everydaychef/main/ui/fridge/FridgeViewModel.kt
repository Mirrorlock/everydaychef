package com.everydaychef.main.ui.fridge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.IngredientRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class FridgeViewModel @Inject constructor(private val userRepository: UserRepository,
                                  private val familyRepository: FamilyRepository,
                                  private val ingredientRepository: IngredientRepository
): ViewModel() {

    var familyIngredients = MutableLiveData<ArrayList<Ingredient>>()
    var allIngredients = ArrayList<Ingredient>()
    var message = MutableLiveData<String>()
    val currentUser: CurrentUser?
        get() = userRepository.currentUserLd.value

    fun getFamilyIngredients() {
        familyRepository.getFamilyIngredients(userRepository.currentUserLd.value!!.user.family.id,
            familyIngredients, message)
    }

    fun getAllOtherIngredients(): Array<String>{
        allIngredients = ingredientRepository.getAllIngredients(message)
        if(!familyIngredients.value.isNullOrEmpty()){
            allIngredients.removeAll(familyIngredients.value!!)
        }
        return allIngredients.map{ ingredient -> ingredient.name}.toTypedArray()

    }

    fun deleteIngredient(familyId: Int, ingredient: Ingredient) {
        familyRepository.deleteIngredient(familyId, ingredient.id, message)
        val newInvitaionsList = familyIngredients.value
        newInvitaionsList?.remove(ingredient)
        familyIngredients.value = newInvitaionsList
    }

    fun addItems(checkedItems: ArrayList<Int>) {
        for(checkedItem in checkedItems){
            val ingredientId = allIngredients[checkedItem].id
            familyRepository.addIngredients(currentUser!!.user.family.id, ingredientId, message,
                userRepository.currentUserLd)
            var newIngrList = familyIngredients.value
            newIngrList?.add(allIngredients[checkedItem])
            familyIngredients.value = newIngrList
        }
    }

}