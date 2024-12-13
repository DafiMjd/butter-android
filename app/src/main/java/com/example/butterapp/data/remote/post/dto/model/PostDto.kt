package com.example.butterapp.data.remote.post.dto.model

import com.example.butterapp.common.helper.DateHelper
import com.example.butterapp.data.remote.user.dto.model.UserDto
import com.example.butterapp.domain.post.Post

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
        createdAt = DateHelper.toZonedDateTime(createdAt),
        updatedAt = DateHelper.toZonedDateTime(updatedAt),
        user = user
    )
}
