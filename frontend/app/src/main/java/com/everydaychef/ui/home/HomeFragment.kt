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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.log

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var loginViewModel: LoginViewModel? = null

    fun updateUI(){

        loginViewModel?.let {
            if( it.isUserSignedIn() ) {
                sign_in_button.visibility = View.GONE
                regular_sign_in_button.visibility = View.GONE
                register_button.visibility = View.GONE
            }else{
                sign_in_button.visibility = View.VISIBLE
                regular_sign_in_button.visibility = View.VISIBLE
                register_button.visibility = View.VISIBLE
                sign_in_button.setOnClickListener(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        activity?.let{
            loginViewModel = ViewModelProviders.of(it).get(LoginViewModel::class.java)
            loginViewModel?.authenticationState?.observe(this, Observer {
                updateUI()
            })
        }
        return root
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sign_in_button -> activity?.let { loginViewModel?.googleSignIn(it) }
        }
    }

}