package com.example.butterapp.domain.post

import com.example.butterapp.data.remote.user.dto.UserDto
import java.time.ZonedDateTime
import java.util.Date

data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val createdAt: ZonedDateTime?,
    val updatedAt: ZonedDateTime?,
    val user: UserDto?,
)
