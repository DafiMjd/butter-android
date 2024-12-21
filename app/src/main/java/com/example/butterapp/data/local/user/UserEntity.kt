package com.example.butterapp.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.butterapp.common.helper.DateHelper
import com.example.butterapp.data.remote.user.model.UserDto
import com.example.butterapp.domain.user.User

@Entity
data class UserEntity (
    @PrimaryKey
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

fun UserEntity.toUser(): User {
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