package com.example.butterapp.presentation.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.local.user.toUser
import com.example.butterapp.data.remote.post.model.ParamGetPosts
import com.example.butterapp.data.remote.post.model.toPost
import com.example.butterapp.data.remote.user.model.toUser
import com.example.butterapp.data.repository.AuthRepository
import com.example.butterapp.data.repository.PostRepository
import com.example.butterapp.data.repository.UserRepository
import com.example.butterapp.domain.post.Post
import com.example.butterapp.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val postRepo: PostRepository,
) : ViewModel() {
    private val _userViewData = MutableStateFlow<ViewData<User>>(ViewData())
    val userViewData get() = _userViewData.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    private val _paramPosts = mutableStateOf(ParamGetPosts.createInstance())

    private val _postsViewStatus = MutableStateFlow(ViewData<Void>())
    val postsViewStatus get() = _postsViewStatus.asStateFlow()

    private val _posts = MutableStateFlow(listOf<Post>())
    val posts get() = _posts.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded get() = _isAllLoaded.asStateFlow()

    init {
        getLocalUser()
        _userViewData.onEach {
            if (it.isSuccess) {
                _paramPosts.value = _paramPosts.value.copy(
                    userId = it.data?.id ?: ""
                )
                getRemoteUser()
                getPosts()
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            _paramPosts.value = ParamGetPosts.createInstance()
            _posts.value = listOf()
            getRemoteUser()
            getPosts()
            delay(500L)
            _isRefreshing.update { false }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_posts.value.isNotEmpty()) {
                getPosts()
            }
        }
    }

    private fun getLocalUser() {
        viewModelScope.launch {
            authRepo.getLocalUser().onEach { it ->
                when (it) {
                    is ViewData.Error -> {
                        _userViewData.update { ViewData.Error(it.message) }
                    }

                    is ViewData.Loading -> {
                        _userViewData.update { ViewData.Loading() }
                    }

                    is ViewData.Success -> {
                        val user = it.data
                        if (user != null) {
                            _userViewData.update { ViewData.Success(user.toUser()) }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getRemoteUser() {
        val id = _userViewData.value.data?.id
        if (id.isNullOrBlank()) {
            return
        }

        viewModelScope.launch {
            userRepo.getUser(id).onEach {
                if (it.isSuccess) {
                    val user = it.data?.data?.doc?.toUser()
                    if (user != null) {
                        _userViewData.update { ViewData.Success(user) }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getPosts() {
        viewModelScope.launch {
            postRepo.getPosts(_paramPosts.value).onEach { it ->
                when (it) {
                    is ViewData.Error -> {
                        _postsViewStatus.update { ViewData.Error(it.message) }
                    }

                    is ViewData.Loading -> {
                        _postsViewStatus.update { ViewData.Loading() }
                    }

                    is ViewData.Success -> {
                        val newPosts = it.data?.data?.docs?.map { it.toPost() }
                        if (newPosts != null) {
                            val updatedPosts = _posts.value + newPosts
                            _posts.value = updatedPosts
                        }
                        _postsViewStatus.update { ViewData.Success() }
                        _paramPosts.value = _paramPosts.value.nextParam()
                        _isAllLoaded.update { newPosts?.isEmpty() ?: true }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}