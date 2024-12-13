package com.example.butterapp.domain.user

import java.time.LocalDate
import java.time.ZonedDateTime

data class User(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val birthDate: LocalDate?,
    val createdAt: ZonedDateTime?,
    val updatedAt: ZonedDateTime?,
    val isFollowed: Boolean
)
