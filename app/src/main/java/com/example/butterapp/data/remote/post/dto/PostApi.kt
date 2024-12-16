package com.example.butterapp.data.remote.post.dto

import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocDto
import com.example.butterapp.data.remote.ResultDocsDto
import com.example.butterapp.data.remote.post.dto.model.PostDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

typealias PostsResponse = BaseResponseDto<ResultDocsDto<PostDto>>
typealias PostResponse = BaseResponseDto<ResultDocDto<PostDto>>

interface PostApi {
    @GET("v1/butter/posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("userId") userId: String?
    ): PostsResponse

    @GET("v1/butter/post/{postId}")
    suspend fun getPost(
        @Path("postId") postId: String,
    ): PostResponse
}