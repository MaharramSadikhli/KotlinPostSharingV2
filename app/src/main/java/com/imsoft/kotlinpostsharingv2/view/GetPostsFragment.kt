package com.imsoft.kotlinpostsharingv2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentGetPostsBinding


@Suppress("DEPRECATION")
class GetPostsFragment : Fragment() {

    private lateinit var binding: FragmentGetPostsBinding

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

            val actionLogOut = GetPostsFragmentDirections.actionGetPostsFragmentToSignInFragment()
            Navigation.findNavController(binding.root).navigate(actionLogOut)

        }

        return super.onOptionsItemSelected(item)
    }


}