package com.everydaychef.main.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.Family
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val userRepository: UserRepository,
                                           private val familyRepository: FamilyRepository): ViewModel() {
//    val currentUser: MutableLiveData<CurrentUser>
//        get() = userRepository.currentUser
    var currentUserFamily = MutableLiveData<Family>()
    var message = MutableLiveData<String>()
//    var otherCurrentUser = MutableLiveData<CurrentUser>()
////
//    init {
//        otherCurrentUser = userRepository.currentUser
//    }

    fun getCurrentUser(): MutableLiveData<CurrentUser>{
        return userRepository.currentUserLd
    }

    fun createFamily(familyName: String) {
        familyRepository.createFamily(familyName,getCurrentUser(), message)
    }

    fun isUserWithDefaultFamily(): Boolean {
        Log.println(Log.DEBUG, "PRINT", "Check for equality")
        return getCurrentUser().value!!.user.family.name ==
                getCurrentUser().value!!.username.capitalize() + "'s Family"
    }

    fun leaveFamily() {
        userRepository.leaveFamily(message)
    }
}
