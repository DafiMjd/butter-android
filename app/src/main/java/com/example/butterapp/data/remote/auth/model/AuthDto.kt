package com.example.butterapp.data.remote.auth.model

import com.example.butterapp.data.remote.user.model.UserDto

data class AuthDto(
    val token: String,
    val refreshToken: String,
    val user: UserDto,
)
