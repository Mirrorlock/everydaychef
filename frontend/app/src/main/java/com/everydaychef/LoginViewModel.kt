package com.everydaychef

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.util.Log
import android.view.MenuItem
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginViewModel : ViewModel() {
    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        MANUALLY_AUTHENTICATED, // The user has authenticated successfully
        GOOGLE_AUTHENTICATED,   // The user has authenticated via google successfully
        FACEBOOK_AUTHENTICATED, // The user has authenticated via facebook successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val authenticationState = MutableLiveData<AuthenticationState>()
    var username: String
    var test = MutableLiveData<Int>()
    var email: String
    var photoURL: String

    init {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
        username = ""
        email    = ""
        photoURL = ""
    }

    fun isUserSignedIn(): Boolean {
        Log.println(Log.DEBUG, "PRINT", authenticationState.value.toString() + " from is user signed in!")
        return authenticationState.value.toString().contains("_AUTHENTICATED")
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    // MANUAL AUTHENTICATION //
    fun authenticate(username: String, password: String) {
        if (passwordIsValidForUsername(username, password)) {
            this.username = username
            authenticationState.value = AuthenticationState.MANUALLY_AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }


    fun manuallySignOut(){
        TODO("not implemented")
    }

    private fun passwordIsValidForUsername(username: String, password: String): Boolean {
        return true
    }
    // MANUAL AUTHENTICATION //


    /// GOOGLE AUTHENTICATION ///
    internal lateinit var mGoogleSignInClient: GoogleSignInClient
    var currentGoogleUser: GoogleSignInAccount? = null
    internal var GOOGLE_SIGN_IN = 7 // Google sign in request code

    fun setupGoogleSignIn(context: Context){
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun googleSignIn(activity: Activity){
        Log.println(Log.INFO, "Action", "Started process: google sign in")
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    fun googleSignInSuccessfull(googleUser: GoogleSignInAccount){
        currentGoogleUser = googleUser
        fillDataFromGoogleAccount()
//        authenticationState.value = AuthenticationState.GOOGLE_AUTHENTICATED
        Log.println(Log.DEBUG, "PRINT", authenticationState.value.toString() + " google sign in successful")
    }

    fun googleSignOut(activity: Activity){
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity) {
                    currentGoogleUser = null
                }
    }

    fun fillDataFromGoogleAccount() {
        username = currentGoogleUser?.displayName.toString()
        email    = currentGoogleUser?.email.toString()
        photoURL = currentGoogleUser?.photoUrl.toString()
    }

    // GOOGLE AUTHENTICATION //

    // FACEBOOK AUTHENTICATION //
    private fun facebookSignOut() {
        TODO("not implemented")
    }
    // FACEBOOK AUTHENTICATION //

    fun signOut(activity: Activity) {
        when(authenticationState) {
            AuthenticationState.MANUALLY_AUTHENTICATED -> manuallySignOut()
            AuthenticationState.GOOGLE_AUTHENTICATED   -> googleSignOut(activity)
            AuthenticationState.FACEBOOK_AUTHENTICATED -> facebookSignOut()
        }
    }
}
