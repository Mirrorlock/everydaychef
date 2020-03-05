package com.everydaychef.main.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.everydaychef.R
import com.everydaychef.main.models.Ingredient
import kotlinx.android.synthetic.main.row_item.view.*

class RecipeDataAdapter constructor(context: Context,
                                    resource: Int,
                                    private val objects: MutableList<Ingredient>,
                                    private val recipeViewModel: RecipeViewModel
): ArrayAdapter<Ingredient>(context, resource, objects)  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView: View = if(convertView == null){
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.row_item, parent, false)
        }else{
            convertView
        }
        val currentItem = objects[position]
        rowView.item_name.text = currentItem.name
        rowView.item_delete_button.visibility = View.GONE
        return rowView
    }

}