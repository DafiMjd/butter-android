package com.example.butterapp.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.butterapp.navigation.Screen
import com.example.butterapp.shared_component.ErrorComponent
import com.example.butterapp.shared_component.user.UserDetailHeaderComponent
import com.example.butterapp.shared_component.GuestScreen
import com.example.butterapp.shared_component.PrimaryButton
import com.example.butterapp.shared_component.VerticalGap
import com.example.butterapp.shared_component.post.PostItem
import com.example.butterapp.shared_component.user.UserDetailConnectionBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
) {
    var isShowBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ProfileViewModel>()
    val userViewData by viewModel.userViewData.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val postsViewStatus by viewModel.postsViewStatus.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(userViewData) {
        if (userViewData.isError) {
            Toast.makeText(
                context, userViewData.message, Toast.LENGTH_LONG,
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (userViewData.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else if (userViewData.isError) {
            GuestScreen(navController)
        } else if (userViewData.isSuccess) {
            val user = userViewData.data!!

            Scaffold(
                contentWindowInsets = WindowInsets(0.dp)
            ) { contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = {
                            viewModel.onRefresh()
                        },
                    ) {
                        LazyColumn {
                            item {
                                VerticalGap(height = 24)
                            }
                            item {
                                UserDetailHeaderComponent(
                                    user = user,
                                    userViewStatus = userViewData,
                                    onTapConnection = {
                                        isShowBottomSheet = true
                                    }
                                )
                            }
                            item {
                                Column {
                                    VerticalGap(height = 16)
                                    PrimaryButton(
                                        text = "Edit Profile",
                                        onClick = { /*TODO*/ },
                                        secondary = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )
                                    VerticalGap(height = 16)
                                    HorizontalDivider(thickness = 1.dp)
                                    VerticalGap(height = 16)
                                }
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
                                if (postsViewStatus.isLoading) {
                                    item {
                                        CircularProgressIndicator()
                                    }
                                }
                            } else if (postsViewStatus.isError) {
                                item {
                                    ErrorComponent(
                                        errorMessage = postsViewStatus.message,
                                        onRetry = {
                                            viewModel.onRefresh()
                                        }
                                    )
                                }
                            } else if (postsViewStatus.isLoading) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            } else {
                                item {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
                                    ) {
                                        Text(
                                            text = "You have no post yet",
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                        VerticalGap(height = 8)
                                        PrimaryButton(
                                            text = "Create Post",
                                            onClick = {
                                                /* TODO */
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
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

    }


}