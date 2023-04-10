package com.example.post_api_img.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.post_api_img.api.UploadStreamRequestBody
import com.example.post_api_img.di.Repository
import com.example.post_api_img.model.ImgResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ViewModelUpload @Inject constructor(
    private val repo:Repository
): ViewModel() {

    var responseBody = MutableLiveData<ImgResponse>()
    var progress = MutableLiveData<Int>()

    suspend fun uploadTest(filePart: MultipartBody.Part):String{

        val response = repo.uploadFile(filePart)
        return if(response.isSuccessful){
            responseBody.value = response.body()
            "Success"
        }else{
            "Fail"
        }
    }

    fun uploadVM(stream: InputStream) {

        viewModelScope.launch {
            val request = UploadStreamRequestBody("image/*",stream, onUploadProgress = {
                Log.d("MyActivity","On upload progress $it")
                //progress here
                progress.postValue(it)
            })
            val filePart = MultipartBody.Part.createFormData("file","test.jpg",request)
            try {
                val response = repo.uploadFile(filePart)

                if(response.isSuccessful){
                    responseBody.postValue(response.body())
                }else{
                    responseBody.postValue(ImgResponse())
                }
            }catch(e:Exception){
                responseBody.postValue(ImgResponse())
                Log.e("Error","Error During upload")
            }
        }


    }

}