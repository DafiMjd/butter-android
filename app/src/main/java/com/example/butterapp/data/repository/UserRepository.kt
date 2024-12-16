package com.example.butterapp.data.repository

import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.user.dto.UserApi
import com.example.butterapp.data.remote.user.dto.UserResponse
import com.example.butterapp.data.remote.user.dto.UsersResponse
import com.example.butterapp.data.remote.user.dto.model.ParamGetUsers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getUsers(param: ParamGetUsers): Flow<ViewData<UsersResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val users = userApi.getUsers(
                limit = param.limit,
                page = param.page,
            )
            emit(ViewData.Success(users))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }

    suspend fun getUser(userId: String): Flow<ViewData<UserResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val user = userApi.getUser(
                userId = userId,
            )
            emit(ViewData.Success(user))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }
}