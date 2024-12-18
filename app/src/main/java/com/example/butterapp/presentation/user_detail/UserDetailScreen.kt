package com.example.butterapp.presentation.user_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.butterapp.common.ViewData
import com.example.butterapp.navigation.Screen
import com.example.butterapp.presentation.user_detail.component.UserDetailConnectionBottomSheet
import com.example.butterapp.presentation.user_detail.component.UserDetailHeaderComponent
import com.example.butterapp.presentation.user_detail.view_model.UserDetailViewModel
import com.example.butterapp.shared_component.ErrorComponent
import com.example.butterapp.shared_component.TopAppBarComponent
import com.example.butterapp.shared_component.post.PostItem
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    navController: NavHostController,
    userId: String,
    username: String,
    name: String,
) {
    var isShowBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<UserDetailViewModel>()
    val user by viewModel.user.collectAsState()
    val userViewStatus by viewModel.userViewStatus.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val scope = rememberCoroutineScope()
    val isAllLoaded by viewModel.isAllLoaded.collectAsState()
    val postsViewStatus by viewModel.postsViewStatus.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val listState = rememberLazyListState()
    val isLoading = postsViewStatus is ViewData.Loading
    val shouldLoadMore = remember {
        derivedStateOf {
            if (isAllLoaded) {
                false
            } else {
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                val lastVisibleItemIndex =
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleItemIndex >= (totalItemsCount - 2) && !isLoading
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.onBuild(userId, username, name)
    }

    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadMore()
            }
    }

    Scaffold(
        topBar = {
            TopAppBarComponent(
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
                scope.launch {
                    viewModel.onRefresh()
                }
            }) {
                LazyColumn {
                    item {
                        UserDetailHeaderComponent(
                            user = user,
                            userViewStatus = userViewStatus,
                            onTapConnection = {
                                isShowBottomSheet = true
                            }
                        )
                    }
                    if (posts.isNotEmpty()) {
                        items(posts.size) { index ->
                            val post = posts[index]
                            val isLastItem = index == posts.size - 1
                            if (isLastItem) {
                                PostItem(
                                    post = post,
                                    onClick = {
                                        navController.navigate(
                                            Screen.PostDetailScreen.route.plus(
                                                "/${post.id}"
                                            )
                                        )
                                    },
                                    onClickProfile = {
                                    }
                                )
                            } else {
                                Column {
                                    PostItem(
                                        post = post,
                                        onClick = {
                                            navController.navigate(
                                                Screen.PostDetailScreen.route.plus(
                                                    "/${post.id}"
                                                )
                                            )
                                        },
                                        onClickProfile = {

                                        }
                                    )
                                    HorizontalDivider(thickness = 1.dp)
                                }
                            }
                        }
                        if (isLoading) {
                            item {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (postsViewStatus is ViewData.Error) {
                        item {
                            ErrorComponent(
                                errorMessage = postsViewStatus.message,
                                onRetry = {
                                    viewModel.onRefresh()
                                }
                            )
                        }
                    } else if (isLoading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            if (isShowBottomSheet) {
                UserDetailConnectionBottomSheet(
                    userId = user.id,
                    navController = navController,
                    onDismissRequest = {
                        isShowBottomSheet = false
                    }
                )
            }
        }
    }

}