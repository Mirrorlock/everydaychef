package com.everydaychef.ui.profile

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.everydaychef.models.User


class FamilyMembersAdapter(context:
                           Context, books: Array<User>) : BaseAdapter() {
    private val mContext: Context = context
    private val books: Array<User> = books
    // 2
    override fun getCount(): Int {
        return books.size
    }

    // 3
    override fun getItemId(position: Int): Long {
        return 0
    }

    // 4
    override fun getItem(position: Int): Any? {
        return null
    }

    // 5
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val dummyTextView = TextView(mContext)
        dummyTextView.text = position.toString()
        return dummyTextView
    }

}