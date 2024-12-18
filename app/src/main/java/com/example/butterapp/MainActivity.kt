package com.example.butterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.butterapp.navigation.Navigation
import com.example.butterapp.navigation.Screen
import com.example.butterapp.presentation.navigation_bar.BottomNavBar
import com.example.butterapp.presentation.navigation_bar.BottomNavBarViewModel
import com.example.butterapp.ui.theme.ButterAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ButterAppTheme {
                val bottomNavBarViewModel = hiltViewModel<BottomNavBarViewModel>()
                val selectedItemIndex = rememberSaveable {
                    bottomNavBarViewModel.selectedIndex
                }
                val navController = rememberNavController()
                val isShowBottomBar =
                    navController.currentBackStackEntryAsState().value?.destination?.route in Screen.mainScreen.map { it.route }

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    if (isShowBottomBar) {
                        BottomNavBar(
                            selectedIndex = selectedItemIndex.intValue,
                            onClick = { index ->
                                selectedItemIndex.intValue = index
                            },
                            navController = navController,
                        )
                    }
                }) { contentPadding ->
                    Box(modifier = Modifier.padding(contentPadding)) {
                        Navigation(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ButterAppTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Butter",
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
            },
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}