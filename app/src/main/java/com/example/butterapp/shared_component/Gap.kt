package com.example.butterapp.shared_component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalGap(
    height: Int
) {
    Spacer(modifier = Modifier.height(height.dp))
}

@Composable
fun HorizontalGap(
    width: Int
) {
    Spacer(modifier = Modifier.width(width.dp))
}