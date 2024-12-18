package com.example.butterapp.common.helper

import androidx.navigation.NavOptions

class NavigationHelper {
    companion object {
        fun defaultNavOption(): NavOptions {
            return NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true).build()
        }
    }
}