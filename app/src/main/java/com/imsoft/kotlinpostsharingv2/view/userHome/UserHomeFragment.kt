package com.imsoft.kotlinpostsharingv2.view.userHome

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
import com.imsoft.kotlinpostsharingv2.databinding.FragmentUserHomeBinding
import com.imsoft.kotlinpostsharingv2.view.GetPostsFragmentDirections

@Suppress("DEPRECATION")
class UserHomeFragment : Fragment() {

    private lateinit var binding: FragmentUserHomeBinding

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
            val actionLogOut = UserHomeFragmentDirections.actionUserHomeFragmentToSignInFragment()
            Navigation.findNavController(binding.root).navigate(actionLogOut)

        }

        return super.onOptionsItemSelected(item)
    }



}