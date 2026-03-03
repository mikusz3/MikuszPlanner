package com.mikusz3.mikuszplanner.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikusz3.mikuszplanner.data.model.SubTask
import com.mikusz3.mikuszplanner.data.model.Task
import com.mikusz3.mikuszplanner.data.model.TaskWithSubTasks
import com.mikusz3.mikuszplanner.ui.components.ConfettiOverlay
import com.mikusz3.mikuszplanner.ui.components.SubTaskItem
import com.mikusz3.mikuszplanner.ui.components.ThemedBackground
import com.mikusz3.mikuszplanner.ui.components.ThemedCard
import com.mikusz3.mikuszplanner.ui.components.ThemedPrimaryButton
import com.mikusz3.mikuszplanner.ui.components.ThemedTopBar
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import com.mikusz3.mikuszplanner.ui.theme.LocalAppTheme
import com.mikusz3.mikuszplanner.ui.theme.NB_Ink
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Black
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Navy
import com.mikusz3.mikuszplanner.ui.theme.iPod_LightGray
import com.mikusz3.mikuszplanner.ui.theme.iPod_Red
import com.mikusz3.mikuszplanner.ui.theme.iPod_White
import com.mikusz3.mikuszplanner.viewmodel.TaskUiState
import kotlinx.coroutines.launch

@Composable
fun TaskDetailScreen(
    taskWithSubTasks: TaskWithSubTasks?,
    uiState: TaskUiState,
    onBack: () -> Unit,
    onCompleteTask: (Task) -> Unit,
    onAddSubTask: (Long, String) -> Unit,
    onCompleteSubTask: (SubTask) -> Unit,
    onUncompleteSubTask: (SubTask) -> Unit,
    onDeleteSubTask: (SubTask) -> Unit,
    onGenerateAI: (Long, String) -> Unit,
    onClearCelebration: () -> Unit,
    onClearAiError: () -> Unit
) {
    val theme = LocalAppTheme.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showAddSubDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.aiError) {
        uiState.aiError?.let { err ->
            scope.launch { snackbarHostState.showSnackbar(err) }
            onClearAiError()
        }
    }

    if (taskWithSubTasks == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Task not found", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    val task = taskWithSubTasks.task
    val subTasks = taskWithSubTasks.subTasks.sortedBy { it.sortOrder }

    Box(modifier = Modifier.fillMaxSize()) {
        ThemedBackground {
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                ThemedTopBar(
                    title = task.title,
                    actions = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).imePadding(),
                    contentPadding = PaddingValues(
                        start = if (theme == AppTheme.NOTEBOOK) 64.dp else 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Main task header card
                    item {
                        TaskHeaderCard(task, theme, onCompleteTask)
                    }

                    // Section header
                    item {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when (theme) {
                                    AppTheme.WINDOWS_9X -> "Steps (${subTasks.count { it.isCompleted }}/${subTasks.size})"
                                    AppTheme.NOTEBOOK -> "Steps ${subTasks.count { it.isCompleted }}/${subTasks.size}"
                                    AppTheme.IPOD_YOUTUBE -> "Episodes (${subTasks.count { it.isCompleted }}/${subTasks.size})"
                                    else -> "Sub-tasks  •  ${subTasks.count { it.isCompleted }} / ${subTasks.size} done"
                                },
                                color = when (theme) {
                                    AppTheme.WINDOWS_9X -> Win9x_Black
                                    AppTheme.NOTEBOOK -> NB_Ink
                                    AppTheme.IPOD_YOUTUBE -> iPod_LightGray
                                    else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                },
                                fontWeight = FontWeight.SemiBold,
                                fontSize = if (theme == AppTheme.WINDOWS_9X) 12.sp else 14.sp,
                                fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                    }

                    // Sub-tasks
                    if (subTasks.isEmpty()) {
                        item {
                            EmptySubTasksHint(theme)
                        }
                    } else {
                        items(subTasks, key = { it.id }) { sub ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically()
                            ) {
                                SubTaskItem(
                                    subTask = sub,
                                    onComplete = { onCompleteSubTask(sub) },
                                    onUncomplete = { onUncompleteSubTask(sub) },
                                    onDelete = { onDeleteSubTask(sub) }
                                )
                            }
                        }
                    }

                    // AI + Add buttons
                    item {
                        Spacer(Modifier.height(12.dp))
                        ActionButtons(
                            theme = theme,
                            isAiGenerating = uiState.aiGenerating,
                            onAddManual = { showAddSubDialog = true },
                            onGenerateAI = { onGenerateAI(task.id, task.title) }
                        )
                    }
                }
            }
        }

        SnackbarHost(snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))

        ConfettiOverlay(
            isVisible = uiState.celebratingId != null,
            onFinished = onClearCelebration,
            modifier = Modifier.fillMaxSize()
        )
    }

    if (showAddSubDialog) {
        AddSubTaskDialog(
            theme = theme,
            onDismiss = { showAddSubDialog = false },
            onConfirm = { title ->
                onAddSubTask(task.id, title)
                showAddSubDialog = false
            }
        )
    }
}

@Composable
private fun TaskHeaderCard(task: Task, theme: AppTheme, onComplete: (Task) -> Unit) {
    ThemedCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = when (theme) {
                            AppTheme.WINDOWS_9X -> 14.sp
                            AppTheme.NOTEBOOK -> 22.sp
                            else -> 20.sp
                        },
                        color = when (theme) {
                            AppTheme.WINDOWS_9X -> Win9x_Black
                            AppTheme.NOTEBOOK -> NB_Ink
                            AppTheme.IPOD_YOUTUBE -> iPod_White
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                    )
                    if (task.isCompleted) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "✓ Completed!",
                            color = Color(0xFF4CAF50),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                        )
                    }
                }
                if (!task.isCompleted) {
                    Spacer(Modifier.width(12.dp))
                    ThemedPrimaryButton(
                        text = when (theme) {
                            AppTheme.WINDOWS_9X -> "Done"
                            AppTheme.NOTEBOOK -> "Done!"
                            AppTheme.IPOD_YOUTUBE -> "✓"
                            else -> "Complete"
                        },
                        onClick = { onComplete(task) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptySubTasksHint(theme: AppTheme) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (theme) {
                AppTheme.NOTEBOOK -> "No steps yet. Add some below or let AI help!"
                AppTheme.IPOD_YOUTUBE -> "No episodes yet"
                AppTheme.WINDOWS_9X -> "No steps added yet."
                else -> "No sub-tasks yet. Add some below!"
            },
            color = when (theme) {
                AppTheme.WINDOWS_9X -> Win9x_Black.copy(alpha = 0.5f)
                AppTheme.NOTEBOOK -> NB_Ink.copy(alpha = 0.5f)
                AppTheme.IPOD_YOUTUBE -> iPod_LightGray.copy(alpha = 0.7f)
                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
            },
            fontSize = 13.sp,
            fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
        )
    }
}

@Composable
private fun ActionButtons(
    theme: AppTheme,
    isAiGenerating: Boolean,
    onAddManual: () -> Unit,
    onGenerateAI: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ThemedPrimaryButton(
            text = "+ Add step manually",
            onClick = onAddManual,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(if (theme == AppTheme.WINDOWS_9X) 0.dp else 12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                .clickable(enabled = !isAiGenerating, onClick = onGenerateAI)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isAiGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "DeepSeek is thinking...",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp,
                    fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                )
            } else {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = when (theme) {
                        AppTheme.WINDOWS_9X -> "AI: Generate steps"
                        AppTheme.NOTEBOOK -> "Let AI plan this for me ✨"
                        AppTheme.IPOD_YOUTUBE -> "Auto-generate playlist"
                        else -> "Generate steps with DeepSeek AI"
                    },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
private fun AddSubTaskDialog(
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
            else -> RoundedCornerShape(10.dp)
        },
        title = {
            Text(
                text = when (theme) {
                    AppTheme.WINDOWS_9X -> "Add Step"
                    AppTheme.NOTEBOOK -> "Add a step"
                    AppTheme.IPOD_YOUTUBE -> "Add Episode"
                    else -> "Add Sub-task"
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
                        "Step description",
                        fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                    )
                },
                singleLine = false,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = when (theme) {
                    AppTheme.WINDOWS_9X -> RoundedCornerShape(0.dp)
                    AppTheme.FRUTIGER_AERO -> RoundedCornerShape(12.dp)
                    else -> RoundedCornerShape(8.dp)
                }
            )
        },
        confirmButton = {
            ThemedPrimaryButton("Add", onClick = { if (title.isNotBlank()) onConfirm(title.trim()) }, enabled = title.isNotBlank())
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif)
            }
        }
    )
}
