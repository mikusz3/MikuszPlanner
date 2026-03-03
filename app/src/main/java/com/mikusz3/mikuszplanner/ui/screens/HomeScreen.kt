package com.mikusz3.mikuszplanner.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikusz3.mikuszplanner.data.model.TaskWithSubTasks
import com.mikusz3.mikuszplanner.ui.components.ConfettiOverlay
import com.mikusz3.mikuszplanner.ui.components.TaskCard
import com.mikusz3.mikuszplanner.ui.components.ThemedBackground
import com.mikusz3.mikuszplanner.ui.components.ThemedPrimaryButton
import com.mikusz3.mikuszplanner.ui.components.ThemedTopBar
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import com.mikusz3.mikuszplanner.ui.theme.LocalAppTheme
import com.mikusz3.mikuszplanner.ui.theme.NB_Ink
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Black
import com.mikusz3.mikuszplanner.viewmodel.TaskUiState
import com.mikusz3.mikuszplanner.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    uiState: TaskUiState,
    onTaskClick: (Long) -> Unit,
    onCompleteTask: (com.mikusz3.mikuszplanner.data.model.Task) -> Unit,
    onDeleteTask: (com.mikusz3.mikuszplanner.data.model.Task) -> Unit,
    onAddTask: (String) -> Unit,
    onClearCelebration: () -> Unit,
    onThemeClick: () -> Unit
) {
    val theme = LocalAppTheme.current
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        ThemedBackground {
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                ThemedTopBar(
                    title = "MikuszPlanner",
                    actions = {
                        IconButton(onClick = onThemeClick) {
                            Icon(
                                Icons.Filled.Palette,
                                contentDescription = "Themes",
                                tint = Color.White
                            )
                        }
                    }
                )

                if (uiState.tasks.isEmpty()) {
                    EmptyState(theme = theme, modifier = Modifier.weight(1f))
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                            start = if (theme == AppTheme.NOTEBOOK) 64.dp else 12.dp,
                            end = 12.dp,
                            top = 12.dp,
                            bottom = 88.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(uiState.tasks, key = { _, item -> item.task.id }) { index, item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                            ) {
                                TaskCard(
                                    taskWithSubTasks = item,
                                    onClick = { onTaskClick(item.task.id) },
                                    onComplete = { onCompleteTask(item.task) },
                                    onDelete = { onDeleteTask(item.task) },
                                    stickyIndex = index
                                )
                            }
                        }
                    }
                }
            }
        }

        // FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .imePadding()
        ) {
            when (theme) {
                AppTheme.WINDOWS_9X -> ThemedPrimaryButton("+ New Task", onClick = { showAddDialog = true })
                else -> FloatingActionButton(
                    onClick = { showAddDialog = true },
                    shape = if (theme == AppTheme.IPOD_YOUTUBE) RoundedCornerShape(14.dp) else CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task", modifier = Modifier.size(28.dp))
                }
            }
        }

        // Confetti
        ConfettiOverlay(
            isVisible = uiState.celebratingId != null,
            onFinished = onClearCelebration,
            modifier = Modifier.fillMaxSize()
        )
    }

    if (showAddDialog) {
        AddTaskDialog(
            theme = theme,
            onDismiss = { showAddDialog = false },
            onConfirm = { title ->
                onAddTask(title)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun EmptyState(theme: AppTheme, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (theme) {
                AppTheme.WINDOWS_9X -> "📁"
                AppTheme.WINDOWS_XP -> "🗂️"
                AppTheme.FRUTIGER_AERO -> "✨"
                AppTheme.NOTEBOOK -> "📓"
                AppTheme.IPOD_YOUTUBE -> "▶️"
            },
            fontSize = 56.sp
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = when (theme) {
                AppTheme.WINDOWS_9X -> "No tasks found."
                AppTheme.WINDOWS_XP -> "Your task list is empty!"
                AppTheme.FRUTIGER_AERO -> "Nothing here yet — add a task!"
                AppTheme.NOTEBOOK -> "Your notebook is empty..."
                AppTheme.IPOD_YOUTUBE -> "No videos in your playlist"
            },
            color = when (theme) {
                AppTheme.WINDOWS_9X -> Win9x_Black
                AppTheme.NOTEBOOK -> NB_Ink
                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            },
            fontSize = if (theme == AppTheme.NOTEBOOK) 18.sp else 16.sp,
            fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tap + to create your first task",
            color = when (theme) {
                AppTheme.WINDOWS_9X -> Win9x_Black.copy(alpha = 0.5f)
                AppTheme.NOTEBOOK -> NB_Ink.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            },
            fontSize = 13.sp,
            fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
        )
    }
}

@Composable
private fun AddTaskDialog(
    theme: AppTheme,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = when (theme) {
            AppTheme.WINDOWS_9X -> RoundedCornerShape(0.dp)
            AppTheme.FRUTIGER_AERO -> RoundedCornerShape(20.dp)
            AppTheme.IPOD_YOUTUBE -> RoundedCornerShape(14.dp)
            else -> RoundedCornerShape(8.dp)
        },
        containerColor = when (theme) {
            AppTheme.WINDOWS_9X -> MaterialTheme.colorScheme.surface
            AppTheme.IPOD_YOUTUBE -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surface
        },
        title = {
            Text(
                text = when (theme) {
                    AppTheme.WINDOWS_9X -> "New Task"
                    AppTheme.NOTEBOOK -> "New Task"
                    AppTheme.IPOD_YOUTUBE -> "Add to Playlist"
                    else -> "Create New Task"
                },
                fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = {
                    Text(
                        "Task title",
                        fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = when (theme) {
                    AppTheme.WINDOWS_9X -> RoundedCornerShape(0.dp)
                    AppTheme.FRUTIGER_AERO -> RoundedCornerShape(12.dp)
                    else -> RoundedCornerShape(8.dp)
                }
            )
        },
        confirmButton = {
            ThemedPrimaryButton(
                text = "Add",
                onClick = { if (title.isNotBlank()) onConfirm(title.trim()) },
                enabled = title.isNotBlank()
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                )
            }
        }
    )
}
