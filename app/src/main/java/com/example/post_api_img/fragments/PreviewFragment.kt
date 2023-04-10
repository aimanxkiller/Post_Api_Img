package com.example.post_api_img.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    private val url:String by lazy {
        arguments?.getString("passUrl")?:""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnRestart:Button = view.findViewById(R.id.buttonRestart)
        val urlPlaceholder:TextView = view.findViewById(R.id.tvURL)

        urlPlaceholder.text = url
        imgPreview = view.findViewById(R.id.imageUrl)

        loadImg(urlPlaceholder.text.toString())

        btnRestart.setOnClickListener {
            findNavController().navigate(R.id.action_previewFragment_to_uploadFragment)
            //findNavController().popBackStack(findNavController().graph.startDestinationId,false)
        }

    }

    //Loads image from url to imagePreview
    private fun loadImg(url:String){
        Glide.with(requireContext())
            .load(url)
            .into(imgPreview)
    }
}