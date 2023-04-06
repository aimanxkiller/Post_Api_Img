package com.example.post_api_img.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.post_api_img.di.Repository
import com.example.post_api_img.model.ImgResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ViewModelUpload @Inject constructor(
    private val repo:Repository
): ViewModel() {

    var liveURL = MutableLiveData<String>()

    suspend fun upload(filePart: MultipartBody.Part){
        liveURL.value = repo.uploadFile(filePart)!!.url!!
    }

}