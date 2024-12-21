package com.example.butterapp.presentation.auth.register.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.RegexCollection
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUsernameViewModel @Inject constructor(
    private val authRepo: AuthRepository,
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username get() = _username.asStateFlow()

    private val _userViewStatus = MutableStateFlow(ViewData<Unit>())
    val userViewStatus get() = _userViewStatus.asStateFlow()

    private val _usernameErrorMessage = MutableStateFlow("")
    val usernameErrorMessage get() = _usernameErrorMessage.asStateFlow()

    @OptIn(FlowPreview::class)
    fun updateUsername(input: String) {
        _username.update { input }
        _userViewStatus.update { ViewData.Initial() }
        if (_username.value.isNotEmpty()) {
            if (input.length <= 4) {
                _usernameErrorMessage.update { "Min 5 chars" }
            }
            else if (RegexCollection.emailRegex.matches(_username.value)) {
                _usernameErrorMessage.update { "" }
            } else {
                _usernameErrorMessage.update { "Username only allowed letters (a-z, A-Z), numbers, underscores periods" }
            }
        }
        _username
            .debounce(500)
            .onEach {
                if (it.length > 4 &&
                    !_userViewStatus.value.isLoading &&
                    _usernameErrorMessage.value.isEmpty()
                ) {
                    getUserByUsername()
                }
            }
            .launchIn(viewModelScope)
    }


    private fun getUserByUsername() {
        viewModelScope.launch {
            authRepo.getUserByUsername(_username.value).onEach {
                when (it) {
                    is ViewData.Error -> {
                        val message = it.message
                        if (message == "record not found") {
                            _userViewStatus.update { ViewData.Success() }
                        } else {
                            _userViewStatus.update { ViewData.Error(message) }
                        }
                    }

                    is ViewData.Loading -> {
                        _userViewStatus.update { ViewData.Loading() }
                    }

                    is ViewData.Success -> {
                        _userViewStatus.update { ViewData.Error("Username already exists, choose another one") }
                    }
                }
            }.launchIn(viewModelScope)
        }

        _userViewStatus
            .onEach { viewData ->
                if (viewData.isError) {
                    _usernameErrorMessage.update { viewData.message }
                } else {
                    _usernameErrorMessage.update { "" }
                }
            }
            .launchIn(viewModelScope)
    }
}