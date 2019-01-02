package com.mviana.crudapp.ui.main


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mviana.crudapp.R
import com.mviana.crudapp.users.User
import com.mviana.crudapp.users.UserManager
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserAdapter(val items : ArrayList<User>, val context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0?.userName.text = items[p1].name
        p0?.userBday.text = items[p1].birthDate.toString()
        p0?.userView = items[p1]
        //p0?.userImage.setBackgroundColor(context.getColor(R.color.))
        p0?.userImage.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher_round))

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_list_item, p0, false))

    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        lateinit var userView: User
        val userCard = view.user_card
        val userName = view.user_name
        val userBday = view.user_bday
        val userImage = view.user_photo

        init {
            view.setOnClickListener {
                Toast.makeText(context,
                    context.resources.getString(R.string.selected) + ": " + userName.text.toString(), Toast.LENGTH_LONG).show()
                if (context is UserManager){
                    var listener = context
                    listener.onUserSelected(userView)
                }

            }
        }

    }


}

