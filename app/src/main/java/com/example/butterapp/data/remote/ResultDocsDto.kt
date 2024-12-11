package com.example.butterapp.data.remote

data class ResultDocsDto<T>(
    val docs: List<T>,
    val limit: Int,
    val page: Int,
    val totalDocs: Int,
    val totalPages: Int,
)
