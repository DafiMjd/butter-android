package com.example.butterapp.presentation.auth.register.completion

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.butterapp.navigation.Screen
import com.example.butterapp.shared_component.BaseScreen
import com.example.butterapp.shared_component.GuestScreen
import com.example.butterapp.shared_component.MyOutlinedTextField
import com.example.butterapp.shared_component.PrimaryButton
import com.example.butterapp.shared_component.VerticalGap

@Composable
fun RegisterCompletionScreen(
    navController: NavHostController,
    username: String,
) {
    val viewModel = hiltViewModel<RegisterCompletionViewModel>()
    val name by viewModel.name.collectAsState()
    val nameErrorMessage by viewModel.nameErrorMessage.collectAsState()
    val email by viewModel.email.collectAsState()
    val emailErrorMessage by viewModel.emailErrorMessage.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordErrorMessage by viewModel.passwordErrorMessage.collectAsState()
    val signUpViewStatus by viewModel.signUpViewStatus.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var isShowDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.onBuild(username)
    }

    val context = LocalContext.current
    LaunchedEffect(signUpViewStatus) {
        if (signUpViewStatus.isError) {
            Toast.makeText(
                context, signUpViewStatus.message, Toast.LENGTH_LONG,
            ).show()
        }
        if (signUpViewStatus.isSuccess) {
            isShowDialog = true
        }
    }

    BaseScreen(onBack = {
        navController.popBackStack()
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Almost There",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
            VerticalGap(height = 16)
            Text(
                text = "Complete Sign up by filling these datas",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            VerticalGap(height = 16)
            MyOutlinedTextField(
                value = name,
                label = "Name",
                onValueChange = { text ->
                    viewModel.updateName(text)
                },
                errorMessage = nameErrorMessage,
                modifier = Modifier
                    .fillMaxWidth()
            )
            VerticalGap(height = 16)
            MyOutlinedTextField(
                value = email,
                label = "Email",
                onValueChange = { text ->
                    viewModel.updateEmail(text)
                },
                errorMessage = emailErrorMessage,
                modifier = Modifier
                    .fillMaxWidth()
            )
            VerticalGap(height = 16)
            MyOutlinedTextField(
                value = password,
                label = "Password",
                onValueChange = { text ->
                    viewModel.updatePassword(text)
                },
                errorMessage = passwordErrorMessage,
                passwordVisible = passwordVisible,
                suffix = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
            VerticalGap(height = 8)
            PrimaryButton(
                text = "Create Account",
                loading = signUpViewStatus.isLoading,
                enabled = viewModel.isDataValid(),
                onClick = {
                    viewModel.signUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if (isShowDialog) {
            Dialog(
                onDismissRequest = { isShowDialog = false },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Welcome to Butter",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        VerticalGap(height = 16)
                        Button(
                            onClick = {
                                isShowDialog = false
                                navController.navigate(
                                    route = Screen.HomeScreen.route
                                ) {
                                    popUpTo(0)
                                }
                            },
                        ) {
                            Text(
                                text = "Continue"
                            )
                        }
                    }
                }
            }
        }
    }

}

