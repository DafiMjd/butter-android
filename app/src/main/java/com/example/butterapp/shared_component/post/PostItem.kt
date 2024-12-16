package com.example.butterapp.shared_component.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.butterapp.common.helper.DateHelper
import com.example.butterapp.domain.post.Post
import com.example.butterapp.shared_component.HorizontalGap
import com.example.butterapp.shared_component.VerticalGap

@Composable
fun PostItem(
    post: Post,
    onClick: () -> Unit,
    onClickProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .clickable {
                onClick()
            }
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "profile_pic",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onClickProfile()
                    }

            )
            HorizontalGap(width = 8)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = post.user?.username ?: "",
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .clickable {
                                onClickProfile()
                            }
                    )
                    HorizontalGap(width = 8)
                    Text(
                        text = DateHelper.toPostTimeStamp(post.createdAt),
                        fontWeight = FontWeight.W400,
                        color = Color.LightGray
                    )
                }
                VerticalGap(height = 8)
                Text(
                    text = post.content
                )
            }
        }
    }
}