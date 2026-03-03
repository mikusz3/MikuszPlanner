package com.mikusz3.mikuszplanner.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikusz3.mikuszplanner.ui.components.ThemedBackground
import com.mikusz3.mikuszplanner.ui.components.ThemedPrimaryButton
import com.mikusz3.mikuszplanner.ui.components.ThemedTopBar
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import com.mikusz3.mikuszplanner.ui.theme.Aero_DeepBlue
import com.mikusz3.mikuszplanner.ui.theme.Aero_Green
import com.mikusz3.mikuszplanner.ui.theme.Aero_Sky
import com.mikusz3.mikuszplanner.ui.theme.LocalAppTheme
import com.mikusz3.mikuszplanner.ui.theme.NB_Cover
import com.mikusz3.mikuszplanner.ui.theme.NB_Ink
import com.mikusz3.mikuszplanner.ui.theme.NB_Paper
import com.mikusz3.mikuszplanner.ui.theme.NB_StickyYellow
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Bg
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Navy
import com.mikusz3.mikuszplanner.ui.theme.XP_BgPane
import com.mikusz3.mikuszplanner.ui.theme.XP_BtnBlue
import com.mikusz3.mikuszplanner.ui.theme.XP_TitleEnd
import com.mikusz3.mikuszplanner.ui.theme.XP_TitleStart
import com.mikusz3.mikuszplanner.ui.theme.iPod_Bg
import com.mikusz3.mikuszplanner.ui.theme.iPod_Red
import com.mikusz3.mikuszplanner.ui.theme.iPod_Surface
import com.mikusz3.mikuszplanner.ui.theme.iPod_White

private data class ThemePreviewColors(
    val bg: Color,
    val accent: Color,
    val surface: Color,
    val text: Color
)

private val themePreviewData = mapOf(
    AppTheme.WINDOWS_9X to ThemePreviewColors(Win9x_Bg, Win9x_Navy, Color(0xFFD4D4D4), Color.Black),
    AppTheme.WINDOWS_XP to ThemePreviewColors(XP_BgPane, XP_BtnBlue, Color.White, Color.Black),
    AppTheme.FRUTIGER_AERO to ThemePreviewColors(Aero_Sky, Aero_DeepBlue, Color(0xB0FFFFFF), Color(0xFF0D3B66)),
    AppTheme.NOTEBOOK to ThemePreviewColors(NB_Paper, NB_Cover, NB_StickyYellow, NB_Ink),
    AppTheme.IPOD_YOUTUBE to ThemePreviewColors(iPod_Bg, iPod_Red, iPod_Surface, iPod_White)
)

@Composable
fun ThemePickerScreen(
    currentTheme: AppTheme,
    currentApiKey: String,
    onThemeSelect: (AppTheme) -> Unit,
    onBack: () -> Unit,
    onSaveApiKey: (String) -> Unit
) {
    val theme = LocalAppTheme.current
    var showApiDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        ThemedBackground {
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                ThemedTopBar(
                    title = "Appearance",
                    actions = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = if (theme == AppTheme.NOTEBOOK) 64.dp else 12.dp,
                        end = 12.dp,
                        top = 16.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = when (theme) {
                                AppTheme.WINDOWS_9X -> "Select display theme:"
                                AppTheme.NOTEBOOK -> "Choose your style"
                                else -> "Choose your theme"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = if (theme == AppTheme.WINDOWS_9X) 13.sp else 18.sp,
                            fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif,
                            color = when (theme) {
                                AppTheme.WINDOWS_9X -> Color.Black
                                AppTheme.NOTEBOOK -> NB_Ink
                                AppTheme.IPOD_YOUTUBE -> iPod_White
                                else -> MaterialTheme.colorScheme.onBackground
                            },
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    items(AppTheme.entries) { appTheme ->
                        ThemeCard(
                            appTheme = appTheme,
                            isSelected = currentTheme == appTheme,
                            onClick = { onThemeSelect(appTheme) }
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                        ApiKeySection(
                            currentApiKey = currentApiKey,
                            theme = theme,
                            onShowDialog = { showApiDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showApiDialog) {
        ApiKeyDialog(
            currentKey = currentApiKey,
            theme = theme,
            onDismiss = { showApiDialog = false },
            onSave = { key ->
                onSaveApiKey(key)
                showApiDialog = false
            }
        )
    }
}

@Composable
private fun ThemeCard(
    appTheme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val preview = themePreviewData[appTheme] ?: return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(preview.bg)
            .clickable(onClick = onClick)
    ) {
        // Mini preview strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    when (appTheme) {
                        AppTheme.FRUTIGER_AERO -> Brush.horizontalGradient(listOf(Aero_Sky, Aero_DeepBlue))
                        AppTheme.WINDOWS_XP -> Brush.horizontalGradient(listOf(XP_TitleStart, XP_TitleEnd))
                        AppTheme.WINDOWS_9X -> Brush.horizontalGradient(listOf(Win9x_Navy, Color(0xFF1084D0)))
                        AppTheme.NOTEBOOK -> Brush.horizontalGradient(listOf(NB_Cover, NB_Cover.copy(0.8f)))
                        AppTheme.IPOD_YOUTUBE -> Brush.horizontalGradient(listOf(iPod_Surface, iPod_Bg))
                    }
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini dots
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                )
                Spacer(Modifier.width(4.dp))
            }
            Spacer(Modifier.weight(1f))
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                }
            }
        }

        // Theme info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color swatches
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(preview.accent))
                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(preview.surface.copy(alpha = 0.9f)))
                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(preview.text.copy(alpha = 0.3f)))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = appTheme.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = preview.text,
                    fontFamily = if (appTheme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                )
                Text(
                    text = appTheme.description,
                    fontSize = 11.sp,
                    color = preview.text.copy(alpha = 0.6f),
                    fontFamily = if (appTheme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
private fun ApiKeySection(currentApiKey: String, theme: AppTheme, onShowDialog: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(if (theme == AppTheme.WINDOWS_9X) 0.dp else 10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .clickable(onClick = onShowDialog)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Key,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "DeepSeek API Key",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (currentApiKey.isBlank()) "Tap to set your key"
                    else "sk-••••${currentApiKey.takeLast(4)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
private fun ApiKeyDialog(
    currentKey: String,
    theme: AppTheme,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var key by remember { mutableStateOf(currentKey) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = when (theme) {
            AppTheme.WINDOWS_9X -> RoundedCornerShape(0.dp)
            AppTheme.FRUTIGER_AERO -> RoundedCornerShape(20.dp)
            else -> RoundedCornerShape(10.dp)
        },
        title = {
            Text(
                "DeepSeek API Key",
                fontFamily = if (theme == AppTheme.NOTEBOOK) FontFamily.Cursive else FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    "Get your key from platform.deepseek.com",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = key,
                    onValueChange = { key = it },
                    label = { Text("sk-...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            ThemedPrimaryButton("Save", onClick = { onSave(key.trim()) })
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
