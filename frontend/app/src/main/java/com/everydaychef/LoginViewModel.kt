package com.everydaychef

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.*
import com.facebook.login.LoginFragment
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.json.JSONException


class LoginViewModel : ViewModel() {
    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        MANUALLY_AUTHENTICATED, // The user has authenticated successfully
        GOOGLE_AUTHENTICATED,   // The user has authenticated via google successfully
        FACEBOOK_AUTHENTICATED, // The user has authenticated via facebook successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val authenticationState = MutableLiveData<AuthenticationState>()
    var username = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var photoURL = MutableLiveData<String>()

    init {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
        username.value = ""
        email.value = ""
        photoURL.value = ""
    }

    fun isUserSignedIn(): Boolean {
//        Log.println(Log.DEBUG, "PRINT", authenticationState.value.toString() + " from is user signed in!")
        return authenticationState.value.toString().contains("_AUTHENTICATED")
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    // MANUAL AUTHENTICATION //
    fun authenticate(username: String, password: String) {
        if (passwordIsValidForUsername(username, password)) {
            this.username.value = username
            authenticationState.value = AuthenticationState.MANUALLY_AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }


    fun manuallySignOut() {
        TODO("not implemented")
    }

    private fun passwordIsValidForUsername(username: String, password: String): Boolean {
        return true
    }
    // MANUAL AUTHENTICATION //


    /// GOOGLE AUTHENTICATION ///
    internal lateinit var mGoogleSignInClient: GoogleSignInClient
    var currentGoogleUser: GoogleSignInAccount? = null
    internal var RC_GOOGLE_SIGN_IN = 7 // Google sign in request code

    fun setupGoogleSignIn(context: Context) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun googleSignIn(activity: Activity) {
        Log.println(Log.INFO, "Action", "Started process: google sign in")
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    fun googleSignInSuccessfull(googleUser: GoogleSignInAccount) {
        currentGoogleUser = googleUser
        fillDataFromGoogleAccount()
        authenticationState.value = AuthenticationState.GOOGLE_AUTHENTICATED
    }

    fun googleSignOut(activity: Activity) {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(activity) {
                currentGoogleUser = null
            }
    }

    fun fillDataFromGoogleAccount() {
        username.value = currentGoogleUser?.displayName.toString()
        email.value = currentGoogleUser?.email.toString()
        photoURL.value = currentGoogleUser?.photoUrl.toString()
    }
    // GOOGLE AUTHENTICATION //

    // FACEBOOK AUTHENTICATION //
    internal val facebookCallbackManager = CallbackManager.Factory.create()

    fun setupFacebookSignIn(button: LoginButton) {
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null)
            fillDataFromFacebookAccount(accessToken)
        else {
            Log.println(Log.DEBUG, "PRINT", "in the setup")
            button.setPermissions("public_profile", "user_status", "email")
            button.registerCallback(facebookCallbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) { // App code
                    Log.println(Log.INFO, "FACEBOOK-AUTH", "Facebook auth successful!")
                    loginResult?.accessToken?.let { fillDataFromFacebookAccount(it) }
                }

                override fun onCancel() { // App code
                    Log.println(Log.INFO, "FACEBOOK-AUTH", "Facebook auth canceled!")
                }

                override fun onError(exception: FacebookException) { // App code
                    Log.println(
                        Log.ERROR,
                        "FACEBOOK-AUTH",
                        "Error from facebook auth: " + exception.stackTrace.toString() + " " + exception.localizedMessage
                    )
                }
            })
        }
    }

    private fun fillDataFromFacebookAccount(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            try {
                val name = `object`.getString("name")
                val email = `object`.getString("email")
                val image =
                    `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                Log.println(Log.DEBUG, "PRINT", "$name with $email and imageURL: $image")
                this.username.value = name
                this.email.value    = email
                this.photoURL.value = image
                authenticationState.value = AuthenticationState.FACEBOOK_AUTHENTICATED
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture.width(600).height(600)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun facebookSignOut() {
        LoginManager.getInstance().logOut()
    }

    // FACEBOOK AUTHENTICATION //

    fun signOut(activity: Activity) {
        when (authenticationState.value) {
            AuthenticationState.MANUALLY_AUTHENTICATED -> manuallySignOut()
            AuthenticationState.GOOGLE_AUTHENTICATED -> googleSignOut(activity)
            AuthenticationState.FACEBOOK_AUTHENTICATED -> facebookSignOut()
        }
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }
}
