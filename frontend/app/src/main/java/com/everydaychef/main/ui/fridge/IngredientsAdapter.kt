package com.everydaychef.main.ui.fridge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.everydaychef.R
import com.everydaychef.main.models.Ingredient
import kotlinx.android.synthetic.main.row_item.view.*

class IngredientsAdapter constructor(context: Context,
                                     resource: Int,
                                     private val objects: MutableList<Ingredient>,
                                     private val fridgeViewModel: FridgeViewModel
): ArrayAdapter<Ingredient>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_item, parent, false)
        val currentItem = objects[position]
        rowView.item_name.text = currentItem.name
        rowView.item_delete_button.setOnClickListener {
            fridgeViewModel.deleteIngredient(fridgeViewModel.currentUser!!.user.family.id, currentItem)
        }
        return rowView
    }
}