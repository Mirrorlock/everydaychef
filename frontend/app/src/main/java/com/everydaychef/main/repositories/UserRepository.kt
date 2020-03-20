package com.everydaychef.main.repositories

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.everydaychef.auth.AuthenticationState
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.Family
import com.everydaychef.main.services.UserService
import com.everydaychef.main.models.User
import com.everydaychef.main.models.helper_models.JwtResponse
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.register.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor (private val userService: UserService){

    companion object{
//        val authSharedPref = "AuthSharedPreferences"
        var token = ""
        var method = ""
    }

    var currentUserLd = MutableLiveData<CurrentUser>()
        private set

    var googleSignInClient: GoogleSignInClient? = null

    val userSignedIn: Boolean
        get() = currentUserLd.value != null

    var authenticationState = MutableLiveData<AuthenticationState>()

    var errorMessage: String = ""

    init{
        currentUserLd.value = null
        authenticationState.value =
            AuthenticationState.UNAUTHENTICATED
    }

    fun setUserAuthenticationState(method: String){
        when (method) {
            "facebook" -> authenticationState.value =
                AuthenticationState.FACEBOOK_AUTHENTICATED
            "google" -> authenticationState.value =
                AuthenticationState.GOOGLE_AUTHENTICATED
            "manual" -> authenticationState.value =
                AuthenticationState.MANUALLY_AUTHENTICATED
            "invalid" -> authenticationState.value =
                AuthenticationState.INVALID_AUTHENTICATION
            else -> authenticationState.value =
                AuthenticationState.MANUALLY_AUTHENTICATED
        }
    }

//    fun setCurrentUser(username: String, accessToken: String){
//        userService
//        currentUserLd.value?.username = username //temp
//    }


    fun storeCurrentUser( authToken: String, authMethod: String) {
        token = authToken
        method = authMethod
//        var  editor : SharedPreferences.Editor = context.getSharedPreferences(authSharedPref,
//            Context.MODE_PRIVATE).edit()
//        editor.putString("token", token)
//        editor.putString("method", method)
//        editor.apply()
    }


    fun authenticateUser(username: String, password: String) : Call<JwtResponse>{
        var body = hashMapOf("username" to username, "password" to password)
        return userService.authenticate(body)
    }

    fun setCurrentUser(username: String, email: String = "",
                       photoUrl: String = "", method: String = "manual"){

        userService.getUserByUsername(username).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.println(Log.ERROR, "Process Authenticate", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                val body = response?.body()
                when {
                    response?.code() == 200 -> {
                            Log.println(Log.DEBUG, "PRINT", "found user: " + body)
                            fillUserFromBody(body!!, photoUrl)
                            setUserAuthenticationState(method)
                    }
                    response?.code() == 404 -> {
                        if (method != "manual") {
                            registerWithMethod(username, email, method, photoUrl)
                        } else {
                            setUserAuthenticationState("invalid")
                        }
                    }
                    else -> {
                        setUserAuthenticationState("invalid")
                    }
                }
                Log.println(Log.DEBUG, "PRINT", "We got response: " + response?.body())
            }

        })
    }

    private fun fillUserFromBody(body: User, photoUrl: String){
        currentUserLd.value = CurrentUser()
        currentUserLd.value!!.user = body
        currentUserLd.value!!.id = body.id
        currentUserLd.value!!.username = body.name
        currentUserLd.value!!.email = body.email
        currentUserLd.value!!.photoUrl = photoUrl
    }

     fun setGoogleClient(googleSignInClient: GoogleSignInClient?) {
        this.googleSignInClient = googleSignInClient
    }

    fun signOut(activity: Activity) {
        when(authenticationState.value){
            AuthenticationState.MANUALLY_AUTHENTICATED -> manuallySignOut()
            AuthenticationState.GOOGLE_AUTHENTICATED -> googleSignOut(activity)
            AuthenticationState.FACEBOOK_AUTHENTICATED -> facebookSignOut()
        }
        token=""
        method=""
        currentUserLd.value = null
        authenticationState.value =
            AuthenticationState.UNAUTHENTICATED

    }

    private fun manuallySignOut() {
        //logout from server
    }

    fun googleSignOut(activity: Activity) {
        Log.println(Log.DEBUG, "PRINT", "Removing google account!")
        googleSignInClient?.signOut()
        googleSignInClient?.revokeAccess()?.addOnCompleteListener(activity, object : OnCompleteListener<Void> {
            override fun onComplete(p0: Task<Void>) {
                Log.println(Log.DEBUG, "PRINT", "Google is out!")
            }

        })

    }


    private fun facebookSignOut() {
        LoginManager.getInstance().logOut()
    }

    fun register(username: String, password: String, email: String) {
        val requestBody: HashMap<String, Any> = HashMap<String, Any>()
        requestBody.putAll(mapOf("username" to username,
            "email" to email, "password" to password))
        userService.create(requestBody).enqueue(object: Callback<Any>{
            override fun onFailure(call: Call<Any>?, t: Throwable?) {
                Log.println(Log.ERROR, "Process Register", t.toString())
            }

            override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                if(response?.code() == 200){
                    val user: User = response.body() as User
                    fillUserFromBody(user, "")
//                    currentUserLd.value = response.body() as CurrentUser?
                    setUserAuthenticationState("manual")
                }else if(response?.code() == 500) {
                    errorMessage = "Server not responding..."
                    setUserAuthenticationState("invalid")
                }else{
                    errorMessage = (response?.body() as String)
                    setUserAuthenticationState("invalid")
                }
            }

        })
    }

    fun registerWithMethod(username: String, email: String, method: String,
                           photoUrl: String) {
//        userService.create(username, email=email,authenticationMethod = method.first())
        val requestBody: HashMap<String, Any> = HashMap<String, Any>()
            requestBody.putAll(mapOf("username" to username,
            "email" to email, "authentication_method" to method))

        userService.create(requestBody)
            .enqueue(object: Callback<Any>{
            override fun onFailure(call: Call<Any>?, t: Throwable?) {
                Log.println(Log.ERROR, "Process Register", t.toString())
            }

            override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                if(response?.code() == 200){
                    Log.println(Log.DEBUG, "PRINT", "Created user:" + response.body().toString())
                    val user: User = response.body() as User
                    fillUserFromBody(user,  photoUrl)
                    setUserAuthenticationState(method)
                }else if(response?.code() == 500) {
                    errorMessage = "Server not responding..."
                    setUserAuthenticationState("invalid")
                }else if(response?.code() == 400){
                    errorMessage = (response.body() as String)
                    setUserAuthenticationState("invalid")
                }else{
                    setUserAuthenticationState("invalid")
                }
            }

        })
    }

    fun leaveFamily(/*currentUserLd: MutableLiveData<CurrentUser>,*/ message: MutableLiveData<String>){
        currentUserLd.value?.let {
            userService.leaveFamily(it.id).enqueue(object : Callback<Family> {
                override fun onFailure(call: Call<Family>?, t: Throwable?) {
                    Log.println(Log.DEBUG, "PRINT", t.toString())
                }

                override fun onResponse(call: Call<Family>?, response: Response<Family>?) {
                    if(response != null){
                        if(response.isSuccessful){
                            val newCurrentUser = currentUserLd.value!!
                            newCurrentUser.user.family = response.body()
                            currentUserLd.value = newCurrentUser
                            Log.println(Log.DEBUG, "PRINT", "Response from leaveFamily(): "
                                    + response.body())
                        }else{
                                errorMessage = "There was an error"
                            }
                        }else{
                        errorMessage = "There was an error"
                    }
                }

            })
        }
    }

    fun inviteUserToFamily(userId: Int, familyId: Int, message: MutableLiveData<String>) {
        Log.println(Log.DEBUG, "PRINT", "Inviting user with id $userId to family with id $familyId")
        userService.inviteUserToFamily(userId, familyId, HashMap()).enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error from inviteUser(): " + t.toString())
                Log.println(Log.ERROR, "process_invite_user", "Error from inviteUser(): " + t.toString())
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        message.value = "User invited successfully!"
                    } else {
                        message.value = "Error!"
                    }
                } else {
                    message.value = "There was an error!"
                }
            }

        })
    }

    fun getInvitaions(invitations: MutableLiveData<ArrayList<Family>>, message: MutableLiveData<String>) {
        userService.getInvitations(currentUserLd.value!!.id).enqueue(object: Callback<ArrayList<Family>>{
            override fun onFailure(call: Call<ArrayList<Family>>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error from getInvitations(): " + t.toString())
                Log.println(Log.ERROR, "process_invite_user", "Error from getInvitations(): " + t.toString())
            }

            override fun onResponse(call: Call<ArrayList<Family>>?, response: Response<ArrayList<Family>>?) {
                if (response != null) {
                    Log.println(Log.DEBUG, "PRINT", "Response received: " + response!!.body() )
                    if(response.isSuccessful){
                        invitations.value = response.body()
                    }else{
                        message.value = "Error in collecting notifications!"
                    }
                }else{
                    message.value = "There was an Error!"
                }
            }
        })
    }

    fun answerInvitation(userId: Int, familyId: Int, isAccepted: Boolean,
                         message: MutableLiveData<String>) {
        userService.answerInvitation(userId, familyId, hashMapOf("isAccepted" to isAccepted))
            .enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.println(Log.DEBUG, "PRINT", "Error from getInvitations(): " + t.toString())
                Log.println(
                    Log.ERROR,
                    "process_invite_user",
                    "Error from getInvitations(): " + t.toString()
                )
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response != null) {
                    Log.println(Log.DEBUG, "PRINT", "Response received: " + response!!.body())
                    if (response.isSuccessful) {
                        var newCurrentUser = currentUserLd.value
                        newCurrentUser!!.user = response.body()
                        currentUserLd.value = newCurrentUser

                        message.value = "Successfully accepted invitation to join " +
                                response.body().family.name
                    } else {
                        message.value = "Error in collecting notifications!"
                    }
                } else {
                    message.value = "There was an Error!"
                }
            }
        })
    }
}

//
//
//    fun setCurrentUserWithMethod(username: String, accessToken: String, photoUrl: String){
//
//    }
////
//    fun setCurrentUser(currentGoogleUser: GoogleSignInAccount?){
//        userService.getUsers().enqueue(object : Callback<List<User>> {
//            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
//                Log.println(Log.DEBUG, "PRINT", "There was an error with retrieving users: " + t.toString())
//            }
//
//            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
//                Log.println(Log.DEBUG, "PRINT", "We got response: " + response?.body())
//            }
//
//        })
//        currentUserLd.value = CurrentUser()
//        Log.println(Log.DEBUG, "PRINT", "Setting google user!")
//        currentUserLd.value?.username = currentGoogleUser?.displayName.toString()
//        currentUserLd.value?.email = currentGoogleUser?.email.toString()
//        currentUserLd.value?.photoUrl = currentGoogleUser?.photoUrl.toString()
//        currentUserLd.value?.token = currentGoogleUser?.idToken.toString()
//        Log.println(Log.DEBUG, "PRINT", currentUserLd.value.toString())
//
////        userService.getUserByUsername(username)
//    }

//    fun passwordIsValidForUsername(username: String, password: String): String {
//        var response = userService.authenticate(username, password)/*.execute()*/
//        .enqueue(object : Callback<String> {
//            override fun onFailure(call: Call<String>?, t: Throwable?) {
//                Log.println(Log.ERROR, "Process Authenticate", t.toString() )
//            }
//
//            override fun onResponse(call: Call<String>?, response: Response<String>?) {
//                if(response?.code() == 200){
//
//                }else{
//
//                }
//            }
//        })
//
//        Log.println(Log.DEBUG, "PRINT", "WE ARE HERE!")
//        var resultingToken: String
//        if(response.code() == 200){
//            resultingToken = response.body()
//        }else{
//            resultingToken = ""
//        }
//        return resultingToken
//    }
