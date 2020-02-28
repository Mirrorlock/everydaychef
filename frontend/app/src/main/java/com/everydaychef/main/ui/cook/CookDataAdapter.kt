package com.everydaychef.main.ui.cook

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.everydaychef.R
import com.everydaychef.main.helpers.ViewHolder
import com.everydaychef.main.models.Recipe
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.row_recipe.view.*


class CookDataAdapter(context: Context,
                      private val resource: Int,
                      private val objects: MutableList<Recipe>,
                      private val viewModel: CookViewModel) :
    ArrayAdapter<Recipe>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView: View = if(convertView == null){
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(resource, parent, false)
        }else{
            convertView
        }
        val recipe = objects[position]
        rowView.recipe_name.text = recipe.name
        rowView.recipe_author_name.text = recipe.creator?.name ?: ""
        rowView.recipe_likes_number.text = recipe.number_of_likes.toString()
        rowView.recipe_image.setOnClickListener {
//            it.findNavController().navigate()
//            (context as Fragment).findNavController().navigate()
        }
        val hasUserLiked = viewModel.hasUserLiked(recipe)
        
        if(hasUserLiked) {
            rowView.recipe_like_button.setImageResource(R.drawable.heart_on)
        }else{
            rowView.recipe_like_button.setImageResource(R.drawable.heart_off)
        }
        rowView.recipe_like_button.setOnClickListener {
            viewModel.rateRecipe(recipe, rowView.recipe_like_button, !viewModel.hasUserLiked(recipe), position)
        }
        val pictureUrl = recipe.picture_url
        if(pictureUrl.length > 3) {
            Glide.with(context)
                .load(pictureUrl)
                .into(rowView.recipe_image)
        }
        return rowView
    }

}