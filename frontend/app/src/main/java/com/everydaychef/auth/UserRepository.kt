package com.everydaychef.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor (/*private val userService: UserService*/){
    var currentUser = MutableLiveData<CurrentUser>()
        private set

    init{
        currentUser.value = null
    }

    val userSignedIn: Boolean
        get() = currentUser.value != null

    var signInMethod: AuthenticationMethod = AuthenticationMethod.UNAUTHENTICATED
        set(value) = setUserSignedInMethod(value.toString())

    enum class AuthenticationMethod {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        MANUALLY_AUTHENTICATED, // The user has authenticated successfully
        GOOGLE_AUTHENTICATED,   // The user has authenticated via google successfully
        FACEBOOK_AUTHENTICATED, // The user has authenticated via facebook successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }


    fun setUserSignedInMethod(method: String){
        if(method.equals("facebook")){
            signInMethod = AuthenticationMethod.FACEBOOK_AUTHENTICATED
        }else if(method.equals("google")){
            signInMethod = AuthenticationMethod.GOOGLE_AUTHENTICATED
        }else if(method.equals("invalid")){
            signInMethod = AuthenticationMethod.INVALID_AUTHENTICATION
        } else {
            signInMethod = AuthenticationMethod.MANUALLY_AUTHENTICATED
        }
    }

    fun setCurrentUser(username: String){
        currentUser.value?.username = username //temp
//        userService.getUserByUsername(username)
    }

    fun setCurrentUser(username: String, email: String, photoUrl: String){
//        userService.getUserByUsername(username)
        currentUser.value?.username = username
        currentUser.value?.email = username
        currentUser.value?.photoUrl = username
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

}