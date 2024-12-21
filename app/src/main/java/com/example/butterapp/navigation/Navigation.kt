package com.example.butterapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.butterapp.presentation.auth.register.completion.RegisterCompletionScreen
import com.example.butterapp.presentation.auth.register.username.RegisterUsernameScreen
import com.example.butterapp.presentation.home.HomeScreen
import com.example.butterapp.presentation.post_detail.PostDetail
import com.example.butterapp.presentation.profile.ProfileScreen
import com.example.butterapp.presentation.search.SearchScreen
import com.example.butterapp.presentation.user_detail.UserDetailScreen

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
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
            ProfileScreen(navController)
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
        composable(
            Screen.RegisterUsernameScreen.route
        ) {
            RegisterUsernameScreen(navController)
        }
        composable(
            Screen.RegisterCompletionScreen.route.plus(Screen.RegisterCompletionScreen.objectPath),
        ) {
            val username = it.arguments?.getString(Screen.RegisterCompletionScreen.objectName)
            username?.let {
                RegisterCompletionScreen(
                    navController = navController, username
                )
            }
        }
    }
}