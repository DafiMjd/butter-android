package com.example.butterapp.data.repository

import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.connection.ConnectionApi
import com.example.butterapp.data.remote.connection.model.ParamGetConnections
import com.example.butterapp.data.remote.user.UsersResponse
import com.example.butterapp.domain.connection.ConnectionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConnectionRepository @Inject constructor(
    private val connectionApi: ConnectionApi
) {
    suspend fun getConnections(
        param: ParamGetConnections,
        type: ConnectionType,
    ): Flow<ViewData<UsersResponse>> = flow {
        try {
            emit(ViewData.Loading())
            val followers = if (type.isFollower) {
                connectionApi.getFollowers(
                    limit = param.limit,
                    page = param.page,
                    userId = param.userId
                )
            } else {
                connectionApi.getFollowings(
                    limit = param.limit,
                    page = param.page,
                    userId = param.userId
                )
            }
            emit(ViewData.Success(followers))
        } catch (e: Exception) {
            emit(ViewData.Error(e.localizedMessage ?: ""))
        }
    }
}