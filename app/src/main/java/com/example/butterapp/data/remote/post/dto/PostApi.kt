package com.example.butterapp.data.remote.post.dto

import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocsDto
import com.example.butterapp.data.remote.post.dto.post.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

typealias PostResponse = BaseResponseDto<ResultDocsDto<PostDto>>

interface PostApi {
    @GET("v1/butter/posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("userId") userId: String?
    ): BaseResponseDto<ResultDocsDto<PostDto>>
}