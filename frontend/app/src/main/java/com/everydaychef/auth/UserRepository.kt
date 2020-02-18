package com.everydaychef.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor (/*private val userService: UserService*/){
    var currentUser = MutableLiveData<CurrentUser>()
        private set

    var googleSignInClient: GoogleSignInClient? = null

    val userSignedIn: Boolean
        get() = currentUser.value != null

    var signInMethod: AuthenticationMethod = AuthenticationMethod.UNAUTHENTICATED
//        set(value) = setUserSignedInMethod(value.toString())

    init{
        currentUser.value = null
    }

    enum class AuthenticationMethod {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        MANUALLY_AUTHENTICATED, // The user has authenticated successfully
        GOOGLE_AUTHENTICATED,   // The user has authenticated via google successfully
        FACEBOOK_AUTHENTICATED, // The user has authenticated via facebook successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }


    fun setUserSignedInMethod(method: String){
        if(method == "facebook"){
            signInMethod = AuthenticationMethod.FACEBOOK_AUTHENTICATED
        }else if(method == "google"){
            signInMethod = AuthenticationMethod.GOOGLE_AUTHENTICATED
        }else if(method == "invalid"){
            signInMethod = AuthenticationMethod.INVALID_AUTHENTICATION
        } else {
            signInMethod = AuthenticationMethod.MANUALLY_AUTHENTICATED
        }
    }

    fun setCurrentUser(username: String){
        currentUser.value?.username = username //temp
//        userService.getUserByUsername(username)
    }

    fun setCurrentUser(username: String, email: String, photoUrl: String, accessToken: String){
//        userService.getUserByUsername(username)
        currentUser.value = CurrentUser()
        currentUser.value?.username = username
        currentUser.value?.email = email
        currentUser.value?.photoUrl = photoUrl
        currentUser.value?.token = accessToken
    }

    fun setCurrentUser(currentGoogleUser: GoogleSignInAccount?){
        currentUser.value = CurrentUser()
        Log.println(Log.DEBUG, "PRINT", "Setting google user!")
        currentUser.value?.username = currentGoogleUser?.displayName.toString()
        currentUser.value?.email = currentGoogleUser?.email.toString()
        currentUser.value?.photoUrl = currentGoogleUser?.photoUrl.toString()
        currentUser.value?.token = currentGoogleUser?.idToken.toString()
        Log.println(Log.DEBUG, "PRINT", currentUser.value.toString())

//        userService.getUserByUsername(username)
    }

    fun passwordIsValidForUsername(username: String, password: String): Boolean {
//        userService.authenticate()
        return true //temp
    }

     fun setGoogleClient(googleSignInClient: GoogleSignInClient?) {
        this.googleSignInClient = googleSignInClient
    }


    fun signOut() {
        when(signInMethod){
            AuthenticationMethod.MANUALLY_AUTHENTICATED -> manuallySignOut()
            AuthenticationMethod.GOOGLE_AUTHENTICATED -> googleSignOut()
            AuthenticationMethod.FACEBOOK_AUTHENTICATED -> facebookSignOut()
        }
        currentUser.value = null
        signInMethod = AuthenticationMethod.UNAUTHENTICATED

    }

    private fun manuallySignOut() {
        TODO("not implemented")
    }

    fun googleSignOut() {
        googleSignInClient?.signOut()
    }

    private fun facebookSignOut() {
        LoginManager.getInstance().logOut()
    }

}