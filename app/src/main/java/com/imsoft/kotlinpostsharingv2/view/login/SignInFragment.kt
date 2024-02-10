package com.imsoft.kotlinpostsharingv2.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentSignInBinding
import com.imsoft.kotlinpostsharingv2.view.GetPostsFragment
import com.imsoft.kotlinpostsharingv2.view.MainActivity


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

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


        auth = Firebase.auth
        firestore = Firebase.firestore

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val actionSignIn = SignInFragmentDirections.actionSignInFragmentToGetPostsFragment()
            findNavController().navigate(actionSignIn)
        }


        binding.signInButton.setOnClickListener(signIn)
        binding.signUpButtonAtSignIn.setOnClickListener(signUp)


    }

    private val signIn = View.OnClickListener {

        val email = binding.textEmailSignIn.text.toString()
        val password = binding.textPasswordSignIn.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val actionSignIn = SignInFragmentDirections.actionSignInFragmentToGetPostsFragment()
                    Navigation.findNavController(binding.root).navigate(actionSignIn)

                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        } else if (email == "" && password.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter email!", Toast.LENGTH_LONG).show()
        } else if (password == "" && email.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter password!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Enter email and password!", Toast.LENGTH_LONG).show()
        }

    }

    private val signUp = View.OnClickListener {

        val actionSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        Navigation.findNavController(it).navigate(actionSignUp)
    }



}