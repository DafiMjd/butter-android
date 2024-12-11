package com.example.butterapp.data.remote.post.dto.post

import com.example.butterapp.common.helper.DateHelper
import com.example.butterapp.data.remote.user.dto.UserDto
import com.example.butterapp.domain.post.Post
import com.example.butterapp.domain.user.User

data class PostDto(
    val id: String,
    val userId: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDto,
)

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        userId = userId,
        content = content,
        createdAt = DateHelper.parse(createdAt),
        updatedAt = DateHelper.parse(updatedAt),
        user = user
    )
}
