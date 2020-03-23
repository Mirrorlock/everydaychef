package com.everydaychef.main.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.models.Family
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    val currentUserLd: MutableLiveData<CurrentUser>
        get() = userRepository.currentUserLd

    val invitations= MutableLiveData<ArrayList<Family>>()
    val message = MutableLiveData<String>()

    fun getUserInvitations(){
        userRepository.getInvitations(invitations, message)
    }

    fun answerInvitation(inviter: Family, isAccepted: Boolean){
        userRepository.answerInvitation(currentUserLd.value!!.id, inviter.id, isAccepted, message)
        val newInvitaionsList = invitations.value
        newInvitaionsList?.remove(inviter)
        invitations.value = newInvitaionsList
    }

    fun acceptInvitaion(inviter: Family) {
        answerInvitation(inviter, true)
    }

    fun rejectInvitation(inviter: Family) {
        answerInvitation(inviter, false)
    }
}