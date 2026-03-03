package com.mikusz3.mikuszplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mikusz3.mikuszplanner.data.db.AppDatabase
import com.mikusz3.mikuszplanner.data.model.SubTask
import com.mikusz3.mikuszplanner.data.model.Task
import com.mikusz3.mikuszplanner.data.model.TaskWithSubTasks
import com.mikusz3.mikuszplanner.data.preferences.ThemePreferenceManager
import com.mikusz3.mikuszplanner.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<TaskWithSubTasks> = emptyList(),
    val celebratingId: Long? = null,
    val aiGenerating: Boolean = false,
    val aiError: String? = null
)

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(AppDatabase.getDatabase(application).taskDao())
    private val prefManager = ThemePreferenceManager(application)

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    val apiKey: StateFlow<String> = prefManager.savedApiKey.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ""
    )

    init {
        viewModelScope.launch {
            repository.getAllTasksWithSubTasks().collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch { prefManager.setApiKey(key) }
    }

    fun addTask(title: String, description: String = "") {
        viewModelScope.launch {
            repository.insertTask(Task(title = title, description = description))
        }
    }

    fun updateTaskTitle(task: Task, newTitle: String) {
        viewModelScope.launch { repository.updateTask(task.copy(title = newTitle)) }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = true))
            _uiState.update { it.copy(celebratingId = task.id) }
        }
    }

    fun uncompleteTask(task: Task) {
        viewModelScope.launch { repository.updateTask(task.copy(isCompleted = false)) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    fun addSubTask(taskId: Long, title: String) {
        viewModelScope.launch {
            val currentCount = _uiState.value.tasks
                .find { it.task.id == taskId }?.subTasks?.size ?: 0
            repository.insertSubTask(SubTask(taskId = taskId, title = title, sortOrder = currentCount))
        }
    }

    fun completeSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.updateSubTask(subTask.copy(isCompleted = true))
            _uiState.update { it.copy(celebratingId = -subTask.id) }
        }
    }

    fun uncompleteSubTask(subTask: SubTask) {
        viewModelScope.launch { repository.updateSubTask(subTask.copy(isCompleted = false)) }
    }

    fun deleteSubTask(subTask: SubTask) {
        viewModelScope.launch { repository.deleteSubTask(subTask) }
    }

    fun clearCelebration() {
        _uiState.update { it.copy(celebratingId = null) }
    }

    fun generateSubTasksWithAI(taskId: Long, taskTitle: String) {
        val key = apiKey.value
        if (key.isBlank()) {
            _uiState.update { it.copy(aiError = "No API key set. Add your DeepSeek API key in Settings.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(aiGenerating = true, aiError = null) }
            repository.generateSubTasksWithAI(key, taskTitle).fold(
                onSuccess = { titles ->
                    val existing = _uiState.value.tasks.find { it.task.id == taskId }?.subTasks?.size ?: 0
                    val subTasks = titles.mapIndexed { i, title ->
                        SubTask(taskId = taskId, title = title, sortOrder = existing + i)
                    }
                    repository.insertSubTasks(subTasks)
                    _uiState.update { it.copy(aiGenerating = false) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(aiGenerating = false, aiError = err.message ?: "Unknown error") }
                }
            )
        }
    }

    fun clearAiError() {
        _uiState.update { it.copy(aiError = null) }
    }
}
