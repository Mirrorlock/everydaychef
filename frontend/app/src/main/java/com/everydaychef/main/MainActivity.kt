package com.everydaychef.main

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
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
import com.everydaychef.main.helpers.firebase_messaging.ChannelsUtility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), View.OnClickListener, ViewModelStoreOwner {

    @Inject lateinit var authViewModel: AuthViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var popupUtility: PopupUtility
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popupUtility = PopupUtility(this)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        (application as EverydayChefApplication).appComponent.inject(this)

        // DRAWER INITIALISATION //
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_fridge,
                R.id.nav_cook,
                R.id.nav_shopping_list,
                R.id.nav_send,
                R.id.nav_profile
            ), drawer_layout
        )




        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        // DRAWER INITIALISATION //
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        authenticateUser()
        user_image.setOnClickListener(this)
        return true
    }

    private fun authenticateUser(){
        authViewModel.currentUserLd.observe(this, Observer{
            if (!authViewModel.isUserSignedIn()) {
                goToLogin()
            } else {
                popupUtility.displayLongDefault("Logged in as: " + it.username)
                updateHeaderUI()
            }
        })
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.user_image -> {
                navController.navigate(R.id.nav_profile)
                drawer_layout.closeDrawer( GravityCompat.START)
            }
            R.id.notification_button -> {
                navController.navigate(R.id.action_global_to_notifications_fragment)
                drawer_layout.closeDrawer( GravityCompat.START)
            }
        }
    }

    fun signOut(item: MenuItem) {
        authViewModel.signOut(this)
        popupUtility.displayShortDefault("Successfully signed out!")
        goToLogin()
    }

    private fun updateHeaderUI(){
        Log.println(Log.DEBUG, "PRINT", "Updating the header UI!" + authViewModel.isUserSignedIn())
        user_name.visibility = View.VISIBLE
        user_email.text = authViewModel.currentUserLd.value!!.email
        user_name.text = authViewModel.currentUserLd.value!!.username
        val photoUrl = authViewModel.currentUserLd.value!!.photoUrl
        if(photoUrl != ""){
            Glide.with(this).load(photoUrl).into(user_image)
        }else {
            user_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person))
        }
    }

}





