package com.example.butterapp.domain.connection

enum class ConnectionType(
    val isFollower: Boolean = false,
    val isFollowing: Boolean = false,
    val text: String = "",
) {
    FOLLOWER(
        isFollower = true,
        text = "Follower"
    ),
    FOLLOWING(
        isFollowing = true,
        text = "Following"
    )
}