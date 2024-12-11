package com.example.butterapp.data.remote.user.dto

import com.example.butterapp.domain.user.User

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val birthDate: String,
    val createdAt: String,
    val updatedAt: String,
    val isFollowed: Boolean
)

fun UserDto.toUser(): User {
    return User(
        id = id,
        username = username,
        name = name,
        email = email,
        birthDate = birthDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isFollowed = isFollowed
    )
}
