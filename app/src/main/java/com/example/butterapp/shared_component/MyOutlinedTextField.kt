package com.example.butterapp.shared_component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MyOutlinedTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    suffix: @Composable() (() -> Unit)? = null,
    singleLine: Boolean = false,
    passwordVisible: Boolean? = null,
) {
    val isError = errorMessage.isNotEmpty()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(errorMessage)
            }
        },
        suffix = suffix,
        singleLine = singleLine,
        visualTransformation =
        if (passwordVisible == null || passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = modifier,
    )
}