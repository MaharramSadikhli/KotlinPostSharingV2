package com.imsoft.kotlinpostsharingv2.view.userHome

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentAddPostBinding
import java.util.UUID


class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImage: Uri? = null
    private lateinit var permission: String
    private val contextUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        registerLauncher()

        binding.imageView.setOnClickListener(selectImage)
        binding.addPostButton.setOnClickListener(addPost)
    }

    private val addPost2 = View.OnClickListener {

        // random image name
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val storageRef = storage.reference

        val imagesRef = storageRef.child("images")
        val imageRef = imagesRef.child(imageName)

        val uploadTask = imageRef.putFile(selectedImage!!) // check null

        if (selectedImage != null) {

            uploadTask.addOnSuccessListener {

                    imageRef.downloadUrl.addOnSuccessListener {
                            val downloadUrl = it.toString()
                            val userComment = binding.commentText.text.toString()

                            if (auth.currentUser != null) {
                                val userUid = auth.currentUser!!.uid
                                // Get username from Users collection
                                firestore.collection("Users").document(userUid).get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            val userName = document.getString("userName")
                                            val postMap = hashMapOf<String, Any>()

                                            postMap["userComment"] = userComment
                                            postMap["downloadUrl"] = downloadUrl
                                            postMap["date"] = Timestamp.now()
                                            postMap["userName"] = userName!!

                                            firestore.collection("Posts").add(postMap)
                                                .addOnSuccessListener {
                                                    val action =
                                                        AddPostFragmentDirections.actionAddPostFragmentToGetPostsFragment()
                                                    Navigation.findNavController(binding.root)
                                                        .navigate(action)
                                                }.addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        requireContext(),
                                                        e.localizedMessage,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        } else {
                                            // Handle scenario where user document doesn't exist
                                            Toast.makeText(
                                                requireContext(),
                                                "User information not found!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }.addOnFailureListener { e ->
                                        Toast.makeText(
                                            requireContext(), e.localizedMessage, Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }

        }


    }

    private val addPost = View.OnClickListener { view->

        // random image name
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val storageRef = storage.reference

        val imagesRef = storageRef.child("images")
        val imageRef = imagesRef.child(imageName)

        val uploadTask = imageRef.putFile(selectedImage!!) // check null

        if (selectedImage != null) {

            uploadTask
                .addOnSuccessListener {

                    imageRef.downloadUrl
                        .addOnSuccessListener {
                            val downloadUrl = it.toString()
                            val userEmail = auth.currentUser!!.email
                            val userComment = binding.commentText.text.toString()

                            val atIndex = userEmail!!.indexOf('@')

                            val username = userEmail.substring(0, atIndex)

                            if (auth.currentUser != null) {
                                val postMap = hashMapOf<String, Any>()

                                postMap["userName"] = username
                                postMap["userComment"] = userComment
                                postMap["downloadUrl"] = downloadUrl
                                postMap["date"] = Timestamp.now()

                                firestore.collection("Posts").add(postMap)
                                    .addOnSuccessListener {
                                        val action = AddPostFragmentDirections.actionAddPostFragmentToGetPostsFragment()
                                        Navigation.findNavController(view).navigate(action)
                                    }
                                    .addOnFailureListener { e->
                                        Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }

        }
    }

    private val selectImage = View.OnClickListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), permission
                    )
                ) {
                    Snackbar.make(
                        requireView(), "Permission Needed For Galery!", Snackbar.LENGTH_INDEFINITE
                    ).setAction("OK") {
                            permissionLauncher.launch(permission)
                        }.show()
                } else {
                    permissionLauncher.launch(permission)
                }
            } else {
                val intent = Intent(Intent.ACTION_PICK, contextUri)
                activityResultLauncher.launch(intent)
            }
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), permission
                    )
                ) {
                    Snackbar.make(
                        requireView(), "Permission Needed For Galery!", Snackbar.LENGTH_INDEFINITE
                    ).setAction("OK") {
                            permissionLauncher.launch(permission)
                        }.show()
                } else {
                    permissionLauncher.launch(permission)
                }
            } else {
                val intent = Intent(Intent.ACTION_PICK, contextUri)
                activityResultLauncher.launch(intent)
            }
        }
    }

    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == RESULT_OK) {
                    val intent = result.data

                    if (intent != null) {
                        selectedImage = intent.data
                        selectedImage?.let {
                            binding.imageView.setImageURI(it)
                        }
                    }
                }

            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intent = Intent(Intent.ACTION_PICK, contextUri)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, "Permission Needed!", Toast.LENGTH_LONG).show()
                }
            }

    }

}



