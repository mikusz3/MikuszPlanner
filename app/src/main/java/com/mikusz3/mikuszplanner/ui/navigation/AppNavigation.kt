package com.mikusz3.mikuszplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mikusz3.mikuszplanner.ui.screens.HomeScreen
import com.mikusz3.mikuszplanner.ui.screens.TaskDetailScreen
import com.mikusz3.mikuszplanner.ui.screens.ThemePickerScreen
import com.mikusz3.mikuszplanner.viewmodel.TaskViewModel
import com.mikusz3.mikuszplanner.viewmodel.ThemeViewModel

object Routes {
    const val HOME = "home"
    const val TASK_DETAIL = "task/{taskId}"
    const val THEMES = "themes"

    fun taskDetail(taskId: Long) = "task/$taskId"
}

@Composable
fun AppNavigation(
    taskViewModel: TaskViewModel,
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()
    val uiState by taskViewModel.uiState.collectAsState()
    val apiKey by taskViewModel.apiKey.collectAsState()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                uiState = uiState,
                onTaskClick = { taskId -> navController.navigate(Routes.taskDetail(taskId)) },
                onCompleteTask = { task -> taskViewModel.completeTask(task) },
                onDeleteTask = { task -> taskViewModel.deleteTask(task) },
                onAddTask = { title -> taskViewModel.addTask(title) },
                onClearCelebration = { taskViewModel.clearCelebration() },
                onThemeClick = { navController.navigate(Routes.THEMES) }
            )
        }

        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: return@composable
            val taskWithSubTasks = uiState.tasks.find { it.task.id == taskId }

            TaskDetailScreen(
                taskWithSubTasks = taskWithSubTasks,
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onCompleteTask = { task -> taskViewModel.completeTask(task) },
                onAddSubTask = { id, title -> taskViewModel.addSubTask(id, title) },
                onCompleteSubTask = { sub -> taskViewModel.completeSubTask(sub) },
                onUncompleteSubTask = { sub -> taskViewModel.uncompleteSubTask(sub) },
                onDeleteSubTask = { sub -> taskViewModel.deleteSubTask(sub) },
                onGenerateAI = { id, title -> taskViewModel.generateSubTasksWithAI(id, title) },
                onClearCelebration = { taskViewModel.clearCelebration() },
                onClearAiError = { taskViewModel.clearAiError() }
            )
        }

        composable(Routes.THEMES) {
            val currentTheme by themeViewModel.currentTheme.collectAsState()
            ThemePickerScreen(
                currentTheme = currentTheme,
                currentApiKey = apiKey,
                onThemeSelect = { theme ->
                    themeViewModel.setTheme(theme)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
                onSaveApiKey = { key -> taskViewModel.saveApiKey(key) }
            )
        }
    }
}
