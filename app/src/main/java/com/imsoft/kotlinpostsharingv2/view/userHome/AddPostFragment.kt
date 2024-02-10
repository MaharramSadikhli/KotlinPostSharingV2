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
import com.google.android.material.snackbar.Snackbar
import com.imsoft.kotlinpostsharingv2.R
import com.imsoft.kotlinpostsharingv2.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImage: Uri? = null
    private lateinit var permission: String
    private val contextUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

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

        registerLauncher()

        binding.imageView.setOnClickListener(selectImage)
        binding.addPostButton.setOnClickListener(addPost)
    }

    private val addPost = View.OnClickListener {
        val actionAddPost = AddPostFragmentDirections.actionAddPostFragmentToGetPostsFragment()
        Navigation.findNavController(it).navigate(actionAddPost)
    }

    private val selectImage = View.OnClickListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                    Snackbar.make(requireView(), "Permission Needed For Galery!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK") {
                            permissionLauncher.launch(permission)
                        }
                        .show()
                } else {
                    permissionLauncher.launch(permission)
                }
            } else {
                val intent = Intent(Intent.ACTION_PICK, contextUri)
                activityResultLauncher.launch(intent)
            }
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(),permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                    Snackbar.make(requireView(), "Permission Needed For Galery!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK") {
                            permissionLauncher.launch(permission)
                        }
                        .show()
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

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

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

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                val intent = Intent(Intent.ACTION_PICK, contextUri)
                startActivity(intent)
            } else {
                Toast.makeText(activity, "Permission Needed!", Toast.LENGTH_LONG).show()
            }
        }

    }

}