package com.tyo.githubuser.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tyo.githubuser.R
import com.tyo.githubuser.repository.User

class SearchUserAdapter: PagingDataAdapter<User, SearchUserAdapter.ViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.userNameText.text = user?.name

        Glide.with(holder.itemView.context)
            .load(user?.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.avatarImage)
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var avatarImage: ImageView
        var userNameText: TextView

        init {
            avatarImage = view.findViewById(R.id.image_user_avatar)
            userNameText = view.findViewById(R.id.text_user_name)
        }
    }
}
