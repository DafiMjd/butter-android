package com.example.butterapp.shared_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.butterapp.navigation.Screen

@Composable
fun GuestScreen(
    navController: NavController,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                navController.navigate(
                    route = Screen.RegisterUsernameScreen.route
                )
            },
        ) {
            Text(
                text = "Create Your Account"
            )
        }
        Text(
            text = "Login",
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable {
                }
        )
    }
}