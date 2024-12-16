package com.example.butterapp.domain.user

import java.time.LocalDate
import java.time.ZonedDateTime

data class User(
    val id: String = "",
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: LocalDate? = null,
    val createdAt: ZonedDateTime? = null,
    val updatedAt: ZonedDateTime? = null,
    val isFollowed: Boolean = false,
    val followersCount: Int = 0,
    val followingsCount: Int = 0,
) {
    val hasConnection = this.followersCount != 0 && this.followingsCount != 0
}
