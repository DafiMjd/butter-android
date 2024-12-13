package com.example.butterapp.data.remote.user.dto.model

import com.example.butterapp.common.helper.DateHelper
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
        birthDate = DateHelper.toLocalDate(birthDate),
        createdAt = DateHelper.toZonedDateTime(createdAt),
        updatedAt = DateHelper.toZonedDateTime(updatedAt),
        isFollowed = isFollowed
    )
}
