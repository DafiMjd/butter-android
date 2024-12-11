package com.example.butterapp.domain.user

data class User(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val birthDate: String,
    val createdAt: String,
    val updatedAt: String,
    val isFollowed: Boolean
)
