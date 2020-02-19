package com.everydaychef.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.R
import com.facebook.*
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import org.json.JSONException
import javax.inject.Inject
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
//    var  tempRepository: TempRepository
    val currentUser = userRepository.currentUser
    val authenticationState: MutableLiveData<AuthenticationState>
            = userRepository.authenticationState
//
    fun isUserSignedIn(): Boolean {
        return userRepository.userSignedIn
    }

    var i = 5

    // MANUAL AUTHENTICATION //
    fun manuallySignIn(username: String, password: String) {
        if (userRepository.passwordIsValidForUsername(username, password)) {
            userRepository.setCurrentUser(username)
            userRepository.setUserAuthenticationState("manual")
        } else {
            userRepository.setUserAuthenticationState("invalid")
        }
    }
    // MANUAL AUTHENTICATION //


    /// GOOGLE AUTHENTICATION ///
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    var currentGoogleUser: GoogleSignInAccount? = null
    var RC_GOOGLE_SIGN_IN = 7 // Google sign in request code

    fun setupGoogleSignIn(context: Context, listener: SignInButton){
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        userRepository.setGoogleClient(mGoogleSignInClient)
        listener.setOnClickListener{googleSignIn(context as Activity)}
    }

    fun googleSignIn(activity: Activity) {
        Log.println(Log.INFO, "Action", "Started process: google sign in")
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    fun googleSignInSuccessful(googleUser: GoogleSignInAccount) {
        currentGoogleUser = googleUser
        fillDataFromGoogleAccount()
        Log.println(Log.DEBUG, "PRINT", "Id token is: " + googleUser.idToken)
    }

    fun fillDataFromGoogleAccount() {
        Log.println(Log.DEBUG, "PRINT", "We are here!")
        userRepository.setCurrentUser(currentGoogleUser)
        userRepository.setUserAuthenticationState("google")
    }
    // GOOGLE AUTHENTICATION //

    // FACEBOOK AUTHENTICATION //
    val facebookCallbackManager = CallbackManager.Factory.create()
    val RC_FACEBOOK_SIGN_IN = 64206

    fun setupFacebookSignIn(button: LoginButton) {
        /*val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null) {
            Log.println(Log.DEBUG, "PRINT", "Found access token!")
            fillDataFromFacebookAccount(accessToken)
        }
        else {
        */
        LoginManager.getInstance().logOut()
        button.setPermissions("public_profile", "user_status", "email")
        facebookRegisterButtonCallback(button)
//        }
    }

    private fun facebookRegisterButtonCallback(button: LoginButton){
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

    private fun fillDataFromFacebookAccount(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, _ ->
            try {
                val name = `object`.getString("name")
                val email = `object`.getString("email")
                val image =
                    `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                Log.println(Log.DEBUG, "PRINT", "$name with $email and imageURL: $image")
                userRepository.setCurrentUser(name, email, image, accessToken.token)
                userRepository.setUserAuthenticationState("facebook")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture.width(600).height(600)")
        request.parameters = parameters
        request.executeAsync()
    }
    // FACEBOOK AUTHENTICATION //

    fun signOut() {
        userRepository.signOut()
    }

    fun refuseAuthentiation() {
        userRepository.setUserAuthenticationState("invalid")
    }
}

