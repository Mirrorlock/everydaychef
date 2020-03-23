package com.everydaychef.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.R
import com.everydaychef.main.helpers.CallbackUtility
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.models.helper_models.JwtResponse
import com.everydaychef.main.repositories.UserRepository
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.gson.JsonObject
import org.json.JSONException
import javax.inject.Inject
class AuthViewModel @Inject constructor(private val userRepository: UserRepository,
                                        private val messageUtility: MessageUtility) : ViewModel() {
    val errorMessage: String
        get() = userRepository.errorMessage

    val currentUserLd = userRepository.currentUserLd
    var authenticationState: MutableLiveData<AuthenticationState>
        get() = userRepository.authenticationState
        set(value) {userRepository.authenticationState = value}

    fun isUserSignedIn(): Boolean {
        return userRepository.userSignedIn
    }

    // MANUAL AUTHENTICATION //
    fun manuallySignIn(username: String, password: String) {
        userRepository.authenticateUser(username, password)
            .enqueue(CallbackUtility<JwtResponse>("manuallySignOut",
                errorMessage="Wrong credentials!",
                messageUtility = messageUtility){
//                userRepository.storeCurrentUser(it.jwtToken, "Manual")
                userRepository.setCurrentUser(username = username, token = it.jwtToken)
            })
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

    fun googleSignInSuccessful(googleUser: GoogleSignInAccount, context: Context) {
        currentGoogleUser = googleUser
        fillDataFromGoogleAccount()
    }

    fun fillDataFromGoogleAccount() {
        userRepository.setCurrentUser(
            username    = currentGoogleUser!!.displayName.toString(),
            token       = currentGoogleUser!!.idToken!!,
            email       = currentGoogleUser!!.email.toString(),
            method      = "google",
            photoUrl    = currentGoogleUser!!.photoUrl.toString()
        )
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
//                userRepository.storeCurrentUser(accessToken.token, "Facebook")
                userRepository.setCurrentUser(
                    username = name,
                    token = accessToken.token,
                    email = email,
                    method = "facebook",
                    photoUrl = image
                )
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

    fun signOut(activity: Activity) {
        userRepository.signOut(activity)
    }

    fun refuseAuthentication(errorMessage: String = "Invalid authentication") {
        userRepository.errorMessage = errorMessage
        userRepository.setUserAuthenticationState("invalid")
    }

    fun register(username: String, password: String, email: String) {
        userRepository.register(username, password, email, "manual")
    }

    fun deleteAllUserData(){
//        userRepository.currentUserLd.value = CurrentUser()
        userRepository.storeCurrentUser("", "")
    }
}

