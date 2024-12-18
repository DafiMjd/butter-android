package com.example.butterapp.presentation.user_detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.butterapp.common.ViewData
import com.example.butterapp.domain.connection.ConnectionType
import com.example.butterapp.navigation.Screen
import com.example.butterapp.presentation.user_detail.view_model.ConnectionViewModel
import com.example.butterapp.shared_component.ErrorComponent
import com.example.butterapp.shared_component.user.UserItem
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun UserDetailUsersComponent(
    viewModel: ConnectionViewModel,
    type: ConnectionType,
    navController: NavController,
) {
    val users by viewModel.users.collectAsState()
    val usersViewStatus by viewModel.usersViewStatus.collectAsState()
    val isLoading = usersViewStatus is ViewData.Loading
    val listState = rememberLazyListState()
    val isAllLoaded by viewModel.isAllLoaded.collectAsState()
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
    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }.distinctUntilChanged().filter { it }.collect {
            viewModel.loadMore()
        }
    }

    Box {
        if (users.isNotEmpty()) {
            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(users.size) { index ->
                    val user = users[index]
                    UserItem(
                        user,
                        onClick = {
                            navController.navigate(
                                Screen.UserDetailScreen.route.plus(
                                    "/${user.id}/${user.username}/${user.name}"
                                )
                            )
                        }
                    )
                }
                if (isLoading) {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        } else if (usersViewStatus is ViewData.Error) {
            ErrorComponent(
                errorMessage = usersViewStatus.message,
                onRetry = {
                    viewModel.getConnections()
                },
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Text(
                text = "No ${type.text}",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}