package com.imsoft.kotlinpostsharingv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imsoft.kotlinpostsharingv2.databinding.RecyclerRowUserhomeBinding
import com.imsoft.kotlinpostsharingv2.model.Posts
import com.squareup.picasso.Picasso

class UserAdapter(val postList: ArrayList<Posts>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: RecyclerRowUserhomeBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRowUserhomeBinding.inflate(layoutInflater, parent, false)

        return UserViewHolder(binding)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.userNameUserHome.text = postList[position].userName
        holder.binding.commentTextUserHome.text = postList[position].comment
        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.imageViewUserHome)
    }


    override fun getItemCount(): Int {
        return postList.size
    }
}