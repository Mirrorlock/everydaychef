package com.everydaychef.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.auth.AuthViewModel
import com.everydaychef.auth.LoginActivity
import com.everydaychef.main.helpers.PopupUtility
import com.everydaychef.main.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), View.OnClickListener, ViewModelStoreOwner {

    @Inject lateinit var authViewModel: AuthViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var popupUtility: PopupUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as EverydayChefApplication).appComponent.inject(this)
        popupUtility = PopupUtility(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SIGN-IN //
        if(!authViewModel.isUserSignedIn()){
            Log.println(Log.DEBUG, "PRINT", "User not signed in, starting activity!")
            startLoginActivity()
        }
        // SIGN-IN //


        // drawer initialisation //
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_fridge,
                R.id.nav_cook,
                R.id.nav_shopping_list,
                R.id.nav_share,
                R.id.nav_send,
                R.id.nav_profile
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        // drawer initialisation //
    }

    private fun startLoginActivity() {

        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        authViewModel.currentUser.observe(this, Observer{
            if(authViewModel.isUserSignedIn()){
                    popupUtility.displayLongTop("Logged in as: " + it.email)
                updateHeaderUI()
            }
        })
        user_image.setOnClickListener(this)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.user_image -> {
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.nav_profile)
            }
        }
    }

    fun signOut(item: MenuItem) {
        authViewModel.signOut()
        popupUtility.displayShortDefault("Successfully signed out!")
        startLoginActivity()
    }

    private fun updateHeaderUI(){
        Log.println(Log.DEBUG, "PRINT", "Updating the header UI!" + authViewModel.isUserSignedIn())
//        if(!authViewModel.isUserSignedIn()){
//            user_name.visibility = View.GONE
//            user_email.text = getString(R.string.nav_header_login_first)
//            user_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person))
//        } else {
        user_name.visibility = View.VISIBLE
        user_email.text = authViewModel.currentUser.value!!.email
        user_name.text = authViewModel.currentUser.value!!.username
        authViewModel.currentUser.value!!.photoUrl.let{
            Glide.with(this).load(it).into(user_image)
        }
        popupUtility.displayShortTop("Logged in as: " + user_name.text)
    }

    fun showSettings(item: MenuItem) {
        intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}





