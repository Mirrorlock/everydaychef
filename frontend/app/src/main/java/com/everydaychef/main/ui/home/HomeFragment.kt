package com.everydaychef.main.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.auth.AuthViewModel
import javax.inject.Inject

class HomeFragment : Fragment(), View.OnClickListener {

    private var homeViewModel: HomeViewModel? = null
    @Inject
    lateinit var loginViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (this.activity?.application as EverydayChefApplication).appComponent.inject(this)
        Log.println(Log.DEBUG, "PRINT", "Our current user is: " + loginViewModel.currentUserLd.value.toString())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
//            loginViewModel = ViewModelProviders.of(it).get(LoginViewModel::class.java)
//            loginViewModel?.authenticationState?.observe(this, Observer {
//                updateHomeUI()
//            })
        }
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