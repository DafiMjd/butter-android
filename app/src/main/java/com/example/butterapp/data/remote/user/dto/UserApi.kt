package com.example.butterapp.data.remote.user.dto

import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocsDto
import com.example.butterapp.data.remote.user.dto.model.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

typealias UserResponse = BaseResponseDto<ResultDocsDto<UserDto>>

interface UserApi {
    @GET("v1/butter/users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): BaseResponseDto<ResultDocsDto<UserDto>>
}