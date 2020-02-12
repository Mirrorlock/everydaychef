package com.everydaychef

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener, ViewModelStoreOwner {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.setupGoogleSignIn(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_fridge, R.id.nav_cook,
                R.id.nav_shopping_list, R.id.nav_share, R.id.nav_send, R.id.nav_profile
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        user_image.setOnClickListener(this)
        loginViewModel.setupFacebookSignIn(facebook_sign_in_button)
        loginViewModel.authenticationState.observe(this, Observer{
            updateHeaderUI()
        })
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateHeaderUI(){
        Log.println(Log.DEBUG, "PRINT", "Updating the header UI!" + loginViewModel.isUserSignedIn())
        if(!loginViewModel.isUserSignedIn()){
            user_name.visibility = View.GONE
            user_email.text = getString(R.string.nav_header_login_first)
            user_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person))
        } else {
            user_name.visibility = View.VISIBLE
            user_email.text = loginViewModel.email.value
            user_name.text = loginViewModel.username.value
            loginViewModel.photoURL.value?.let{
                Glide.with(this).load(it).into(user_image)
            }
        }
    }

    fun signOut(item: MenuItem) {
        loginViewModel.signOut(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.user_image -> {
                val navController = findNavController(R.id.nav_host_fragment)
                if(!loginViewModel.isUserSignedIn()){
                    navController.navigate(R.id.nav_home)
                } else {
                    navController.navigate(R.id.nav_profile)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == loginViewModel.RC_GOOGLE_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }else{
            loginViewModel.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            loginViewModel.googleSignInSuccessful(completedTask!!.getResult(ApiException::class.java)!!)
        } catch (e: ApiException) {
            Log.println(Log.ERROR, "GOOGLE-SIGN-IN", e.printStackTrace().toString())
        }
    }
}

