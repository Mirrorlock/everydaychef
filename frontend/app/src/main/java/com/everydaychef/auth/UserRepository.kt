package com.everydaychef.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import javax.security.auth.callback.Callback

@Singleton
class UserRepository @Inject constructor (private val userService: UserService){
    var currentUser = MutableLiveData<CurrentUser>()
        private set

    var googleSignInClient: GoogleSignInClient? = null

    val userSignedIn: Boolean
        get() = currentUser.value != null

    var authenticationState = MutableLiveData<AuthenticationState>()
//        set(value) = setUserSignedInMethod(value.toString())

    init{
        currentUser.value = null
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun setUserAuthenticationState(method: String){
        when (method) {
            "facebook" -> authenticationState.value = AuthenticationState.FACEBOOK_AUTHENTICATED
            "google" -> authenticationState.value = AuthenticationState.GOOGLE_AUTHENTICATED
            "invalid" -> authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
            else -> authenticationState.value = AuthenticationState.MANUALLY_AUTHENTICATED
        }
    }

    fun setCurrentUser(username: String){
        currentUser.value?.username = username //temp
    }

    fun setCurrentUser(username: String, email: String, photoUrl: String, accessToken: String){
//        userService.getUserByUsername(username)
        currentUser.value = CurrentUser()
        currentUser.value?.username = username
        currentUser.value?.email = email
        currentUser.value?.photoUrl = photoUrl
        currentUser.value?.token = accessToken
        userService.getUsers().enqueue(object : retrofit2.Callback<List<User>>{
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "There was an error with retrieving users!")
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                Log.println(Log.DEBUG, "PRINT", "We got response: " + response?.body())
            }

        })
    }

    fun setCurrentUser(currentGoogleUser: GoogleSignInAccount?){
        userService.getUsers().enqueue(object : retrofit2.Callback<List<User>>{
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "There was an error with retrieving users: " + t.toString())
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                Log.println(Log.DEBUG, "PRINT", "We got response: " + response?.body())
            }

        })
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
        when(authenticationState.value){
            AuthenticationState.MANUALLY_AUTHENTICATED -> manuallySignOut()
            AuthenticationState.GOOGLE_AUTHENTICATED -> googleSignOut()
            AuthenticationState.FACEBOOK_AUTHENTICATED -> facebookSignOut()
        }
        currentUser.value = null
        authenticationState.value = AuthenticationState.UNAUTHENTICATED

    }

    private fun manuallySignOut() {
        //TODO("not implemented")

    }

    fun googleSignOut() {
        googleSignInClient?.signOut()
    }

    private fun facebookSignOut() {
        LoginManager.getInstance().logOut()
    }

}