package com.everydaychef.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.PopupUtility
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {
    @Inject
    lateinit var authViewModel: AuthViewModel
    private val popupUtility: PopupUtility = PopupUtility(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as EverydayChefApplication).appComponent.inject(this)
        setContentView(R.layout.register)
        authViewModel.authenticationState.observe(this, androidx.lifecycle.Observer{
            when(it){
                AuthenticationState.INVALID_AUTHENTICATION ->
                    popupUtility.displayLongDefault(authViewModel.errorMessage)
                AuthenticationState.MANUALLY_AUTHENTICATED -> {
                    returnToLogin()
                }
            }
        })
    }

    fun registerOnClick(view: View) {
        val username = et_reg_username.text.toString()
        val password = et_reg_password.text.toString()
        val email    = et_reg_email.text.toString()
        if(username.length > 3 && password.length > 3 && email.length > 5) {
            authViewModel.register(username, password, email)
        } else {
            authViewModel.refuseAuthentication()
        }
    }

    private fun returnToLogin() {
        authViewModel.currentUserLd.value = CurrentUser()
        authViewModel.authenticationState.value = AuthenticationState.UNAUTHENTICATED
        findNavController(R.id.register_host).popBackStack()
        finish()
    }

}
