package com.example.butterapp.data.remote.connection

import com.example.butterapp.data.remote.user.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ConnectionApi {
    @GET("v1/butter/followers")
    suspend fun getFollowers(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("userId") userId: String?
    ): UsersResponse

    @GET("v1/butter/followings")
    suspend fun getFollowings(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("userId") userId: String?
    ): UsersResponse
}