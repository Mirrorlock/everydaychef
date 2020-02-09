package com.everydaychef.ui.home

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.everydaychef.LoginViewModel
import com.everydaychef.MainActivity
import com.everydaychef.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import kotlin.math.log

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var loginViewModel: LoginViewModel = LoginViewModel()

    fun updateUI(root: View){
        Log.println(Log.DEBUG, "PRINT", "Updating UI - ${loginViewModel.isUserSignedIn()}")
        if( loginViewModel.isUserSignedIn() ) {
            val googleSignInButton = root.findViewById<SignInButton>(R.id.sign_in_button)
            val regularSignInButton = root.findViewById<Button>(R.id.regular_sign_in_button)
            val registerButton = root.findViewById<Button>(R.id.register_button)
            googleSignInButton.visibility = View.GONE
            regularSignInButton.visibility = View.GONE
            registerButton.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        loginViewModel.authenticationState.observe(this, Observer {
//            Log.println(Log.DEBUG, "PRINT", it.toString() + " has been changed!")
//        })

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        updateUI(root)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.println(Log.DEBUG, "PRINT", " View HOME has been created!")
        activity?.let{
            loginViewModel =
                ViewModelProviders.of(it).get(LoginViewModel::class.java)
            loginViewModel.test.observe(viewLifecycleOwner, Observer {
                Log.println(Log.DEBUG, "PRINT", "New value for test: " + it)
            })
        }
    }

}