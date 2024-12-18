package com.example.butterapp.presentation.user_detail.view_model

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.connection.model.ParamGetConnections
import com.example.butterapp.data.remote.user.model.toUser
import com.example.butterapp.data.repository.ConnectionRepository
import com.example.butterapp.domain.connection.ConnectionType
import com.example.butterapp.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val repo: ConnectionRepository,
) : ViewModel() {
    val selectedTabIndex = mutableIntStateOf(0)
    private val _param = mutableStateOf(ParamGetConnections.createInstance())
    private val _type = mutableStateOf(ConnectionType.FOLLOWER)

    private val _users = MutableStateFlow(listOf<User>())
    val users get() = _users.asStateFlow()

    private val _usersViewStatus = MutableStateFlow(ViewData<Void>())
    val usersViewStatus get() = _usersViewStatus.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded get() = _isAllLoaded.asStateFlow()

    fun onBuild(type: ConnectionType, userId: String) {
        _param.value = _param.value.copy(userId = userId)
        _type.value = type
        getConnections()
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_users.value.isNotEmpty()) {
                getConnections()
            }
        }
    }

    fun getConnections() {
        viewModelScope.launch {
            repo.getConnections(
                param = _param.value, type = _type.value,
            ).onEach { it ->
                when (it) {
                    is ViewData.Error -> {
                        _usersViewStatus.value = ViewData.Error(it.message)
                    }

                    is ViewData.Loading -> {
                        _usersViewStatus.value = ViewData.Loading()
                    }

                    is ViewData.Success -> {
                        val newUsers = it.data?.data?.docs?.map { it.toUser() }
                        if (newUsers != null) {
                            val updatedPosts = _users.value + newUsers
                            _users.value = updatedPosts
                        }
                        _param.value = _param.value.nextParam()
                        _isAllLoaded.value = newUsers?.isEmpty() ?: true
                        _usersViewStatus.value = ViewData.Success()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}