package com.mikusz3.mikuszplanner.data.api

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatRequest(
    val model: String = "deepseek-chat",
    val messages: List<ChatMessage>,
    val max_tokens: Int = 600,
    val temperature: Double = 0.7
)

data class ChatChoice(
    val message: ChatMessage,
    val finish_reason: String?
)

data class ChatResponse(
    val id: String?,
    val choices: List<ChatChoice>?,
    val error: ApiError?
)

data class ApiError(
    val message: String,
    val type: String?
)
