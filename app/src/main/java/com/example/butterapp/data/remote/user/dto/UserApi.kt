package com.example.butterapp.data.remote.user.dto

import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocDto
import com.example.butterapp.data.remote.ResultDocsDto
import com.example.butterapp.data.remote.user.dto.model.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

typealias UsersResponse = BaseResponseDto<ResultDocsDto<UserDto>>
typealias UserResponse = BaseResponseDto<ResultDocDto<UserDto>>

interface UserApi {
    @GET("v1/butter/users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): UsersResponse

    @GET("v1/butter/user/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String,
    ): UserResponse
}