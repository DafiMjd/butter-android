package com.example.butterapp.presentation.user_detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.post.model.ParamGetPosts
import com.example.butterapp.data.remote.post.model.toPost
import com.example.butterapp.data.remote.user.model.toUser
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
class UserDetailViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val postRepo: PostRepository,
) : ViewModel() {
    private val _userId = mutableStateOf("")

    private val _user = MutableStateFlow(User())
    val user get() = _user.asStateFlow()

    private val _userViewStatus = MutableStateFlow(ViewData<Void>())
    val userViewStatus get() = _userViewStatus.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    private val _paramPosts = mutableStateOf(ParamGetPosts.createInstance())

    private val _postsViewStatus = MutableStateFlow(ViewData<Void>())
    val postsViewStatus get() = _postsViewStatus.asStateFlow()

    private val _posts = MutableStateFlow(listOf<Post>())
    val posts get() = _posts.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded get() = _isAllLoaded.asStateFlow()

    fun onBuild(
        userId: String,
        username: String,
        name: String,
    ) {
        _userId.value = userId
        _user.value = _user.value.copy(
            id = userId,
            username = username,
            name = name,
        )
        _paramPosts.value = _paramPosts.value.copy(
            userId = userId
        )
        getUser()
        getPosts()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            _paramPosts.value = ParamGetPosts.createInstance()
            _posts.value = listOf()
            getUser()
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

    private fun getUser() {
        viewModelScope.launch {
            userRepo.getUser(_userId.value).onEach { it ->
                when (it) {
                    is ViewData.Error -> {
                        _userViewStatus.value = ViewData.Error(it.message)
                    }

                    is ViewData.Loading -> {
                        _userViewStatus.value = ViewData.Loading()
                    }

                    is ViewData.Success -> {
                        val user = it.data?.data?.doc?.toUser()
                        if (user != null) {
                            _user.value = user
                        }

                        _userViewStatus.value = ViewData.Success()
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
                        _postsViewStatus.value = ViewData.Error(it.message)
                    }

                    is ViewData.Loading -> {
                        _postsViewStatus.value = ViewData.Loading()
                    }

                    is ViewData.Success -> {
                        val newPosts = it.data?.data?.docs?.map { it.toPost() }
                        if (newPosts != null) {
                            val updatedPosts = _posts.value + newPosts
                            _posts.value = updatedPosts
                        }
                        _postsViewStatus.value = ViewData.Success()
                        _paramPosts.value = _paramPosts.value.nextParam()
                        _isAllLoaded.value = newPosts?.isEmpty() ?: true
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}