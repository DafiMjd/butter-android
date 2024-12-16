package com.example.butterapp.shared_component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.butterapp.ui.theme.ColorPrimary
import com.example.butterapp.ui.theme.DarkGray

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = ButtonDefaults.ContentPadding,
    secondary: Boolean = false,
) {
    val type = if (secondary) {
        ButtonType.SECONDARY
    } else {
        ButtonType.PRIMARY
    }

    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(12.dp),
        contentPadding = padding,
        border = type.borderStroke,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = type.bgColor,
            contentColor = type.fgColor,
        ),
        modifier = modifier,
    ) {
        Text(
            text = text
        )
    }
}

enum class ButtonType(
    val bgColor: Color,
    val fgColor: Color,
    val borderStroke: BorderStroke? = null,
) {
    PRIMARY(
        bgColor = ColorPrimary,
        fgColor = DarkGray,
    ),
    SECONDARY(
        bgColor = DarkGray,
        fgColor = ColorPrimary,
        borderStroke = BorderStroke(1.dp, ColorPrimary),
    )
}