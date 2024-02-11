package com.imsoft.kotlinpostsharingv2.view.userHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentUserHomeBinding
import com.imsoft.kotlinpostsharingv2.model.Posts
import com.imsoft.kotlinpostsharingv2.view.GetPostsFragmentDirections

@Suppress("DEPRECATION")
class UserHomeFragment : Fragment() {

    private lateinit var binding: FragmentUserHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var postList: ArrayList<Posts>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        postList = ArrayList<Posts>()

        getUserPosts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_post_menu_user) {

            val actionAddPost = UserHomeFragmentDirections.actionUserHomeFragmentToAddPostFragment()
            Navigation.findNavController(binding.root).navigate(actionAddPost)

        } else if (item.itemId == R.id.go_to_post_feed) {

            val actionPostMenu = UserHomeFragmentDirections.actionUserHomeFragmentToGetPostsFragment()
            Navigation.findNavController(binding.root).navigate(actionPostMenu)

        } else {

            // log_out_menu_user
            auth.signOut()
            val actionLogOut = UserHomeFragmentDirections.actionUserHomeFragmentToSignInFragment()
            Navigation.findNavController(binding.root).navigate(actionLogOut)

        }

        return super.onOptionsItemSelected(item)
    }


    private fun getUserPosts() {

        val uid = auth.currentUser?.uid

        firestore.collection("Posts")
            .whereEqualTo("userId", uid)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    if (value != null && !(value.isEmpty)) {
                        val documents = value.documents

                        postList.clear()

                        for (document in documents) {
                            val username = document["userName"] as String
                            val comment = document["userComment"] as String
                            val downloadUrl = document["downloadUrl"] as String

                            val post = Posts(username, comment, downloadUrl)

                            println("username from user home $username")
                            postList.add(post)
                        }

//                        postAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                    println(error.localizedMessage)
                }
            }

    }


}