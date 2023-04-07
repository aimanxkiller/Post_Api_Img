package com.example.post_api_img.di

import com.example.post_api_img.api.APIService
import com.example.post_api_img.model.ImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api:APIService
){

    suspend fun uploadFile(filePart: MultipartBody.Part): Response<ImgResponse> {
        return api.uploadFile(filePart)
    }

}