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

private data class DeepSeekErrorEnvelope(
    val error: DeepSeekError? = null
)

private data class DeepSeekError(
    val message: String? = null,
    val type: String? = null
)

internal fun normalizeDeepSeekApiKey(rawKey: String): String {
    return rawKey
        .trim()
        .removeSurrounding("\"")
        .removeSurrounding("'")
        .replace(Regex("^Authorization\\s*:\\s*", RegexOption.IGNORE_CASE), "")
        .replace(Regex("^Bearer\\s+", RegexOption.IGNORE_CASE), "")
        .trim()
}

internal fun formatDeepSeekApiError(httpCode: Int, rawBody: String?): String {
    val parsed = rawBody
        ?.takeIf { it.isNotBlank() }
        ?.let { runCatching { Gson().fromJson(it, DeepSeekErrorEnvelope::class.java) }.getOrNull() }
        ?.error

    val type = parsed?.type?.trim()
    val message = parsed?.message?.trim()

    if (type.equals("authentication_error", ignoreCase = true) || httpCode == 401) {
        return "DeepSeek authentication failed. Check your API key in Appearance > DeepSeek API Key."
    }

    if (!message.isNullOrBlank()) {
        return "DeepSeek API error: $message"
    }

    return "DeepSeek API request failed (HTTP $httpCode)."
}

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
            val normalizedApiKey = normalizeDeepSeekApiKey(apiKey)
            if (normalizedApiKey.isBlank()) {
                return Result.failure(
                    Exception("No API key set. Add your DeepSeek API key in Settings.")
                )
            }

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
                authorization = "Bearer $normalizedApiKey",
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
                Result.failure(
                    Exception(
                        formatDeepSeekApiError(
                            httpCode = response.code(),
                            rawBody = response.errorBody()?.string()
                        )
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
