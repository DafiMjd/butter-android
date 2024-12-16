package com.example.butterapp.domain.post

import com.example.butterapp.domain.user.User
import java.time.ZonedDateTime

data class Post(
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val createdAt: ZonedDateTime? = null,
    val updatedAt: ZonedDateTime? = null,
    val user: User? = null,
)
