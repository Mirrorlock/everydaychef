package com.everydaychef.main.ui.notifications

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.everydaychef.R
import com.everydaychef.main.models.Family
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationsDataAdapter constructor(
    context: Context,
    resource: Int,
    private val objects: MutableList<Family>,
    private val notificationsViewModel: NotificationsViewModel
): ArrayAdapter<Family>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.notification_row, parent, false)
        val notificationText = rowView.notification_text
        val notificationAcceptBtn = rowView.button_accept
        val notificationRejectBtn = rowView.button_reject
        val text = "Invitation from family: ${objects[position].name}"
        notificationText.text = text
        notificationAcceptBtn.setOnClickListener{
            notificationsViewModel.acceptInvitaion(objects[position])
        }
        notificationRejectBtn.setOnClickListener{
            notificationsViewModel.rejectInvitation(objects[position])
        }
        return rowView
    }
}