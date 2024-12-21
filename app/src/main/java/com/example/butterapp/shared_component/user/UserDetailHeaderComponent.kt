package com.example.butterapp.shared_component.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.butterapp.common.ViewData
import com.example.butterapp.domain.user.User
import com.example.butterapp.shared_component.HorizontalGap
import com.example.butterapp.shared_component.VerticalGap

@Composable
fun <T>UserDetailHeaderComponent(
    user: User,
    userViewStatus: ViewData<T>,
    onTapConnection: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                VerticalGap(height = 16)
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineLarge
                )
                VerticalGap(height = 4)
                Text(
                    text = user.username,
                )
                if (userViewStatus.isSuccess) {
                    VerticalGap(height = 16)
                    Row(
                        modifier = Modifier.clickable {
                            if (user.hasConnection) {
                                onTapConnection()
                            }
                        }
                    ) {
                        Text(
                            text = "${user.followersCount} followers",
                            fontWeight = FontWeight.W400,
                            color = Color.LightGray
                        )
                        HorizontalGap(width = 8)
                        Text(
                            text = "${user.followingsCount} followings",
                            fontWeight = FontWeight.W400,
                            color = Color.LightGray
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "profile_pic",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}