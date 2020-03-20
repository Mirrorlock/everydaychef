package com.everydaychef.main.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsUtility @Inject constructor(private val messageUtility: MessageUtility){
    val RC_STORAGE_PERMISSION = 10

    fun checkForStoragePermission(activity: Activity): Boolean{
        return if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                messageUtility.setMessage("Permission denied!")
            } else {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    RC_STORAGE_PERMISSION)
            }
            false
        } else {
            true
        }
    }
}