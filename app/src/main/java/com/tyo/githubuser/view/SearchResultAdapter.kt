package com.tyo.githubuser.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tyo.githubuser.R
import com.tyo.githubuser.repository.User

class SearchResultAdapter: RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    var users: List<User> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.userNameText.text = user.name

        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.avatarImage)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var avatarImage: ImageView
        var userNameText: TextView

        init {
            avatarImage = view.findViewById(R.id.image_user_avatar)
            userNameText = view.findViewById(R.id.text_user_name)
        }
    }
}
