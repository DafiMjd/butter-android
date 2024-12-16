//package com.example.butterapp.data.remote.post.dto
//
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import com.example.butterapp.data.remote.ResultDocsDto
//import com.example.butterapp.data.remote.post.dto.post.PostDto
//import kotlinx.coroutines.delay
//import retrofit2.HttpException
//import java.io.IOException
//
//@OptIn(ExperimentalPagingApi::class)
//class PostRemoteMediator(
//    private val postApi: PostApi,
//) : RemoteMediator<Int, ResultDocsDto<PostDto>>() {
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, ResultDocsDto<PostDto>>
//    ): MediatorResult {
//        return try {
//            val loadKey = when (loadType) {
//                LoadType.REFRESH -> 1
//                LoadType.PREPEND -> return MediatorResult.Success(
//                    endOfPaginationReached = true
//                )
//                LoadType.APPEND -> {
//                    val lastItem = state.lastItemOrNull()
//                    // no item
//                    if (lastItem != null) {
//                        (lastItem.id / state.config.pageSize) + 1
//                    }
//
//                    1
//                }
//            }
//
//            delay(1000L)
//
//            val beers = postApi.getPosts(
//                page = loadKey,
//                pageCount = state.config.pageSize
//            )
//
//            return MediatorResult.Success(
//                endOfPaginationReached = beers.isEmpty()
//            )
//        } catch (e: IOException) {
//            MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            MediatorResult.Error(e)
//        }
//    }
//}