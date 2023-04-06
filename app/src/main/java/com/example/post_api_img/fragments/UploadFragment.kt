package com.example.post_api_img.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.post_api_img.R
import com.example.post_api_img.UploadStreamRequestBody
import com.example.post_api_img.viewmodel.ViewModelUpload
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
@AndroidEntryPoint
class UploadFragment : Fragment() {
    companion object{
        private const val CAMERA = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    private lateinit var imgPreview:ImageView
    private lateinit var tvImageName:TextView
    private var imgUri: Uri? = null
    private val viewModel:ViewModelUpload by activityViewModels()
    private lateinit var progressBar:ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnCam = view.findViewById<Button>(R.id.buttonCamera)
        val btnGallery = view.findViewById<Button>(R.id.buttonGallery)
        val btnUpload = view.findViewById<Button>(R.id.buttonUpload)
        imgPreview = view.findViewById(R.id.imagePreview)
        tvImageName = view.findViewById(R.id.tvFileName)
        progressBar = view.findViewById(R.id.progressBar)

        //Camera button
        btnCam.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
        //Gallery Button
        btnGallery.setOnClickListener {
            pickImage.launch("image/*")
        }
        //UploadButton
        btnUpload.setOnClickListener {

            if (imgUri != null) {
                uploadFile()
            }else {
                Toast.makeText(requireContext(),"NO IMG",Toast.LENGTH_SHORT).show()
            }
        }
    }



    //Upload File To API
    private fun uploadFile(){

        lifecycleScope.launch {
            val stream = requireContext().contentResolver.openInputStream(imgUri!!) ?: return@launch
            val request = RequestBody.create("image/*".toMediaTypeOrNull(), stream.readBytes()) // read all bytes using kotlin extension
            val filePart = MultipartBody.Part.createFormData("file", "test.jpg", request)

            try {
                //TODO - Get Progress Bar Here
                Toast.makeText(requireContext(),"Im uploading here ",Toast.LENGTH_SHORT).show()
                viewModel.urlHolder = viewModel.upload(filePart)!!.url

            }
            catch (e: Exception) {
                Log.e("Output","Error during uploading = $e")
                return@launch
            }
            Log.d("MyActivity", "on finish upload file")
            findNavController().navigate(R.id.action_uploadFragment_to_previewFragment)
        }
    }

    //get picture from gallery
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Get the image bitmap from the URI
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            imgUri = uri

            //Set and get name from file
            val name = getFileName(uri)

            //change the icon etc
            imgPreview.setImageBitmap(bitmap)
            tvImageName.text = name
        }else{
            //do nothing
        }
    }

    //get picture from camera
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA){
            try {
                if(data != null) {

                    val imageCapture02 = data.extras?.get("data") as Bitmap
                    val filename = "Image_Capture"

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    val tempUri: Uri = getImageUri(imageCapture02)
                    imgUri = tempUri

                    imgPreview.setImageBitmap(imageCapture02)
                    tvImageName.text = filename
                }
            }catch (e:IOException){
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val outImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true)
        val path = Images.Media.insertImage(requireContext().contentResolver, outImage, "Title", null)
        return Uri.parse(path)
    }

    //get Filename func
    private fun getFileName(uri: Uri): String? {
        // Get the file name from the URI
        var name: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val fileNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (fileNameIndex >= 0) {
                    //Get filename here
                    name = it.getString(fileNameIndex)
                } else {
                    Log.e("TAG", "Column not found: ${OpenableColumns.DISPLAY_NAME}")
                }
            } else {
                Log.e("TAG", "Empty cursor")
            }
        }
        return name
    }

}