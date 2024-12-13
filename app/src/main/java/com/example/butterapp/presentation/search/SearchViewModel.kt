package com.example.butterapp.presentation.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.user.dto.model.ParamGetUsers
import com.example.butterapp.data.remote.user.dto.model.toUser
import com.example.butterapp.data.repository.UserRepository
import com.example.butterapp.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _param = mutableStateOf(ParamGetUsers.createInstance())

    private val _users = MutableStateFlow(listOf<User>())
    val users get() = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded get() = _isAllLoaded.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage get() = _errorMessage.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()


    fun onBuild() {
        getUsers()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _param.value = ParamGetUsers.createInstance()
            _users.value = listOf()
            getUsers()
            _isRefreshing.value = false
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_users.value.isNotEmpty()) {
                getUsers()
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            repo.getUsers(_param.value).onEach { it ->
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
                        val newUsers = it.data?.data?.docs?.map { it.toUser() }
                        if (newUsers != null) {
                            val updatedData = _users.value + newUsers
                            _users.value = updatedData
                        }
                        _errorMessage.value = ""
                        _param.value = _param.value.nextParam()
                        _isAllLoaded.value = newUsers?.isEmpty() ?: true
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}