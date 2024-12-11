package com.example.butterapp.data.remote

data class BaseResponseDto<T>(
    val code: Int,
    val status: String,
    val message: String,
    val data: T,
)
