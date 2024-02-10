package com.imsoft.kotlinpostsharingv2.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener(signIn)
        binding.signUpButtonAtSignIn.setOnClickListener(signUp)

    }

    private val signIn = View.OnClickListener {
        val actionSignIn = SignInFragmentDirections.actionSignInFragmentToGetPostsFragment()
        Navigation.findNavController(it).navigate(actionSignIn)
    }

    private val signUp = View.OnClickListener {
        val actionSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        Navigation.findNavController(it).navigate(actionSignUp)
    }



}