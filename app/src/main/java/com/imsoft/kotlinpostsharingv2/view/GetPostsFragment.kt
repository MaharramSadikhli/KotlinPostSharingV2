package com.imsoft.kotlinpostsharingv2.view

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.adapter.PostAdapter
import com.imsoft.kotlinpostsharingv2.databinding.FragmentGetPostsBinding
import com.imsoft.kotlinpostsharingv2.model.Posts


@Suppress("DEPRECATION")
class GetPostsFragment : Fragment() {

    private lateinit var binding: FragmentGetPostsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postList: ArrayList<Posts>
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGetPostsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        postList = ArrayList<Posts>()

        getPost()

        binding.recyclerViewGetPost.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostAdapter(postList)
        binding.recyclerViewGetPost.adapter = postAdapter

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_posts, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.go_to_user_home) {

            val actionUserHome = GetPostsFragmentDirections.actionGetPostsFragmentToUserHomeFragment()
            Navigation.findNavController(binding.root).navigate(actionUserHome)

        } else if (item.itemId == R.id.add_post_menu_posts) {

            val actionAddPost = GetPostsFragmentDirections.actionGetPostsFragmentToAddPostFragment()
            Navigation.findNavController(binding.root).navigate(actionAddPost)

        } else {

            auth.signOut()
            val actionLogOut = GetPostsFragmentDirections.actionGetPostsFragmentToSignInFragment()
            Navigation.findNavController(binding.root).navigate(actionLogOut)
        }

        return super.onOptionsItemSelected(item)
    }


    private fun getPost() {

        firestore.collection("Posts").addSnapshotListener { value, error ->

            firestore.collection("Posts")
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


                                postList.add(post)


                            }

                            postAdapter.notifyDataSetChanged()
                        }
                    }
//                    else {
//                        println(error.localizedMessage)
//                        Toast.makeText( requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
//                    }
                }


        }

    }


}