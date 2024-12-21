package com.example.butterapp.data.remote.auth

import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocDto
import com.example.butterapp.data.remote.auth.model.AuthDto
import com.example.butterapp.data.remote.auth.model.LoginRequest
import com.example.butterapp.data.remote.auth.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

typealias AuthResponse = BaseResponseDto<ResultDocDto<AuthDto>>

interface AuthApi {
    @POST("v1/butter/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): AuthResponse

    @POST("v1/butter/login/username")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse
}