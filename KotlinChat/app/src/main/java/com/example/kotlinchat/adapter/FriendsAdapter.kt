package com.example.kotlinchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinchat.R
import com.example.kotlinchat.model.User
import de.hdodenhof.circleimageview.CircleImageView

class FriendsAdapter(var context: Context, var friendsList: ArrayList<User>): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    private lateinit var onFriendClickListener: OnFriendClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = friendsList[position].name
        holder.status.text = friendsList[position].status

        if(friendsList[position].profile_image == "no_image") {
            Glide.with(context).load(R.drawable.defaultuser).into(holder.pp)
        } else {
            Glide.with(context).load(friendsList[position].profile_image).into(holder.pp)
        }

        holder.itemView.setOnClickListener {
            onFriendClickListener.onFriendClick(friendsList[position])
        }
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.item_friends_name)
        val status = view.findViewById<TextView>(R.id.item_friends_status)
        val pp = view.findViewById<CircleImageView>(R.id.item_friend_pp)
    }

    fun setOnFriendClickListener(onFriendClickListener: OnFriendClickListener) {
        this.onFriendClickListener = onFriendClickListener
    }

    interface OnFriendClickListener {
        fun onFriendClick(user: User)
    }
}
