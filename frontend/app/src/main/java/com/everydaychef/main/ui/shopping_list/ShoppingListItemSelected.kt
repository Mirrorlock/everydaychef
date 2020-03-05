package com.everydaychef.main.ui.shopping_list

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import com.everydaychef.main.helpers.MessageUtility

class ShoppingListItemSelected constructor(private val shoppingListViewModel: ShoppingListViewModel,
                                           private val messageUtility: MessageUtility,
                                           private val viewManipulation: () -> Unit)
    : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p2 >= shoppingListViewModel.shoppingLists.value!!.size){
            var newSlName = EditText(p0!!.context)
            AlertDialog.Builder(p0.context).setTitle("New Shopping list")
                .setView(newSlName)
                .setPositiveButton("Create"){ dialog, which ->
                    if(newSlName.text.isNotEmpty()) {
                        shoppingListViewModel.createShoppingList(newSlName.text.toString())
                    }else{
                        messageUtility.setMessage("Must type something!")
                    }
                }.setNeutralButton("Cancel"){dialog, which ->  }
                .create().show()
        }else{
            viewManipulation()
            shoppingListViewModel.changeCurrentShoppingList(shoppingListViewModel.shoppingLists.value!![p2])
        }
    }
}