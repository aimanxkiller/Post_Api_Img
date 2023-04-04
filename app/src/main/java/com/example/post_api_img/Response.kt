package com.example.post_api_img

data class Response(
    val FileExt: String,
    val FileId: String,
    val FileName: String,
    val FileSize: Int,
    val Url: String
)