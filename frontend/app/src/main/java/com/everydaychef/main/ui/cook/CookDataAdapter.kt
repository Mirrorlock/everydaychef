package com.everydaychef.main.ui.cook

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.everydaychef.main.helpers.ViewHolder
import com.everydaychef.main.models.Recipe
import kotlinx.android.synthetic.main.row_recipe.view.*


class CookDataAdapter(context: Context, private val resource: Int,
                      private val objects: MutableList<Recipe>) :
    ArrayAdapter<Recipe>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView: View
        rowView = if(convertView == null){
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(resource, parent, false)
        }else{
            convertView
        }
        rowView.recipe_name.text = objects[position].name
//        val rowView: View = inflater.inflate(resource, parent, false)

        return rowView
    }

}