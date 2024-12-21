package com.example.butterapp.presentation.post

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.local.user.toUser
import com.example.butterapp.data.remote.post.model.ParamGetPosts
import com.example.butterapp.data.remote.post.model.toPost
import com.example.butterapp.data.repository.AuthRepository
import com.example.butterapp.data.repository.PostRepository
import com.example.butterapp.domain.post.Post
import com.example.butterapp.domain.post.PostScreenType
import com.example.butterapp.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repo: PostRepository,
    private val authRepo: AuthRepository,
) : ViewModel() {
    private val _param = mutableStateOf(ParamGetPosts.createInstance())

    private val _type = mutableStateOf(PostScreenType.ALL)
    val type get() = _type.value

    private val _posts = MutableStateFlow(listOf<Post>())
    val posts get() = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded get() = _isAllLoaded.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage get() = _errorMessage.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    private val _userViewData = MutableStateFlow<ViewData<User>>(ViewData())
    val userViewData get() = _userViewData.asStateFlow()

    fun onBuild(screenType: PostScreenType) {
        _type.value = screenType
        if (screenType.isAll) {
            getPosts()
        }

        if (screenType.isFollowing) {
            println("following")
            getLocalUser()
            _userViewData.onEach {
                if (it.isSuccess) {
                    _param.value = _param.value.copy(
                        userId = it.data?.id ?: ""
                    )
                    getPosts()
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _param.value = ParamGetPosts.createInstance()
            _posts.value = listOf()
            getPosts()
            delay(500L)
            _isRefreshing.value = false
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_posts.value.isNotEmpty()) {
                getPosts()
            }
        }
    }

    private fun getPosts() {
        viewModelScope.launch {
            repo.getPosts(_param.value).onEach { it ->
                when (it) {
                    is ViewData.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = it.message
                    }

                    is ViewData.Loading -> {
                        _isLoading.value = true
                        _errorMessage.value = ""
                    }

                    is ViewData.Success -> {
                        _isLoading.value = false
                        val newPosts = it.data?.data?.docs?.map { it.toPost() }
                        if (newPosts != null) {
                            val updatedPosts = _posts.value + newPosts
                            _posts.value = updatedPosts
                        }
                        _errorMessage.value = ""
                        _param.value = _param.value.nextParam()
                        _isAllLoaded.value = newPosts?.isEmpty() ?: true
                    }
                }
            }.launchIn(viewModelScope)
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
                            println("dapet user local")
                            _userViewData.update { ViewData.Success(user.toUser()) }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}