package com.everydaychef.main.ui.shopping_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.ShoppingList
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.IngredientRepository
import com.everydaychef.main.repositories.ShoppingListRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject
import javax.security.auth.callback.Callback

class ShoppingListViewModel @Inject constructor(private val userRepository: UserRepository,
                                                private val familyRepository: FamilyRepository,
                                                private val ingredientRepository: IngredientRepository,
                                                private val shoppingListRepository: ShoppingListRepository,
                                                private val messageUtility: MessageUtility)
    : ViewModel() {

    val currentShoppingList = MutableLiveData<ShoppingList>()
    var shoppingLists = MutableLiveData<List<ShoppingList>>()
    var allOtherIngredients = MutableLiveData<List<Ingredient>>()
    var receivedAllOtherIngredients = false
    var currentShoppingListIngredients = MutableLiveData<List<Ingredient>>()

    fun getFamilyShoppingLists() {
        familyRepository.getShoppingLists(userRepository.currentUserLd.value!!.user.family.id)
            .enqueue(CallbackUtility<List<ShoppingList>>("getFamilyShoppingLists",
                messageUtility = messageUtility){
                shoppingLists.value = it
            })
    }

    fun deleteItem(currentItem: Ingredient) {
        shoppingListRepository.deleteIngredient(currentShoppingList.value!!.id, currentItem.id)
            .enqueue(CallbackUtility("deleteItem",
                "Removed item from ${currentShoppingList.value!!.name}",
                messageUtility = messageUtility){
                currentShoppingListIngredients.value = currentShoppingListIngredients.value!!.minus(currentItem)
            })
    }

    fun getAllOtherIngredients() {
        ingredientRepository.getAllIngredients()
            .enqueue(CallbackUtility<ArrayList<Ingredient>>("getAllOtherIngredients",
                messageUtility= messageUtility){
                var ingredients = it
                    .filter{ingredient -> !currentShoppingListIngredients.value!!.contains(ingredient)}
                receivedAllOtherIngredients = true
                allOtherIngredients.value = ingredients
            })
    }

    fun addItems(checkedItems: ArrayList<Int>) {
        for(item in checkedItems){
            val ingredient = allOtherIngredients.value!![item]
            shoppingListRepository.addItem(currentShoppingList.value!!.id,
                ingredient.id)
                .enqueue(CallbackUtility<List<Ingredient>>("addItems",
                    "Successfully added items", messageUtility = messageUtility){
                    currentShoppingListIngredients.value = currentShoppingListIngredients.value!!.plus(ingredient)
                })
        }
    }

    fun changeCurrentShoppingList(shoppingList: ShoppingList) {
        currentShoppingList.value = shoppingList
        getIngredients(shoppingList.id)
    }

    private fun getIngredients(shoppingListId: Int){
        shoppingListRepository.getIngredients(shoppingListId)
            .enqueue(CallbackUtility<List<Ingredient>>("getIngredients", messageUtility = messageUtility){
                currentShoppingListIngredients.value = it
            })
    }

    fun createShoppingList(name: String) {
        shoppingListRepository.create(userRepository.currentUserLd.value!!.user.family.id, name)
            .enqueue(CallbackUtility<ShoppingList>("createShoppingListName",
                "Created new shopping list!", messageUtility=messageUtility){
                shoppingLists.value = shoppingLists.value!!.plus(it)
                changeCurrentShoppingList(it)
            })
    }

    fun deleteShoppingList() {
        currentShoppingList.value?.let {
            shoppingLists.value = shoppingLists.value!!.minus(it)
            shoppingListRepository.deleteShoppingList(it.id)
                .enqueue(CallbackUtility<Boolean>("deleteShoppingList()",
                    "Successfully deleted shopping list!", messageUtility=messageUtility
                    ){})
        }
    }
}