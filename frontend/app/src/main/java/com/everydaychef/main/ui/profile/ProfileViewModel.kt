package com.everydaychef.main.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.User
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val userRepository: UserRepository,
                                           private val familyRepository: FamilyRepository): ViewModel() {
    var familyNonMembers = ArrayList<User>()
    var message = MutableLiveData<String>()

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

    fun getFamilyNonMembers(): Array<String> {
        familyNonMembers = familyRepository.getNonMembers(getCurrentUser().value!!.user.family.id)
        return familyNonMembers.map { user -> user.name }.toTypedArray()
    }

    fun getInvitedArrayIndexes(nonMembersArray: Array<String>): BooleanArray{
        var invitedNonMembers = familyNonMembers.filter { nonMember ->
            nonMember.invitations != null &&
                    nonMember.invitations!!.contains(getCurrentUser().value!!.user.family)
            }
        var resultIndexes = Array(nonMembersArray.size) {i -> false}
        for(nonMember in invitedNonMembers){
            resultIndexes[nonMembersArray.indexOf(nonMember.name)] = true
        }
        return resultIndexes.toBooleanArray()

    }

    fun inviteUsers(checkedUsers: ArrayList<Int>) {
        Log.println(Log.DEBUG, "PRINT", "Inviting users: ${checkedUsers}, to family: " + getCurrentUser().value!!.user.family.toString())
        for (checkedUser in checkedUsers){
            val userId = familyNonMembers[checkedUser].id
            userRepository.inviteUserToFamily(userId, getCurrentUser().value!!.user.family.id, message)
        }
    }
}
