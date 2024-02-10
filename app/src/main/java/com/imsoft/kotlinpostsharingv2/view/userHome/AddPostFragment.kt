package com.imsoft.kotlinpostsharingv2.view.userHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPostButton.setOnClickListener(addPost)
    }

    private val addPost = View.OnClickListener {
        val actionAddPost = AddPostFragmentDirections.actionAddPostFragmentToGetPostsFragment()
        Navigation.findNavController(it).navigate(actionAddPost)
    }

}