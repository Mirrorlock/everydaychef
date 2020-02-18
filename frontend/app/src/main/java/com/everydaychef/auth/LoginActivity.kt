package com.everydaychef.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.everydaychef.EverydayChefApplication
import com.everydaychef.main.MainActivity
import com.everydaychef.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    @Inject lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as EverydayChefApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        setupAuth()
    }

    private fun setupAuth(){
        authViewModel.setupFacebookSignIn(facebook_sign_in_button)
        authViewModel.setupGoogleSignIn(this, google_sign_in_button)
        authViewModel.currentUser.observe(this, Observer{
            Log.println(Log.DEBUG, "PRINT", "Changed current user!")
            if(authViewModel.isUserSignedIn())
                startMainActivity()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            authViewModel.RC_GOOGLE_SIGN_IN   -> {
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
//                startMainActivity()
//                popupUtility.displayShortTop("Successfully logged in as: " + authViewModel.email.value)
            }
            authViewModel.RC_FACEBOOK_SIGN_IN -> {
                authViewModel.facebookCallbackManager
                    .onActivityResult(requestCode, resultCode, data)
//                startMainActivity()

//                popupUtility.displayShortTop("Successfully logged in as: " + authViewModel.email.value)
            }
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            authViewModel.googleSignInSuccessful(completedTask!!.result!!)
        } catch (e: ApiException) {
            Log.println(Log.ERROR, "GOOGLE-SIGN-IN", e.printStackTrace().toString())
        }
    }

    private fun startMainActivity(){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}

