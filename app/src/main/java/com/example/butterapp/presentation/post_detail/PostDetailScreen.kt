package com.example.butterapp.presentation.post_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.butterapp.common.helper.DateHelper
import com.example.butterapp.navigation.Screen
import com.example.butterapp.shared_component.ErrorComponent
import com.example.butterapp.shared_component.HorizontalGap
import com.example.butterapp.shared_component.TopAppBarComponent
import com.example.butterapp.shared_component.VerticalGap

@Composable
fun PostDetail(
    navController: NavHostController,
    postId: String,
) {
    val viewModel = hiltViewModel<PostDetailViewModel>()
    val post by viewModel.post.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(true) {
        viewModel.onBuild(postId)
    }

    Scaffold(
        topBar = {
            TopAppBarComponent(
                onClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (errorMessage.isNotEmpty()) {
                ErrorComponent(
                    errorMessage = errorMessage,
                    onRetry = {
                        viewModel.getPost()
                    },
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                if (post.user != null) {
                                    navController.navigate(
                                        Screen.UserDetailScreen.route.plus(
                                            "/${post.user!!.id}/${post.user!!.username}/${post.user!!.name}"
                                        ),
                                    )
                                }
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "profile_pic",
                            modifier = Modifier.size(48.dp)
                        )
                        HorizontalGap(width = 8)
                        Text(
                            text = post.user?.username ?: "",
                            fontWeight = FontWeight.W600
                        )
                        HorizontalGap(width = 8)
                        Text(
                            text = DateHelper.toPostTimeStamp(post.createdAt),
                            fontWeight = FontWeight.W400,
                            color = Color.LightGray
                        )
                    }
                    VerticalGap(height = 16)
                    Text(
                        text = post.content
                    )
                }
            }
        }
    }
}