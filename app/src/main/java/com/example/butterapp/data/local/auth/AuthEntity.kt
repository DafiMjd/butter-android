package com.example.butterapp.data.local.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthEntity(
    @PrimaryKey
    val token: String,
    val refreshToken: String,
)
