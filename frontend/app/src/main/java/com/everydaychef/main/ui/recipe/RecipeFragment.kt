package com.everydaychef.main.ui.recipe

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.ImageUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.Ingredient
import kotlinx.android.synthetic.main.fragment_recipe.*
import javax.inject.Inject

class RecipeFragment: Fragment() {
    private val args: RecipeFragmentArgs by navArgs()
    @Inject lateinit var recipeViewModel: RecipeViewModel
    @Inject lateinit var messageUtility: MessageUtility
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as EverydayChefApplication).appComponent.inject(this)
        recipeViewModel.setRecipe(args.recipeIndex, args.isFav)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var pictureUrl = recipeViewModel.recipe.picture_url
        messageUtility.subscribe(viewLifecycleOwner)
        ImageUtility.parseImage(pictureUrl, recipe_image_view, context!!)
        recipe_description.text = recipeViewModel.recipe.description
        recipe_ingredients_list.adapter = RecipeDataAdapter(context!!, R.layout.row_item,
            recipeViewModel.recipe.ingredients as MutableList<Ingredient>, recipeViewModel
        )

        add_to_shopping_list_btn.setOnClickListener{
            addToShoppingList()
        }
    }

    private fun addToShoppingList() {
        recipeViewModel.getShoppingLists()

        recipeViewModel.shoppingLists.observe(viewLifecycleOwner, Observer {
            Log.d("PRINT", "Detected change in shopping lists: $it")
            if(it != null){
                val shoppingListsArray = it.map{it.name}.toTypedArray()
                var checkedIdx = 0
                AlertDialog.Builder(context!!)
                    .setTitle("Choose shopping list!")
                    .setSingleChoiceItems(shoppingListsArray, checkedIdx){dialog, which -> checkedIdx=which}
                    .setPositiveButton("Add"){dialog, which ->
                        messageUtility.setMessage(which.toString())
                        recipeViewModel.addToShoppingList(checkedIdx)
                    }
                    .setNeutralButton("Cancel", null)
                    .create().show()
            }
        })
    }

}
