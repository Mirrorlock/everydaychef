package com.everydaychef

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), View.OnClickListener, ViewModelStoreOwner {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.setupGoogleSignIn(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_fridge, R.id.nav_cook,
                R.id.nav_shopping_list, R.id.nav_share, R.id.nav_send, R.id.nav_profile
            ), drawerLayout
        )

        loginViewModel.test.postValue(3)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        userImage = findViewById(R.id.user_image)
        userImage.setOnClickListener(this)
        userName  = findViewById(R.id.user_name)
        userEmail = findViewById(R.id.user_email)
        updateUI()

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateUI(){
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        val regularSignInButton = findViewById<Button>(R.id.regular_sign_in_button)
        val registerButton = findViewById<Button>(R.id.register_button)
        if(!loginViewModel.isUserSignedIn()){
            signInButton.visibility = View.VISIBLE
            regularSignInButton.visibility = View.VISIBLE
            registerButton.visibility = View.VISIBLE
            userName.visibility = View.GONE
            userEmail.text = getString(R.string.nav_header_login_first)
            signInButton.setOnClickListener(this)
            userImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person))
        } else {
            signInButton.visibility = View.GONE
            regularSignInButton.visibility = View.GONE
            registerButton.visibility = View.GONE
            userName.visibility = View.VISIBLE
            userEmail.text = loginViewModel.email
            userName.text = loginViewModel.username
            if(loginViewModel.photoURL != "") {
                Glide.with(this).load(loginViewModel.photoURL).into(userImage)
            }
        }
    }

    fun signOut(Item: MenuItem) {
        loginViewModel.signOut(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.sign_in_button -> loginViewModel.googleSignIn(this)
            R.id.user_image -> {
                if(loginViewModel.currentGoogleUser === null){
                    loginViewModel.googleSignIn(this)
                } else {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_shopping_list)
                }
            }
        }
    }

    fun signIn(view: View) {
        loginViewModel.googleSignIn(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === loginViewModel.GOOGLE_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            loginViewModel.googleSignInSuccessfull(completedTask!!.getResult(ApiException::class.java)!!)
//            loginViewModel.authenticationState.s(LoginViewModel.AuthenticationState.GOOGLE_AUTHENTICATED)
            updateUI()
        } catch (e: ApiException) {
            Log.println(Log.ERROR, "GOOGLE-SIGN-IN", e.printStackTrace().toString())
        }
    }

}

