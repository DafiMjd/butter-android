package com.example.butterapp.data.repository

import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocsDto
import com.example.butterapp.data.remote.post.dto.PostApi
import com.example.butterapp.data.remote.post.dto.PostResponse
import com.example.butterapp.data.remote.post.dto.post.ParamGetPosts
import com.example.butterapp.data.remote.post.dto.post.PostDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postApi: PostApi
) {
    suspend fun getPosts(param: ParamGetPosts): Flow<ViewData<PostResponse>> = flow {
        try {

            emit(ViewData.Loading())
            val posts = postApi.getPosts(
                limit = param.limit,
                page = param.page,
                userId = param.userId
            )
            emit(ViewData.Success(posts))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }
}