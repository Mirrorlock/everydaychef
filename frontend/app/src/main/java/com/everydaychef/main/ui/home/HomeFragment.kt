package com.everydaychef.main.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.auth.AuthViewModel
import com.everydaychef.main.ui.cook.CookDataAdapter
import com.everydaychef.main.ui.cook.CookFragmentDirections
import com.everydaychef.main.ui.recipe.new_recipe.NewRecipeFragment
import kotlinx.android.synthetic.main.fragment_cook.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var homeViewModel: HomeViewModel
    @Inject
    lateinit var loginViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (this.activity?.application as EverydayChefApplication).appComponent.inject(this)
        Log.println(Log.DEBUG, "PRINT", "Our current user is: " + loginViewModel.currentUserLd.value.toString())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_go_to_create_recipe.setOnClickListener{
            findNavController().navigate(R.id.fragment_new_recipe)
        }

        homeViewModel.currentUser.observe(viewLifecycleOwner, Observer{
            Log.println(Log.DEBUG, "PRINT", "User changed to: $it")
            if(it.isUserSigned()){
                Log.println(Log.DEBUG, "PRINT", "User is signed in!")
                homeViewModel.getFavouriteRecipes()
            }else{
                Log.println(Log.DEBUG, "PRINT", "User is NOT signed in!")
            }
        })

        homeViewModel.favRecipes.observe(viewLifecycleOwner, Observer{
            if(it.isNotEmpty()){
                favRecProgressBar.visibility = View.GONE
                Log.println(Log.DEBUG, "PRINT", "Fav recipes changed to: $it")
                if(favourite_recipes_list_view.adapter == null){
                    favourite_recipes_list_view.adapter = CookDataAdapter(context!!,
                        R.layout.row_recipe, it, homeViewModel) {
                        val action = HomeFragmentDirections.actionNavHomeToRecipeFragment(it, true)
                        (context as Activity).findNavController(R.id.nav_host_fragment).navigate(action)
                    }
                }else{
                    (favourite_recipes_list_view.adapter as CookDataAdapter).notifyDataSetChanged()
                }
            }
        })


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
//            R.id/**/.google_sign_in_button -> activity?.let { loginViewModel?.googleSignIn(it) }
//            R.id.regular_sign_in_button -> {
//                activity?.let {
//                    val startLoginActivityIntent = Intent(activity, LoginActivity::class.java)
//                        startActivity(startLoginActivityIntent)
//                }
//            }
        }
    }

}