package com.example.butterapp.presentation.navigation_bar

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavBarViewModel @Inject constructor(): ViewModel()  {
    val selectedIndex = mutableIntStateOf(0)
}