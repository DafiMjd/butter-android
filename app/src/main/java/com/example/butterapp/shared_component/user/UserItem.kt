package com.example.butterapp.shared_component.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.butterapp.domain.user.User
import com.example.butterapp.shared_component.GuestDialog
import com.example.butterapp.shared_component.HorizontalGap
import com.example.butterapp.shared_component.PrimaryButton
import com.example.butterapp.shared_component.VerticalGap

@Composable
fun UserItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 12.dp)
            .clickable {
                onClick()
            }
    ) {
        Column {
            Row {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "profile_pic",
                    modifier = Modifier.size(48.dp)
                )
                HorizontalGap(width = 8)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.username, fontWeight = FontWeight.W600
                    )
                    HorizontalGap(width = 4)
                    Text(
                        text = user.name, fontWeight = FontWeight.W400, color = Color.LightGray
                    )
                }
                HorizontalGap(width = 8)
                PrimaryButton(
                    text = "Follow",
                    onClick = {
                        isShowDialog = true
                    },
                    secondary = user.isFollowed,
                    padding = PaddingValues(
                        vertical = 1.dp, horizontal = 16.dp,
                    ),
                )
            }
            VerticalGap(height = 12)
            HorizontalDivider(
                thickness = 1.dp, modifier = Modifier.padding(start = 56.dp)
            )
        }
        if (isShowDialog) {
            GuestDialog {
                isShowDialog = false
            }
        }
    }
}