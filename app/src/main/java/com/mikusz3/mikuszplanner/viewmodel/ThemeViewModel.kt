package com.mikusz3.mikuszplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mikusz3.mikuszplanner.data.preferences.ThemePreferenceManager
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val themeManager = ThemePreferenceManager(application)

    val currentTheme: StateFlow<AppTheme> = themeManager.selectedTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.FRUTIGER_AERO
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { themeManager.setTheme(theme) }
    }
}
