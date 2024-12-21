package com.example.butterapp.data.remote.auth.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String,
    val name: String,
)