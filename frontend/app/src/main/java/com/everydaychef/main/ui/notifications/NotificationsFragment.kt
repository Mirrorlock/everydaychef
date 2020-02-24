package com.everydaychef.main.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.PopupUtility
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.notification_row.view.*
import javax.inject.Inject

class NotificationsFragment: Fragment() {

    @Inject lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var popupUtility: PopupUtility



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        popupUtility = PopupUtility(context)
        (activity?.application as EverydayChefApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationsViewModel.getUserInvitations()
        notificationsViewModel.invitations.observe(this, Observer{
            Log.println(Log.DEBUG, "PRINT", "Invitaions changed")
            if(context != null){
                if(notifications_lv.adapter == null){
                    notifications_lv.adapter = NotificationsDataAdapter(context!!, -1,  it,
                        notificationsViewModel)
                    fragment_info_text.visibility = View.GONE
                }else{
                    (notifications_lv.adapter as NotificationsDataAdapter).notifyDataSetChanged()
                }
            }
        })
    }


}