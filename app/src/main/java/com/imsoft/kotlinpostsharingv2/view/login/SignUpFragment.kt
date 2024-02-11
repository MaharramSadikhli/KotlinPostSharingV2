package com.imsoft.kotlinpostsharingv2.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
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
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        auth = Firebase.auth
        firestore = Firebase.firestore


        binding.signInButtonAtSignUp.setOnClickListener(signIn)
        binding.signUpButtonAtSignUp.setOnClickListener(signUp)


        binding.textEmailSignUp.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val eMail = s.toString()
                val atIndex = eMail.indexOf('@')
                if (atIndex != -1) {
                    val userName = eMail.substring(0, atIndex)
                    binding.textUserNameSignUp.text = userName
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    private val signIn = View.OnClickListener {
        val actionSignIn = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        Navigation.findNavController(it).navigate(actionSignIn)
    }

    private val signUp = View.OnClickListener {

        val email = binding.textEmailSignUp.text.toString()
        val password = binding.textPasswordSignUp.text.toString()
        val username = binding.textUserNameSignUp.text.toString()



        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val userMap = hashMapOf<String, String>()

                    userMap["userEmail"] = email
                    userMap["userName"] = username
                    userMap["userPassword"] = password

                    firestore.collection("Users")
                        .add(userMap)
                        .addOnSuccessListener {
                            val actionSignUp = SignUpFragmentDirections.actionSignUpFragmentToGetPostsFragment()
                            Navigation.findNavController(binding.root).navigate(actionSignUp)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                        }

                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }

        } else if (email == "" && password.isNotEmpty() && username.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter email!", Toast.LENGTH_LONG).show()
        } else if (password == "" && email.isNotEmpty() && username.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter password!", Toast.LENGTH_LONG).show()
        } else if (username == "" && email.isNotEmpty() && password.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter user name!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Enter email, user name and password!", Toast.LENGTH_LONG).show()
        }


    }

}