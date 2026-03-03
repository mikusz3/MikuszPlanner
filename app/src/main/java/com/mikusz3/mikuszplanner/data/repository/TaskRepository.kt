package com.mikusz3.mikuszplanner.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mikusz3.mikuszplanner.data.api.ChatMessage
import com.mikusz3.mikuszplanner.data.api.ChatRequest
import com.mikusz3.mikuszplanner.data.api.DeepSeekClient
import com.mikusz3.mikuszplanner.data.db.TaskDao
import com.mikusz3.mikuszplanner.data.model.SubTask
import com.mikusz3.mikuszplanner.data.model.Task
import com.mikusz3.mikuszplanner.data.model.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasksWithSubTasks(): Flow<List<TaskWithSubTasks>> =
        taskDao.getAllTasksWithSubTasks()

    fun getTaskWithSubTasks(taskId: Long): Flow<TaskWithSubTasks?> =
        taskDao.getTaskWithSubTasks(taskId)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun insertSubTask(subTask: SubTask): Long = taskDao.insertSubTask(subTask)
    suspend fun insertSubTasks(subTasks: List<SubTask>) = taskDao.insertSubTasks(subTasks)
    suspend fun updateSubTask(subTask: SubTask) = taskDao.updateSubTask(subTask)
    suspend fun deleteSubTask(subTask: SubTask) = taskDao.deleteSubTask(subTask)

    suspend fun generateSubTasksWithAI(apiKey: String, mainTaskTitle: String): Result<List<String>> {
        return try {
            val prompt = """Given the main task: "$mainTaskTitle", generate 3 to 5 specific, actionable sub-tasks that help complete it step by step.
Return ONLY a valid JSON array of strings, no explanations, no markdown. Example: ["Step one", "Step two", "Step three"]"""

            val request = ChatRequest(
                messages = listOf(
                    ChatMessage(
                        role = "system",
                        content = "You are a helpful task planning assistant for people with ADHD/autism. Break tasks into clear, manageable steps. Respond only with a valid JSON array of strings."
                    ),
                    ChatMessage(role = "user", content = prompt)
                )
            )

            val response = DeepSeekClient.service.generateSubTasks(
                authorization = "Bearer $apiKey",
                request = request
            )

            if (response.isSuccessful) {
                val content = response.body()?.choices?.firstOrNull()?.message?.content
                    ?: return Result.failure(Exception("Empty response from API"))

                val jsonStart = content.indexOf('[')
                val jsonEnd = content.lastIndexOf(']')
                val jsonArray = if (jsonStart != -1 && jsonEnd != -1)
                    content.substring(jsonStart, jsonEnd + 1)
                else
                    return Result.failure(Exception("Could not parse AI response"))

                val type = object : TypeToken<List<String>>() {}.type
                val titles: List<String> = Gson().fromJson(jsonArray, type)
                Result.success(titles)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                Result.failure(Exception("API error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
