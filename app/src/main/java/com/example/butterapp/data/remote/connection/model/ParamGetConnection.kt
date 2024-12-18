package com.example.butterapp.data.remote.connection.model

data class ParamGetConnections(
    val limit: Int = 10,
    val page: Int = 1,
    val userId: String = ""
) {
    companion object Factory {
        fun createInstance(): ParamGetConnections = ParamGetConnections()
    }

    fun nextParam(): ParamGetConnections {
        return ParamGetConnections(
            limit = this.limit,
            page = this.page + 1,
            userId = this.userId
        )
    }
}