package com.example.butterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.butterapp.navigation.Navigation
import com.example.butterapp.navigation.Screen
import com.example.butterapp.presentation.home.HomeScreen
import com.example.butterapp.presentation.navigation_bar.BottomNavBar
import com.example.butterapp.presentation.navigation_bar.BottomNavBarViewModel
import com.example.butterapp.presentation.profile.ProfileScreen
import com.example.butterapp.shared_component.VerticalGap
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(
                            selectedIndex = selectedItemIndex.intValue,
                            onClick = { index ->
                                selectedItemIndex.intValue = index
                            },
                            navController = navController,
                        )
                    }
                ) { contentPadding ->
                    Navigation(
                        navController = navController,
                        contentPadding = contentPadding,
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ButterAppTheme {
        Greeting("Android")
    }
}