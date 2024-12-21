package com.example.butterapp

import android.content.Context
import androidx.room.Room
import com.example.butterapp.data.local.user.UserDatabase
import com.example.butterapp.data.remote.BaseResponseDto
import com.example.butterapp.data.remote.ResultDocDto
import com.example.butterapp.data.remote.ResultDocsDto
import com.example.butterapp.data.remote.auth.AuthApi
import com.example.butterapp.data.remote.auth.AuthResponse
import com.example.butterapp.data.remote.auth.model.AuthDto
import com.example.butterapp.data.remote.auth.model.LoginRequest
import com.example.butterapp.data.remote.auth.model.SignUpRequest
import com.example.butterapp.data.remote.user.UserApi
import com.example.butterapp.data.remote.user.UserResponse
import com.example.butterapp.data.remote.user.UsersResponse
import com.example.butterapp.data.remote.user.model.UserDto
import com.example.butterapp.data.repository.AuthRepository
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import org.junit.Test

import org.junit.Assert.*
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class AuthApiImpl() : AuthApi {
    override suspend fun signUp(request: SignUpRequest): AuthResponse {
        return BaseResponseDto(
            code = 200,
            message = "success",
            status = "success",
            data = ResultDocDto(
                doc = AuthDto(
                    user = UserDto(
                        id = "1",
                        username = request.username,
                        email = request.email,
                        name = request.name,
                        birthDate = null,
                        isFollowed = false,
                        followersCount = 0,
                        followingsCount = 0,
                        createdAt = "2021-01-01T00:00:00Z",
                        updatedAt = "2021-01-01T00:00:00Z",
                    ),
                    token = "token",
                    refreshToken = "refreshToken",
                )
            )
        )
    }

    override suspend fun login(request: LoginRequest): AuthResponse {
        return BaseResponseDto(
            code = 200,
            message = "success",
            status = "success",
            data = ResultDocDto(
                doc = AuthDto(
                    user = UserDto(
                        id = "1",
                        username = request.username,
                        email = "email",
                        name = "name",
                        birthDate = null,
                        isFollowed = false,
                        followersCount = 0,
                        followingsCount = 0,
                        createdAt = "2021-01-01T00:00:00Z",
                        updatedAt = "2021-01-01T00:00:00Z",
                    ),
                    token = "token",
                    refreshToken = "refreshToken",
                )
            )
        )
    }
}

class UserApiImpl : UserApi {
    override suspend fun getUsers(limit: Int, page: Int): UsersResponse {
        return BaseResponseDto(
            code = 200,
            message = "success",
            status = "success",
            data = ResultDocsDto(
                docs = listOf(
                    UserDto(
                        id = "id",
                        username = "username",
                        email = "email",
                        name = "name",
                        birthDate = null,
                        isFollowed = false,
                        followersCount = 0,
                        followingsCount = 0,
                        createdAt = "2021-01-01T00:00:00Z",
                        updatedAt = "2021-01-01T00:00:00Z",
                    )
                ),
                limit = limit,
                page = page,
                totalDocs = 1,
                totalPages = 1,
            )
        )
    }

    override suspend fun getUser(userId: String): UserResponse {
        return BaseResponseDto(
            code = 200,
            message = "success",
            status = "success",
            data = ResultDocDto(
                doc = UserDto(
                    id = userId,
                    username = "username",
                    email = "email",
                    name = "name",
                    birthDate = null,
                    isFollowed = false,
                    followersCount = 0,
                    followingsCount = 0,
                    createdAt = "2021-01-01T00:00:00Z",
                    updatedAt = "2021-01-01T00:00:00Z",
                ),
            )
        )
    }


    override suspend fun getUserByUsername(username: String): UserResponse {
        return BaseResponseDto(
            code = 200,
            message = "success",
            status = "success",
            data = ResultDocDto(
                doc = UserDto(
                    id = "id",
                    username = username,
                    email = "email",
                    name = "name",
                    birthDate = null,
                    isFollowed = false,
                    followersCount = 0,
                    followingsCount = 0,
                    createdAt = "2021-01-01T00:00:00Z",
                    updatedAt = "2021-01-01T00:00:00Z",
                ),
            )
        )
    }
}

//class ExampleUnitTest {
//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }
//
//    @Test
//    fun testAuthRepository() {
//        val context = Contexts.getApplication(Context)
//        val userDatabase = Room.databaseBuilder(
//            context,
//            UserDatabase::class.java,
//            "users.db"
//        ).build()
//        val authRepository = AuthRepository(
//            authApi = AuthApiImpl(),
//            userApi = UserApiImpl(),
//            userDatabase = UserDatabase(),
//            authDatabase = null,
//        )
//        assertNotNull(authRepository)
//    }
//}