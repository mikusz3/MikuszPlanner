package com.mikusz3.mikuszplanner.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mikusz_settings")

class ThemePreferenceManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("selected_theme")
        val API_KEY_KEY = stringPreferencesKey("deepseek_api_key")
    }

    val selectedTheme: Flow<AppTheme> = context.dataStore.data.map { prefs ->
        val name = prefs[THEME_KEY] ?: AppTheme.FRUTIGER_AERO.name
        runCatching { AppTheme.valueOf(name) }.getOrDefault(AppTheme.FRUTIGER_AERO)
    }

    val savedApiKey: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[API_KEY_KEY] ?: ""
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { prefs -> prefs[THEME_KEY] = theme.name }
    }

    suspend fun setApiKey(key: String) {
        context.dataStore.edit { prefs -> prefs[API_KEY_KEY] = key }
    }
}
