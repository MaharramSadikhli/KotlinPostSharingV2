package com.imsoft.kotlinpostsharingv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imsoft.kotlinpostsharingv2.databinding.RecyclerRowPostsBinding
import com.imsoft.kotlinpostsharingv2.model.Posts
import com.squareup.picasso.Picasso

class PostAdapter(val postList: ArrayList<Posts>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: RecyclerRowPostsBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRowPostsBinding.inflate(layoutInflater, parent, false)

        return PostViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.userNameGetPosts.text = postList[position].userName
        holder.binding.commentTextGetPosts.text = postList[position].comment
        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.imageViewGetPost)
    }


    override fun getItemCount(): Int {
        return postList.size
    }
}