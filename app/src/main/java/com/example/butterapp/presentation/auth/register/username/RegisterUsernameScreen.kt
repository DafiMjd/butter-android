package com.example.butterapp.presentation.auth.register.username

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.butterapp.navigation.Screen
import com.example.butterapp.shared_component.BaseScreen
import com.example.butterapp.shared_component.MyOutlinedTextField
import com.example.butterapp.shared_component.PrimaryButton
import com.example.butterapp.shared_component.VerticalGap
import kotlinx.coroutines.flow.onEach

@Composable
fun RegisterUsernameScreen(
    navController: NavHostController,
) {
    val viewModel = hiltViewModel<RegisterUsernameViewModel>()
    val username by viewModel.username.collectAsState()
    val usernameErrorMessage by viewModel.usernameErrorMessage.collectAsState()
    val userViewStatus by viewModel.userViewStatus.collectAsState()

    BaseScreen(onBack = {
        navController.popBackStack()
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Create username",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
            VerticalGap(height = 16)
            Text(
                text = "Pick your best, unique, funny username. Whatever you like",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            VerticalGap(height = 16)
            MyOutlinedTextField(
                value = username,
                label = "Username",
                onValueChange = { text ->
                    viewModel.updateUsername(text)
                },
                errorMessage = usernameErrorMessage,
                suffix = {
                    if (userViewStatus.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                        )
                    } else if (userViewStatus.isSuccess) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "clear",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    } else if (username.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "clear",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    viewModel.updateUsername("")
                                }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            VerticalGap(height = 8)
            PrimaryButton(
                text = "Next",
                enabled = userViewStatus.isSuccess,
                onClick = {
                    navController.navigate(
                        route = Screen.RegisterCompletionScreen.route.plus(
                            "/$username"
                        ),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}

