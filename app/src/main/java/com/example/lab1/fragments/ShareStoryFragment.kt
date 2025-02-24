package com.example.lab1.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.lab1.BuildConfig
import com.example.lab1.R
import com.example.lab1.databinding.FragmentShareStoryBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ShareStoryFragment : Fragment() {

    private var _binding: FragmentShareStoryBinding? = null
    private val binding get() = _binding!!

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val dataUri = result.data?.data
            if (dataUri != null) {
                shareToInstagramStory(dataUri)
            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPickImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun shareToInstagramStory(imageUri: Uri) {

        val backgroundAssetUri = copyUriToCache(imageUri) ?: run {
            Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show()
            return
        }

        // Instagram story deep link
        val intent = Intent("com.instagram.share.ADD_TO_STORY")

        // Attach your App ID to the intent
        val sourceApplication = getString(R.string.facebook_app_id) // This is your application's FB ID
        intent.putExtra("source_application", sourceApplication)

        intent.setDataAndType(backgroundAssetUri, "image/*")
        intent.putExtra("interactive_asset_uri", backgroundAssetUri)

        // Grant temporary read permission to Instagram
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val resolveInfo = requireContext().packageManager.queryIntentActivities(intent, 0)
        if (resolveInfo.isNotEmpty()) {
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Instagram not installed.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Instagram app not found.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Copies the selected image Uri to a cache FileProvider location
     * so that Instagram can read it.
     */
    private fun copyUriToCache(selectedUri: Uri): Uri? {
        return try {
            val parcelFileDescriptor =
                requireContext().contentResolver.openFileDescriptor(selectedUri, "r")
                    ?: return null

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

            // Create a temp file in cache
            val tempFile = File(
                requireContext().cacheDir,
                "insta_share_${System.currentTimeMillis()}.png"
            )

            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            parcelFileDescriptor.close()

            // Get a FileProvider Uri
            FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                tempFile
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}