package com.example.butterapp.presentation.auth.register.completion

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.butterapp.common.RegexCollection
import com.example.butterapp.common.ViewData
import com.example.butterapp.data.remote.auth.model.SignUpRequest
import com.example.butterapp.data.remote.post.model.ParamGetPosts
import com.example.butterapp.data.repository.AuthRepository
import com.example.butterapp.domain.post.PostScreenType
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
class RegisterCompletionViewModel @Inject constructor(
    private val authRepo: AuthRepository,
) : ViewModel() {
    private val _username = mutableStateOf("")

    private val _name = MutableStateFlow("")
    val name get() = _name.asStateFlow()
    private val _nameErrorMessage = MutableStateFlow("")
    val nameErrorMessage get() = _nameErrorMessage.asStateFlow()

    private val _email = MutableStateFlow("")
    val email get() = _email.asStateFlow()
    private val _emailErrorMessage = MutableStateFlow("")
    val emailErrorMessage get() = _emailErrorMessage.asStateFlow()

    private val _password = MutableStateFlow("")
    val password get() = _password.asStateFlow()
    private val _passwordErrorMessage = MutableStateFlow("")
    val passwordErrorMessage get() = _passwordErrorMessage.asStateFlow()

    private val _signUpViewStatus = MutableStateFlow(ViewData<Unit>())
    val signUpViewStatus get() = _signUpViewStatus.asStateFlow()

    fun onBuild(username: String) {
        _username.value = username
    }

    fun updateName(input: String) {
        _name.update { input }
        if (_name.value.isEmpty()) {
            _nameErrorMessage.update { "Name should not be empty" }
        } else {
            _nameErrorMessage.update { "" }
        }
    }

    fun updateEmail(input: String) {
        _email.update { input }
        if (_email.value.isNotEmpty()) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
                _emailErrorMessage.update { "" }
            } else {
                _emailErrorMessage.update { "Format email invalid" }
            }
        } else {
            _emailErrorMessage.update { "Email should not be empty" }
        }
    }

    fun updatePassword(input: String) {
        _password.update { input }
        if (_password.value.isNotEmpty()) {
            if (_password.value.length < 8) {
                _passwordErrorMessage.update { "Password length min is 8 chars" }
            } else {
                _passwordErrorMessage.update { "" }
            }
        } else {
            _passwordErrorMessage.update { "Password should not be empty" }
        }
    }

    fun isDataValid(): Boolean {
        val isAllFilled =
            _name.value.isNotEmpty() && _email.value.isNotEmpty() && _password.value.isNotEmpty()
        val isNoErrorMessage =
            _nameErrorMessage.value.isEmpty() && _emailErrorMessage.value.isEmpty() && _passwordErrorMessage.value.isEmpty()

        return isAllFilled && isNoErrorMessage
    }

    fun signUp() {
        viewModelScope.launch {
            authRepo.signUp(
                SignUpRequest(
                    username = _username.value,
                    password = _password.value,
                    email = _email.value,
                    name = _name.value,
                )
            ).onEach {
                when (it) {
                    is ViewData.Error -> {
                        val message = it.message
                        _signUpViewStatus.update { ViewData.Error(message) }
                    }

                    is ViewData.Loading -> {
                        _signUpViewStatus.update { ViewData.Loading() }
                    }

                    is ViewData.Success -> {
                        _signUpViewStatus.update { ViewData.Success() }
                    }
                }
            }.launchIn(viewModelScope)
        }

//        _userViewData.onEach { viewData ->
//            if (viewData.isError) {
//                _usernameErrorMessage.update { viewData.message }
//            } else {
//                _usernameErrorMessage.update { "" }
//            }
//        }.launchIn(viewModelScope)
    }
}