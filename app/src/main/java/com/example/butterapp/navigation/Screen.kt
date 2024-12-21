package com.example.butterapp.navigation

sealed class Screen(
    val route: String,
    val objectName: String = "",
    val objectPath: String = "",
) {
    data object HomeScreen : Screen("home_screen")
    data object ProfileScreen : Screen("profile_screen")
    data object SearchScreen : Screen("search_screen")
    data object PostDetailScreen : Screen(
        "post_detail_screen",
        objectName = "postId",
        objectPath = "/{postId}",
    )
    data object UserDetailScreen : Screen(
        "user_detail_screen",
        objectPath = "/{userId}/{username}/{name}",
    )
    data object RegisterUsernameScreen : Screen(
        "register_username_screen",
    )
    data object RegisterCompletionScreen : Screen(
        "register_completion_screen",
        objectName = "username",
        objectPath = "/{username}",
    )

    companion object {
        val mainScreen = listOf(
            HomeScreen,
            ProfileScreen,
            SearchScreen
        )
    }
}