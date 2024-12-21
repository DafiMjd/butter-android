package com.example.butterapp.data.repository

import com.example.butterapp.common.ViewData
import com.example.butterapp.common.helper.HttpHelper
import com.example.butterapp.data.local.auth.AuthDatabase
import com.example.butterapp.data.local.auth.AuthEntity
import com.example.butterapp.data.local.user.UserDatabase
import com.example.butterapp.data.local.user.UserEntity
import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.auth.AuthApi
import com.example.butterapp.data.remote.auth.AuthResponse
import com.example.butterapp.data.remote.auth.model.LoginRequest
import com.example.butterapp.data.remote.auth.model.SignUpRequest
import com.example.butterapp.data.remote.user.UserApi
import com.example.butterapp.data.remote.user.UserResponse
import com.example.butterapp.data.remote.user.model.UserDto
import com.example.butterapp.data.remote.user.model.toUserEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val userDatabase: UserDatabase,
    private val authDatabase: AuthDatabase,
) {
    suspend fun signUp(
        request: SignUpRequest
    ): Flow<ViewData<AuthResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val res = authApi.signUp(
                request
            )
            val user = res.data.doc.user
            setAuthData(
                user,
                res.data.doc.token,
                res.data.doc.refreshToken,
            )

            emit(ViewData.Success(res))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }

    suspend fun login(
        request: LoginRequest
    ): Flow<ViewData<AuthResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val res = authApi.login(
                request
            )
            val user = res.data.doc.user
            setAuthData(
                user,
                res.data.doc.token,
                res.data.doc.refreshToken,
            )

            emit(ViewData.Success(res))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }

    private suspend fun setAuthData(
        user: UserDto, token: String, refreshToken: String,
    ) {
        userDatabase.dao.clearAll()
        userDatabase.dao.insertUser(user.toUserEntity())
        authDatabase.dao.clearAll()
        authDatabase.dao.insertAuth(
            AuthEntity(
                token = token, refreshToken = refreshToken
            )
        )
    }

    suspend fun getUserByUsername(
        username: String
    ): Flow<ViewData<UserResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val res = userApi.getUserByUsername(
                username
            )

            emit(ViewData.Success(res))
        } catch (e: HttpException) {

            emit(
                ViewData.Error(
                    e.message(),
                    // still could not convert response
                    data = HttpHelper.parseResponseBody<UserResponse>(e.response()?.errorBody())
                )
            )
        } catch (e: Exception) {
            emit(ViewData.Error(e.message ?: ""))
        }
    }

    suspend fun getLocalUser(): Flow<ViewData<UserEntity>> = flow {
        try {
            emit(ViewData.Loading())
            val res = userDatabase.dao.getUser()
            emit(ViewData.Success(res))
        } catch (e: HttpException) {
            emit(ViewData.Error(e.message()))
        } catch (e: Exception) {
            emit(ViewData.Error(e.message ?: ""))
        }
    }
}