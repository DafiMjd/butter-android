package com.example.butterapp.presentation.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.butterapp.domain.post.PostScreenType
import com.example.butterapp.shared_component.post.PostItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    type: PostScreenType,
    viewModel: PostViewModel
) {
    val posts by viewModel.posts.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        if (posts.isNotEmpty()) {
            PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
                scope.launch {
                    viewModel.onRefresh()
                }
            }) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(posts.size) { index ->
                        val post = posts[index]
                        val isLastItem = index == posts.size - 1
                        if (isLastItem) {
                            PostItem(
                                post = post,
                            )
                        } else {
                            Column {
                                PostItem(
                                    post = post,
                                )
                                HorizontalDivider(thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        } else if (errorMessage.isNotEmpty()) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                Button(onClick = {
                    scope.launch {
                        viewModel.getPosts()
                    }
                }) {
                    Text(
                        text = "Retry"
                    )
                }

            }
        } else if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}