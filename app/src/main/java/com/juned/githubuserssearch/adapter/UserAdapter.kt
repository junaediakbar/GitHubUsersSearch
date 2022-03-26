package com.juned.githubuserssearch.adapter


import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juned.githubuserssearch.databinding.ItemUserBinding
import com.juned.githubuserssearch.model.User


class UserAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemCount(): Int = userList.size

    class UserHolder( itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var imgPhoto = itemBinding.imgUser
        var tvUsername = itemBinding.tvUsername
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val userData: User = userList[position]
        holder.apply {
            tvUsername.text = userData.username
            imgPhoto.setOnClickListener { onItemClickCallback?.onItemClicked(userList[holder.adapterPosition]) }
            Glide.with(holder.imgPhoto.context)
                .load(userData.avatarUrl)
                .into(holder.imgPhoto)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}