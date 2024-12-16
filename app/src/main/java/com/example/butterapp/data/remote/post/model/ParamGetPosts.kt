package com.example.butterapp.data.remote.post.model

data class ParamGetPosts(
    val limit: Int = 10,
    val page: Int = 1,
    val userId: String? = null
) {
    companion object Factory {
        fun createInstance(): ParamGetPosts = ParamGetPosts()
    }

    fun nextParam(): ParamGetPosts {
        return ParamGetPosts(
            limit = this.limit,
            page = this.page + 1,
            userId = this.userId
        )
    }
}