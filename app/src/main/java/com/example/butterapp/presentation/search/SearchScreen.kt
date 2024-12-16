package com.example.butterapp.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.butterapp.navigation.Screen
import com.example.butterapp.presentation.search.component.UserItem
import com.example.butterapp.shared_component.ErrorComponent
import com.example.butterapp.shared_component.VerticalGap
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val users by viewModel.users.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isAllLoaded by viewModel.isAllLoaded.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
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
        viewModel.onBuild()
    }
    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }.distinctUntilChanged().filter { it }.collect {
            viewModel.loadMore()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
            VerticalGap(height = 16)
            if (users.isNotEmpty()) {
                PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
                    scope.launch {
                        viewModel.onRefresh()
                    }
                }) {
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
                }
            } else if (errorMessage.isNotEmpty()) {
                ErrorComponent(
                    errorMessage = errorMessage,
                    onRetry = {
                        viewModel.getUsers()
                    }
                )
            } else if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}