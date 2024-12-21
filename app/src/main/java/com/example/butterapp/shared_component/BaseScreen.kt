package com.example.butterapp.shared_component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseScreen(
    onBack: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (onBack != null) {
                TopAppBarComponent(
                    onClick = {
                        onBack()
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            content()
        }
    }
}