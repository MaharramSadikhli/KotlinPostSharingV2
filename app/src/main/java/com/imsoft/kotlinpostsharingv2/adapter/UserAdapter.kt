package com.imsoft.kotlinpostsharingv2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.imsoft.kotlinpostsharingv2.databinding.RecyclerRowUserhomeBinding
import com.imsoft.kotlinpostsharingv2.model.Posts
import com.squareup.picasso.Picasso

class UserAdapter(val postList: ArrayList<Posts>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var auth: FirebaseAuth = Firebase.auth
    private var firestore: FirebaseFirestore = Firebase.firestore
    private var storage: FirebaseStorage = Firebase.storage

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



        holder.binding.deleteBtnUserHome.setOnClickListener {
            // Remove the item from the list
            val deletedPost = postList.removeAt(position)

            // Notify the adapter about the item removal
            notifyItemRemoved(position)

            // Delete the corresponding document from Firestore using userName
            deletePost(deletedPost.downloadUrl)
        }

    }


    override fun getItemCount(): Int {
        return postList.size
    }


    private fun deletePost(url: String) {
        val query = firestore.collection("Posts").whereEqualTo("downloadUrl", url)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Delete each document with the specified userName
                    firestore.collection("Posts").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("UserAdapter", "Delete document")

                        }
                        .addOnFailureListener { e ->
                            Log.e("UserAdapter", "Error deleting document", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure if needed
                Log.e("UserAdapter", "Error querying documents", e)
            }
    }
}

//val post = postList[position]
//

//val currentUser = auth.currentUser!!.email
//val atIndex = currentUser!!.indexOf('@')
//
//val user = currentUser.substring(0, atIndex)

//val documentRef = firestore.collection("Posts").document()
//documentRef.delete()
//.addOnSuccessListener {
//    Log.d("Firestore", "Post deleted")
//    println("Post deleted")
//    postList.removeAt(position)
//    notifyItemRemoved(position)
//    notifyItemRangeChanged(position, postList.size)
//}
//.addOnFailureListener { e ->
//    Log.e("Firestore", "Error deleting post", e)
//    println(e.localizedMessage)
//}