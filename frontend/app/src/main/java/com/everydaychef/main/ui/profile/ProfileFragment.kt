package com.everydaychef.main.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.auth.AuthViewModel
import com.everydaychef.auth.CurrentUser
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.helpers.PopupUtility
import kotlinx.android.synthetic.main.dialog_create_family.*
import kotlinx.android.synthetic.main.dialog_create_family.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*
import javax.inject.Inject

class ProfileFragment : Fragment(), View.OnClickListener {

    @Inject lateinit var profileViewModel: ProfileViewModel
//    @Inject lateinit var messageUtility: MessageUtility

    private var user: CurrentUser? = null
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    private lateinit var popupUtility: PopupUtility

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as EverydayChefApplication).appComponent.inject(this)
        user = profileViewModel.getCurrentUser().value
        alertDialogBuilder = AlertDialog.Builder(activity)
        popupUtility = PopupUtility(activity!!.applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer{
            Log.println(Log.DEBUG, "PRINT", "Changed user detected!")
            it?.let{
                Log.println(Log.DEBUG, "PRINT", "Current user change was detected: " + it.user.toString())
                updateUI()
            }
        })
//        profileViewModel.message.observe(viewLifecycleOwner, Observer{
//            if(it.isNotEmpty()){
//                popupUtility.displayShortDefault(it)
//                profileViewModel.message.value = ""
//            }
//        })
        btn_leave_family.setOnClickListener(this)
        btn_add_members.setOnClickListener(this)
    }

    private fun updateUI(){
        user = profileViewModel.getCurrentUser().value
        user?.let{
            val photoUrl = it.photoUrl
            if(photoUrl != ""){
                Glide.with(this).load(photoUrl).into(profile_pic)
            }else{
                profile_pic.setImageDrawable(ContextCompat
                    .getDrawable(activity!!.applicationContext, R.drawable.ic_person))
            }
            profile_email.text = it.email
            profile_name.text = it.username
            profile_family_name.text = it.user.family.name
            if(!profileViewModel.isUserWithDefaultFamily()) {
                btn_create_family.visibility = View.GONE
            }else {
                btn_create_family.visibility = View.VISIBLE
                btn_create_family.setOnClickListener(this)
            }
        }
    }

    private fun leaveFamily(){
        profileViewModel.leaveFamily()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(p0: View?) {
        when(p0?.id){
            btn_leave_family.id -> {
                if (user != null) {
                    if(profileViewModel.isUserWithDefaultFamily()){
                        popupUtility.displayLongDefault("Can not leave default family!")
                    }else{
                        AlertDialog.Builder(activity)
                            .setTitle("Are you sure?")
                            .setMessage("Do you really want to leave this family? " +
                                "You will not be able to return unless they invite you!")
                            .setPositiveButton("YES") { dialog, which ->
                                popupUtility.displayShortDefault("Leaving family...")
                                leaveFamily()
                            }
                            .setNegativeButton("NO") { dialog, which ->
                                popupUtility.displayShortDefault("Canceling...")
                            }.create().show()
                    }

                }
            }

            btn_create_family.id -> {
                Log.println(Log.DEBUG, "PRINT", "Clicked create family!")
                val dialogViewRoot = LayoutInflater.from(activity)
                    .inflate( R.layout.dialog_create_family, null)
                AlertDialog.Builder(activity)
                    .setView(dialogViewRoot)
                    .setPositiveButton("Create") { dialog, which ->
                    popupUtility.displayShortDefault("Creating new family")
                    profileViewModel.createFamily(dialogViewRoot.new_family_name.text.toString())
                }
                .setNeutralButton("Cancel") { dialog, which ->
                    popupUtility.displayShortDefault("Canceled creating family!")
                }.create().show()
            }

            btn_add_members.id -> {
                Log.println(Log.DEBUG, "PRINT", "Clicked add members button!")
                addMembers()
            }
        }
    }

    private fun addMembers() {
        var nonMembers = profileViewModel.getFamilyNonMembers()
        var previouslyCheckedUsers  = profileViewModel.getInvitedArrayIndexes(nonMembers)
        var checkedUsers = ArrayList<Int>()
        Log.println(Log.DEBUG, "PRINT", "Received non-member users in the fragment are: \n" +
                nonMembers.toString())
        AlertDialog.Builder(activity)
            .setTitle("Invite members to family")
            .setMultiChoiceItems(nonMembers, previouslyCheckedUsers )
                { dialog, which, isChecked ->
                    if(isChecked) checkedUsers.add(which)
                    else checkedUsers.remove(which)
                }.setPositiveButton("Add Members"){dialog, which ->
                    profileViewModel.inviteUsers(checkedUsers)
                }.setNeutralButton("Cancel"){dialog, which -> }
            .create().show()
    }
}
