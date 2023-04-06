package com.example.post_api_img.di

import com.example.post_api_img.api.APIService
import okhttp3.MultipartBody
import javax.inject.Inject

class Repository @Inject constructor(
    private val api:APIService
){

    suspend fun uploadFile(filePart: MultipartBody.Part){
        api.uploadFile(filePart)
    }

}