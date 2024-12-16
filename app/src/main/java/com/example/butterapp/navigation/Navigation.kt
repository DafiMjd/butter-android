package com.example.butterapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.butterapp.presentation.home.HomeScreen
import com.example.butterapp.presentation.post_detail.PostDetail
import com.example.butterapp.presentation.profile.ProfileScreen
import com.example.butterapp.presentation.search.SearchScreen
import com.example.butterapp.presentation.user_detail.UserDetailScreen

@Composable
fun Navigation(
    navController: NavHostController,
//    contentPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
//        modifier = Modifier
//            .padding(contentPadding)
    ) {
        composable(
            Screen.HomeScreen.route
        ) {
            HomeScreen(navController)
        }
        composable(
            Screen.SearchScreen.route
        ) {
            SearchScreen(
                navController,
            )
        }
        composable(
            Screen.ProfileScreen.route
        ) {
            ProfileScreen()
        }
        composable(
            Screen.PostDetailScreen.route.plus(Screen.PostDetailScreen.objectPath),
        ) {
            val postId = it.arguments?.getString(Screen.PostDetailScreen.objectName)
            postId?.let {
                PostDetail(
                    navController = navController, postId
                )
            }
        }
        composable(
            Screen.UserDetailScreen.route.plus(Screen.UserDetailScreen.objectPath),
        ) {
            val userId = it.arguments?.getString("userId")
            val username = it.arguments?.getString("username")
            val name = it.arguments?.getString("name")
            userId?.let {
                UserDetailScreen(
                    navController,
                    userId,
                    username ?: "",
                    name ?: "",
                )
            }
        }
    }
}