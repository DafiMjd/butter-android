package com.example.butterapp.shared_component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.butterapp.ui.theme.ColorPrimary
import com.example.butterapp.ui.theme.ColorPrimaryDisabled
import com.example.butterapp.ui.theme.DarkGray
import com.example.butterapp.ui.theme.DarkGrayDisabled

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = ButtonDefaults.ContentPadding,
    secondary: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val type = if (secondary) {
        ButtonType.SECONDARY
    } else {
        ButtonType.PRIMARY
    }

    Button(
        onClick = {
            if (enabled && !loading) {
                onClick()
            }
        },
        shape = RoundedCornerShape(12.dp),
        contentPadding = padding,
        border = type.getBorderStroke(enabled),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = type.getBgColor(enabled),
            contentColor = type.getFgColor(enabled),
        ),
        modifier = modifier,
    ) {
        if (loading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = text
            )
        }
    }
}

fun getBgColor(enabled: Boolean, type: ButtonType) {

}


enum class ButtonType(
    val bgColor: Color,
    val fgColor: Color,
    val borderStroke: BorderStroke? = null,
) {
    PRIMARY(
        bgColor = ColorPrimary,
        fgColor = DarkGray,
    ) {
        override fun getBgColor(enabled: Boolean): Color {
            return if (enabled) bgColor else ColorPrimaryDisabled
        }

        override fun getFgColor(enabled: Boolean): Color {
            return if (enabled) fgColor else DarkGrayDisabled
        }

        override fun getBorderStroke(enabled: Boolean): BorderStroke? = null
    },
    SECONDARY(
        bgColor = DarkGray,
        fgColor = ColorPrimary,
        borderStroke = BorderStroke(1.dp, ColorPrimary),
    ) {
        override fun getBgColor(enabled: Boolean): Color {
            return bgColor
        }

        override fun getFgColor(enabled: Boolean): Color {
            return if (enabled) fgColor else ColorPrimaryDisabled
        }

        override fun getBorderStroke(enabled: Boolean): BorderStroke? {
            return if (enabled) borderStroke else BorderStroke(1.dp, ColorPrimaryDisabled)
        }
    };

    abstract fun getBgColor(enabled: Boolean): Color
    abstract fun getFgColor(enabled: Boolean): Color
    abstract fun getBorderStroke(enabled: Boolean): BorderStroke?
}