package com.example.butterapp.presentation.post_detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.post.dto.model.toPost
import com.example.butterapp.data.repository.PostRepository
import com.example.butterapp.domain.post.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repo: PostRepository,
) : ViewModel() {
    private val _postId = mutableStateOf("")

    private val _post = MutableStateFlow(Post())
    val post get() = _post.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage get() = _errorMessage.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    fun onBuild(postId: String) {
        _postId.value = postId
        getPost()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getPost()
            _isRefreshing.value = false
        }
    }

    fun getPost() {
        viewModelScope.launch {
            repo.getPost(_postId.value).onEach { it ->
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
                        val post = it.data?.data?.doc?.toPost()
                        if (post != null) {
                            _post.value = post
                        }
                        _errorMessage.value = ""
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}