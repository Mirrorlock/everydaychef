package com.everydaychef.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.PopupUtility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    @Inject lateinit var authViewModel: AuthViewModel
    private val popupUtility: PopupUtility = PopupUtility(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as EverydayChefApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        setupAuth()
    }

    private fun setupAuth(){
        authViewModel.setupFacebookSignIn(facebook_sign_in_button)
        authViewModel.setupGoogleSignIn(this, google_sign_in_button)
        authViewModel.authenticationState.observe(this, Observer{ authenticationState ->
            when(authenticationState){
                AuthenticationState.MANUALLY_AUTHENTICATED,
                AuthenticationState.FACEBOOK_AUTHENTICATED,
                AuthenticationState.GOOGLE_AUTHENTICATED  -> returnToMain()
                AuthenticationState.INVALID_AUTHENTICATION -> showError("Invalid Authentication!")
                else -> {}
            }
        })
    }


    private fun showError(s: String) {
        // TODO: Implement snackbar error!
        popupUtility.displayShortDefault(s)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            authViewModel.RC_GOOGLE_SIGN_IN   -> {
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
            authViewModel.RC_FACEBOOK_SIGN_IN -> {
                authViewModel.facebookCallbackManager
                    .onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            authViewModel.googleSignInSuccessful(completedTask!!.result!!, applicationContext!!)
        } catch (e: ApiException) {
            Log.println(Log.ERROR, "GOOGLE-SIGN-IN", e.printStackTrace().toString())
        }
    }


    private var doubleBackToExitPressedOnce = false
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        popupUtility.displayShortTop("Press 'back' again to exit!")
        showError("Log in first")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun returnToMain() {
        findNavController(R.id.login_host).popBackStack()
        finish()
    }

    fun loginOnClick(view: View) {
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        if(username.length > 3 && password.length > 3){
            authViewModel.manuallySignIn(username, password)
        }else{
            authViewModel.refuseAuthentication()
        }
    }

    fun registerOnClick(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }


}

