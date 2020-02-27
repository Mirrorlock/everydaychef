package com.everydaychef.main.ui.cook

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.MessageUtility
import kotlinx.android.synthetic.main.fragment_cook.*
import javax.inject.Inject

class CookFragment : Fragment() {

    @Inject
    lateinit var cookViewModel: CookViewModel
    @Inject
    lateinit var messageUtility: MessageUtility

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cook, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as EverydayChefApplication).appComponent.inject(this)
        messageUtility.subscribe(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cookViewModel.getRecommendedRecipes()
        cookViewModel.recipes.observe(viewLifecycleOwner, Observer{
            if(it.isNotEmpty()){
                progressBar.visibility = View.GONE
                Log.println(Log.DEBUG, "PRINT", "Found recipes: $it")
                if(recipes_list_view.adapter == null)
                    recipes_list_view.adapter = CookDataAdapter(context!!, R.layout.row_recipe, it)
                else{
                    (recipes_list_view.adapter as CookDataAdapter).notifyDataSetChanged()
                }
            }
        })
    }

}