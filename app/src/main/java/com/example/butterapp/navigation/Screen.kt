package com.example.butterapp.navigation

sealed class Screen(
    val route: String,
    val objectPath: String = ""
) {
    data object HomeScreen : Screen("home_screen")
    data object ProfileScreen : Screen("profile_screen")
    data object SearchScreen : Screen("search_screen")
}