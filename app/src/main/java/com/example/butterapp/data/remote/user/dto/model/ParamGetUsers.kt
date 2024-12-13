package com.example.butterapp.data.remote.user.dto.model

data class ParamGetUsers(
    val limit: Int = 10,
    val page: Int = 1,
) {
    companion object Factory {
        fun createInstance(): ParamGetUsers = ParamGetUsers()
    }

    fun nextParam(): ParamGetUsers {
        return ParamGetUsers(
            limit = this.limit,
            page = this.page + 1,
        )
    }
}
