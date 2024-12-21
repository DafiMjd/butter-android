package com.example.butterapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.butterapp.domain.post.PostScreenType
import com.example.butterapp.presentation.post.PostScreen
import com.example.butterapp.presentation.post.PostViewModel
import com.example.butterapp.shared_component.GuestScreen

@Composable
fun HomeScreen(
    navController: NavController,
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val tabItems = listOf(
            "All", "Following"
        )
        val selectedTabIndex = rememberSaveable {
            homeViewModel.selectedTabIndex
        }
        val pagerState = rememberPagerState {
            tabItems.size
        }
        val allViewModel = hiltViewModel<PostViewModel>(key = "all")
        val followingViewModel = hiltViewModel<PostViewModel>(key = "following")
        val userViewData by followingViewModel.userViewData.collectAsState()

        LaunchedEffect(selectedTabIndex.intValue) {
            pagerState.animateScrollToPage(selectedTabIndex.intValue)
        }
        LaunchedEffect(pagerState.currentPage) {
            selectedTabIndex.intValue = pagerState.currentPage
        }
        LaunchedEffect(true) {
            allViewModel.onBuild(PostScreenType.ALL)
            followingViewModel.onBuild(PostScreenType.FOLLOWING)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Butter",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .height(60.dp)
                    .padding(16.dp)
            )
            TabRow(selectedTabIndex = selectedTabIndex.intValue) {
                tabItems.forEachIndexed { index, s ->
                    Tab(
                        selected = selectedTabIndex.intValue == index,
                        onClick = {
                            selectedTabIndex.intValue = index
                        },
                        text = {
                            Text(text = s)
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
                    if (index == 0) {
                        PostScreen(
                            navController = navController,
                            viewModel = allViewModel,
                        )
                    } else {
                        if (userViewData.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                            )
                        } else if (userViewData.isError) {
                            GuestScreen(navController)
                        } else if (userViewData.isSuccess) {
                            val user = userViewData.data
                            PostScreen(
                                navController = navController,
                                viewModel = followingViewModel,
                                user = user,
                            )
                        }
                    }
                }
            }
        }
    }
}
