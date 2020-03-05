package com.everydaychef.main.ui.shopping_list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Ingredient
import com.everydaychef.main.models.ShoppingList
import com.everydaychef.main.ui.fridge.IngredientDataAdapter
import android.R as Rnew

import kotlinx.android.synthetic.main.fragment_shopping_list.*
import javax.inject.Inject


class ShoppingListFragment : Fragment() {

    @Inject lateinit var shoppingListViewModel: ShoppingListViewModel
    @Inject lateinit var messageUtility: MessageUtility

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as EverydayChefApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        messageUtility.subscribe(viewLifecycleOwner)
        shoppingListViewModel.getFamilyShoppingLists()
        shoppingListViewModel.shoppingLists.observe(viewLifecycleOwner, Observer{
            populateDropDown(it)
        })

        shoppingListViewModel.currentShoppingListIngredients
            .observe(viewLifecycleOwner, Observer {ingredients ->
                ingredients?.let {
                    populateItemsList(
                        it)
                }
        })

        shopping_list_add_items.setOnClickListener{
            addItems()
        }

        shopping_list_delete_btn.setOnClickListener{
            shoppingListViewModel.deleteShoppingList()
        }

    }

    private fun addItems() {
        shoppingListViewModel.getAllOtherIngredients()
        shoppingListViewModel.allOtherIngredients.observe(viewLifecycleOwner, Observer {
            if(shoppingListViewModel.receivedAllOtherIngredients){
                var ingredientNames = it.map { ingredient -> ingredient.name }.toTypedArray()
                var checkedItems = ArrayList<Int>()
                AlertDialog.Builder(activity)
                    .setTitle("Add Items")
                    .setMultiChoiceItems( ingredientNames,  null)
                    { dialog, which, isChecked ->
                        if(isChecked) checkedItems.add(which)
                        else checkedItems.remove(which)
                    }.setPositiveButton("Add"){ dialog, which ->
                        shoppingListViewModel.addItems(checkedItems)
                    }.setNeutralButton("Cancel"){dialog, which -> }
                    .create().show()
                shoppingListViewModel.receivedAllOtherIngredients = false
            }
        })
    }

    private fun populateItemsList(ingredients: List<Ingredient>) {
        Log.d("PRINT", "Ingredients: $ingredients")
        sl_progressbar.visibility = View.GONE
        shopping_list_items.adapter = IngredientDataAdapter(context!!, R.layout.row_item,
            ingredients.toList()
        ){ currentItem ->
            shoppingListViewModel.deleteItem(currentItem)
        }
    }

    private fun populateDropDown(ingredients: List<ShoppingList>) {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            Rnew.layout.simple_spinner_item, ingredients.map{sl -> sl.name}.plus("New...")
        )
        dataAdapter.setDropDownViewResource(Rnew.layout.simple_spinner_dropdown_item)
        sl_dropdown.adapter = dataAdapter
        sl_dropdown.onItemSelectedListener = ShoppingListItemSelected(shoppingListViewModel, messageUtility){
            sl_progressbar.visibility = View.VISIBLE
        }
    }
}