package com.mikusz3.mikuszplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikusz3.mikuszplanner.ui.navigation.AppNavigation
import com.mikusz3.mikuszplanner.ui.theme.MikuszPlannerTheme
import com.mikusz3.mikuszplanner.viewmodel.TaskViewModel
import com.mikusz3.mikuszplanner.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val taskViewModel: TaskViewModel = viewModel()
            val currentTheme by themeViewModel.currentTheme.collectAsState()

            MikuszPlannerTheme(appTheme = currentTheme) {
                AppNavigation(
                    taskViewModel = taskViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
