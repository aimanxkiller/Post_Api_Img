package com.example.post_api_img.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.post_api_img.R

class PreviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    private lateinit var imgPreview:ImageView
    private var url:String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnLoad:Button = view.findViewById(R.id.buttonLoad)
        val inputURL:EditText = view.findViewById(R.id.etURL)
        imgPreview = view.findViewById(R.id.imageUrl)

        btnLoad.setOnClickListener {

            url = inputURL.text.toString()
            if (url != null){
            loadImg(url!!)
            }else{
                Toast.makeText(requireContext(),"Please enter test URL !",Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Loads image from url to imagePreview
    private fun loadImg(url:String){

        Glide.with(requireContext())
            .load(url)
            .into(imgPreview)

    }
}