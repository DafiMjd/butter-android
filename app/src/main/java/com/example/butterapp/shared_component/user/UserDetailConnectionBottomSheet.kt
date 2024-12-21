package com.example.butterapp.shared_component.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.butterapp.domain.connection.ConnectionType
import com.example.butterapp.shared_component.user.view_model.ConnectionViewModel
import com.example.butterapp.ui.theme.DarkGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailConnectionBottomSheet(
    userId: String,
    onDismissRequest: () -> Unit,
    navController: NavController,
) {
    val connectionViewModel = hiltViewModel<ConnectionViewModel>(key = userId)
    val tabItems = listOf(
        ConnectionType.FOLLOWER, ConnectionType.FOLLOWING
    )
    val selectedTabIndex = rememberSaveable {
        connectionViewModel.selectedTabIndex
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex.intValue) {
        pagerState.animateScrollToPage(selectedTabIndex.intValue)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex.intValue = pagerState.currentPage
    }

    val followerViewModel = hiltViewModel<ConnectionViewModel>(
        key = ConnectionType.FOLLOWER.text,
    )
    val followingViewModel = hiltViewModel<ConnectionViewModel>(
        key = ConnectionType.FOLLOWING.text,
    )

    LaunchedEffect(true) {
        followerViewModel.onBuild(ConnectionType.FOLLOWER, userId)
        followingViewModel.onBuild(ConnectionType.FOLLOWING, userId)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(
            containerColor = DarkGray,
            selectedTabIndex = selectedTabIndex.intValue,
        ) {
            tabItems.forEachIndexed { index, type ->
                Tab(
                    selected = selectedTabIndex.intValue == index,
                    onClick = {
                        selectedTabIndex.intValue = index
                    },
                    text = {
                        Text(text = type.text)
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val viewModel = if (index == 0) {
                    followerViewModel
                } else {
                    followingViewModel
                }

                UserDetailUsersComponent(
                    viewModel = viewModel,
                    type = tabItems[index],
                    navController = navController,
                )
            }
        }
    }
}