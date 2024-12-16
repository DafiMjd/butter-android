package com.example.butterapp.data.repository

import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.post.PostApi
import com.example.butterapp.data.remote.post.PostResponse
import com.example.butterapp.data.remote.post.PostsResponse
import com.example.butterapp.data.remote.post.model.ParamGetPosts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postApi: PostApi
) {
    suspend fun getPosts(param: ParamGetPosts): Flow<ViewData<PostsResponse>> = flow {
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

    suspend fun getPost(postId: String): Flow<ViewData<PostResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val post = postApi.getPost(
                postId = postId
            )
            emit(ViewData.Success(post))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }
}