package com.mikusz3.mikuszplanner.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class TaskRepositoryApiUtilsTest {

    @Test
    fun normalizeDeepSeekApiKey_keepsRawSkKey() {
        val normalized = normalizeDeepSeekApiKey("sk-abc123")

        assertEquals("sk-abc123", normalized)
    }

    @Test
    fun normalizeDeepSeekApiKey_removesBearerPrefix() {
        val normalized = normalizeDeepSeekApiKey("Bearer sk-abc123")

        assertEquals("sk-abc123", normalized)
    }

    @Test
    fun normalizeDeepSeekApiKey_removesAuthorizationPrefixAndQuotes() {
        val normalized = normalizeDeepSeekApiKey("  \"Authorization: Bearer sk-abc123\"  ")

        assertEquals("sk-abc123", normalized)
    }

    @Test
    fun formatDeepSeekApiError_returnsFriendlyAuthMessage() {
        val message = formatDeepSeekApiError(
            httpCode = 401,
            rawBody = """{"error":{"message":"Authentication Fails","type":"authentication_error"}}"""
        )

        assertEquals(
            "DeepSeek authentication failed. Check your API key in Appearance > DeepSeek API Key.",
            message
        )
    }

    @Test
    fun formatDeepSeekApiError_returnsApiMessageWhenAvailable() {
        val message = formatDeepSeekApiError(
            httpCode = 400,
            rawBody = """{"error":{"message":"Model not found","type":"invalid_request_error"}}"""
        )

        assertEquals("DeepSeek API error: Model not found", message)
    }

    @Test
    fun formatDeepSeekApiError_fallsBackToHttpCode() {
        val message = formatDeepSeekApiError(httpCode = 500, rawBody = "")

        assertEquals("DeepSeek API request failed (HTTP 500).", message)
    }
}
