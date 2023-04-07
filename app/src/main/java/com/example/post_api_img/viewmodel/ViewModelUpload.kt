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

    var responseBody = MutableLiveData<ImgResponse>()

    //Update to handle error if fail to upload here from app schedule github
    suspend fun upload(filePart: MultipartBody.Part){
        liveURL.value = repo.uploadFile(filePart).body()?.url!!
    }

    suspend fun uploadTest(filePart: MultipartBody.Part):String{

        val response = repo.uploadFile(filePart)
        return if(response.isSuccessful){
            responseBody.value = response.body()
            "Success"
        }else{
            "Fail"
        }
    }

}