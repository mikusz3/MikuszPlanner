package com.mikusz3.mikuszplanner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppTheme = staticCompositionLocalOf { AppTheme.FRUTIGER_AERO }

private fun win9xColors() = lightColorScheme(
    primary            = Win9x_Navy,
    onPrimary          = Win9x_White,
    primaryContainer   = Win9x_TitleEnd,
    onPrimaryContainer = Win9x_White,
    secondary          = Win9x_Teal,
    onSecondary        = Win9x_White,
    secondaryContainer = Win9x_Light,
    onSecondaryContainer = Win9x_Black,
    background         = Win9x_Bg,
    onBackground       = Win9x_Black,
    surface            = Win9x_ButtonFace,
    onSurface          = Win9x_Black,
    surfaceVariant     = Win9x_Light,
    onSurfaceVariant   = Win9x_Black,
    outline            = Win9x_Dark,
    outlineVariant     = Win9x_Darkest,
    error              = Color(0xFFCC0000),
    onError            = Win9x_White
)

private fun xpColors() = lightColorScheme(
    primary            = XP_BtnBlue,
    onPrimary          = XP_SelectedText,
    primaryContainer   = XP_BtnBlueHover,
    onPrimaryContainer = XP_SelectedText,
    secondary          = XP_StartGreen,
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFFD8EDCA),
    onSecondaryContainer = XP_Text,
    background         = XP_BgPane,
    onBackground       = XP_Text,
    surface            = XP_FormBg,
    onSurface          = XP_Text,
    surfaceVariant     = Color(0xFFD5D5D5),
    onSurfaceVariant   = XP_Text,
    outline            = XP_SilverBorder,
    outlineVariant     = Color(0xFFCCCCCC)
)

private fun aeroColors() = lightColorScheme(
    primary            = Aero_DeepBlue,
    onPrimary          = Aero_White,
    primaryContainer   = Aero_LightBlue,
    onPrimaryContainer = Aero_TextDark,
    secondary          = Aero_Green,
    onSecondary        = Aero_White,
    secondaryContainer = Aero_LightGreen,
    onSecondaryContainer = Aero_TextDark,
    background         = Aero_Bg,
    onBackground       = Aero_TextDark,
    surface            = Aero_GlassWhite,
    onSurface          = Aero_TextDark,
    surfaceVariant     = Color(0xFFD0E8FA),
    onSurfaceVariant   = Aero_TextDark,
    outline            = Aero_DeepBlue,
    outlineVariant     = Aero_LightBlue
)

private fun notebookColors() = lightColorScheme(
    primary            = NB_Cover,
    onPrimary          = Color.White,
    primaryContainer   = NB_Blue,
    onPrimaryContainer = NB_Ink,
    secondary          = Color(0xFFD32F2F),
    onSecondary        = Color.White,
    secondaryContainer = NB_Pink,
    onSecondaryContainer = NB_Ink,
    background         = NB_Paper,
    onBackground       = NB_Ink,
    surface            = NB_StickyYellow,
    onSurface          = NB_Ink,
    surfaceVariant     = NB_StickyBlue,
    onSurfaceVariant   = NB_Ink,
    outline            = NB_RedMargin,
    outlineVariant     = NB_LineBlue
)

private fun ipodColors() = darkColorScheme(
    primary            = iPod_Red,
    onPrimary          = iPod_White,
    primaryContainer   = iPod_DarkRed,
    onPrimaryContainer = iPod_White,
    secondary          = iPod_LightGray,
    onSecondary        = iPod_Bg,
    secondaryContainer = iPod_Selected,
    onSecondaryContainer = iPod_White,
    background         = iPod_Bg,
    onBackground       = iPod_White,
    surface            = iPod_Surface,
    onSurface          = iPod_White,
    surfaceVariant     = iPod_Card,
    onSurfaceVariant   = iPod_LightGray,
    outline            = iPod_Border,
    outlineVariant     = iPod_MidGray,
    error              = Color(0xFFFF5252),
    onError            = Color.Black
)

@Composable
fun MikuszPlannerTheme(
    appTheme: AppTheme = AppTheme.FRUTIGER_AERO,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.WINDOWS_9X    -> win9xColors()
        AppTheme.WINDOWS_XP    -> xpColors()
        AppTheme.FRUTIGER_AERO -> aeroColors()
        AppTheme.NOTEBOOK      -> notebookColors()
        AppTheme.IPOD_YOUTUBE  -> ipodColors()
    }
    val typography = typographyForTheme(appTheme)

    CompositionLocalProvider(LocalAppTheme provides appTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
