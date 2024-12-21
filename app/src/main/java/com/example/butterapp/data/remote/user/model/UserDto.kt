package com.example.butterapp.data.remote.user.model

import com.example.butterapp.common.helper.DateHelper
import com.example.butterapp.data.local.user.UserEntity
import com.example.butterapp.domain.user.User

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val birthDate: String?,
    val createdAt: String,
    val updatedAt: String,
    val isFollowed: Boolean,
    val followersCount: Int,
    val followingsCount: Int,
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
        isFollowed = isFollowed,
        followersCount = followersCount,
        followingsCount = followingsCount,
    )
}

fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        name = name,
        email = email,
        birthDate = birthDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isFollowed = isFollowed,
        followersCount = followersCount,
        followingsCount = followingsCount,
    )
}
