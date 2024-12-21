package com.example.butterapp.domain.post

enum class PostScreenType(
    val isAll: Boolean = false,
    val isFollowing: Boolean = false,
) {
    ALL(isAll = true), FOLLOWING(isFollowing = true)
}