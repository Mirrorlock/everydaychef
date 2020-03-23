package com.everydaychef.main.helpers.firebase_messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.MainActivity
import com.everydaychef.main.helpers.SharedPreferencesUtility
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class MessageReceiver: FirebaseMessagingService() {
    private val REQUEST_CODE = 1
    private val NOTIFICATION_ID = 6578

    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var sharedPreferencesUtility: SharedPreferencesUtility

    override fun onCreate() {
        super.onCreate()
        (application as EverydayChefApplication).appComponent.inject(this)
    }

    companion object{
        const val FIREBASE_SHARED_PREFERENCE = "FirebaseSharedPreferences"
        const val TOKEN_KEY = "firebase_token"
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        val oldToken = getOldToken()
        if(oldToken.isNotEmpty()){
            storeOnServer(newToken)
        }
        storeOnPhone(newToken)
        Log.d("PRINT", "Token has been created1: $newToken")
    }

    private fun getOldToken(): String{
        return sharedPreferencesUtility.getPreference(FIREBASE_SHARED_PREFERENCE, TOKEN_KEY)
    }

    private fun storeOnPhone(newToken: String) {
        sharedPreferencesUtility.setPreference(FIREBASE_SHARED_PREFERENCE, TOKEN_KEY, newToken)
    }

    private fun storeOnServer(newToken: String) {
        // change the values of all the users that have this device registered
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var title: String? = remoteMessage.notification?.title
        var message:String? = remoteMessage.notification?.body
        if(title != null && message != null)
            showNotifications(title, message)
    }

    private fun showNotifications(title: String, msg: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
            intent, 0)

        val builder = NotificationCompat.Builder(this, ChannelsUtility.CHANNEL_IMPORTANT_ID)
            .setSmallIcon(R.mipmap.ic_notification)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
    }
}