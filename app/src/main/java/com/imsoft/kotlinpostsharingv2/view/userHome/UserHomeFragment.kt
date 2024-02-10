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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentUserHomeBinding
import com.imsoft.kotlinpostsharingv2.view.GetPostsFragmentDirections

@Suppress("DEPRECATION")
class UserHomeFragment : Fragment() {

    private lateinit var binding: FragmentUserHomeBinding
    private lateinit var auth: FirebaseAuth

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



}