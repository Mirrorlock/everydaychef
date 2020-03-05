package com.everydaychef.main.ui.fridge

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.everydaychef.main.models.Ingredient
import kotlinx.android.synthetic.main.row_item.view.*

class IngredientDataAdapter(
    context: Context,
    private val resource: Int,
    private val objects: List<Ingredient>,
    private val onDeleteClickListener: (currentIngredient: Ingredient) -> Unit
): ArrayAdapter<Ingredient>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView: View = if(convertView == null){
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(resource, parent, false)
        }else{
            convertView
        }

        val currentItem = objects[position]
        rowView.item_name.text = currentItem.name
        rowView.item_delete_button.setOnClickListener{
            Log.d("PRINT", "Clicked delete!")
            onDeleteClickListener(currentItem)
        }
        return rowView
    }
}