package com.mikusz3.mikuszplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import com.mikusz3.mikuszplanner.ui.theme.Aero_Bg
import com.mikusz3.mikuszplanner.ui.theme.Aero_DeepBlue
import com.mikusz3.mikuszplanner.ui.theme.Aero_GlassWhite
import com.mikusz3.mikuszplanner.ui.theme.Aero_GlossTop
import com.mikusz3.mikuszplanner.ui.theme.Aero_Gradient1
import com.mikusz3.mikuszplanner.ui.theme.Aero_Gradient2
import com.mikusz3.mikuszplanner.ui.theme.Aero_LightBlue
import com.mikusz3.mikuszplanner.ui.theme.Aero_Sky
import com.mikusz3.mikuszplanner.ui.theme.Aero_TextDark
import com.mikusz3.mikuszplanner.ui.theme.Aero_White
import com.mikusz3.mikuszplanner.ui.theme.LocalAppTheme
import com.mikusz3.mikuszplanner.ui.theme.NB_Cover
import com.mikusz3.mikuszplanner.ui.theme.NB_Ink
import com.mikusz3.mikuszplanner.ui.theme.NB_LineBlue
import com.mikusz3.mikuszplanner.ui.theme.NB_Paper
import com.mikusz3.mikuszplanner.ui.theme.NB_RedMargin
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Bg
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Black
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Dark
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Darkest
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Light
import com.mikusz3.mikuszplanner.ui.theme.Win9x_TitleEnd
import com.mikusz3.mikuszplanner.ui.theme.Win9x_TitleStart
import com.mikusz3.mikuszplanner.ui.theme.Win9x_White
import com.mikusz3.mikuszplanner.ui.theme.XP_BgPane
import com.mikusz3.mikuszplanner.ui.theme.XP_BtnBlue
import com.mikusz3.mikuszplanner.ui.theme.XP_TitleEnd
import com.mikusz3.mikuszplanner.ui.theme.XP_TitleStart
import com.mikusz3.mikuszplanner.ui.theme.iPod_Bg
import com.mikusz3.mikuszplanner.ui.theme.iPod_Border
import com.mikusz3.mikuszplanner.ui.theme.iPod_DarkRed
import com.mikusz3.mikuszplanner.ui.theme.iPod_GlossWhite
import com.mikusz3.mikuszplanner.ui.theme.iPod_LightGray
import com.mikusz3.mikuszplanner.ui.theme.iPod_Red
import com.mikusz3.mikuszplanner.ui.theme.iPod_Surface
import com.mikusz3.mikuszplanner.ui.theme.iPod_White

// ─────────────────────────────────────────────────────────────────────────────
// Background
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ThemedBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val theme = LocalAppTheme.current
    val bg: Modifier = when (theme) {
        AppTheme.WINDOWS_9X -> modifier.fillMaxSize().background(Win9x_Bg)
        AppTheme.WINDOWS_XP -> modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF2B7FD4), XP_BgPane, XP_BgPane))
        )
        AppTheme.FRUTIGER_AERO -> modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Aero_Sky, Aero_Bg, Aero_White))
        )
        AppTheme.NOTEBOOK -> modifier.fillMaxSize().drawBehind {
            drawRect(NB_Paper)
            val lineSpacing = 30.dp.toPx()
            val marginX = 56.dp.toPx()
            var y = lineSpacing * 2
            while (y < size.height) {
                drawLine(NB_LineBlue, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.5f)
                y += lineSpacing
            }
            drawLine(NB_RedMargin, Offset(marginX, 0f), Offset(marginX, size.height), strokeWidth = 2f)
        }
        AppTheme.IPOD_YOUTUBE -> modifier.fillMaxSize().background(iPod_Bg)
    }
    Box(modifier = bg, content = content)
}

// ─────────────────────────────────────────────────────────────────────────────
// Top Bar
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ThemedTopBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val theme = LocalAppTheme.current
    when (theme) {
        AppTheme.WINDOWS_9X -> Win9xTitleBar(title, modifier, actions)
        AppTheme.WINDOWS_XP -> XpTitleBar(title, modifier, actions)
        AppTheme.FRUTIGER_AERO -> AeroTitleBar(title, modifier, actions)
        AppTheme.NOTEBOOK -> NotebookTitleBar(title, modifier, actions)
        AppTheme.IPOD_YOUTUBE -> IpodTitleBar(title, modifier, actions)
    }
}

@Composable
private fun Win9xTitleBar(
    title: String,
    modifier: Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Win9x_TitleStart, Win9x_TitleEnd)))
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🖥 $title",
                color = Win9x_White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
    }
}

@Composable
private fun XpTitleBar(
    title: String,
    modifier: Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(XP_TitleStart, XP_TitleEnd)))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
    }
}

@Composable
private fun AeroTitleBar(
    title: String,
    modifier: Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Aero_Gradient1, Aero_Gradient2)))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(listOf(Aero_GlossTop, Color.Transparent)))
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Aero_White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
    }
}

@Composable
private fun NotebookTitleBar(
    title: String,
    modifier: Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(NB_Cover)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
    }
}

@Composable
private fun IpodTitleBar(
    title: String,
    modifier: Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(iPod_Surface)
            .border(Dp.Hairline, iPod_Border, RectangleShape)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = iPod_White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Primary Button
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ThemedPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val theme = LocalAppTheme.current
    when (theme) {
        AppTheme.WINDOWS_9X -> Win9xButton(text, onClick, modifier, enabled)
        AppTheme.WINDOWS_XP -> XpButton(text, onClick, modifier, enabled)
        AppTheme.FRUTIGER_AERO -> AeroButton(text, onClick, modifier, enabled)
        AppTheme.NOTEBOOK -> NotebookButton(text, onClick, modifier, enabled)
        AppTheme.IPOD_YOUTUBE -> IpodButton(text, onClick, modifier, enabled)
    }
}

@Composable
private fun Win9xButton(text: String, onClick: () -> Unit, modifier: Modifier, enabled: Boolean) {
    Box(
        modifier = modifier
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .background(Win9x_Bg)
            .drawBehind {
                val w = size.width; val h = size.height; val s = 2.dp.toPx()
                drawLine(Win9x_White, Offset(0f, h), Offset(0f, 0f), s)
                drawLine(Win9x_White, Offset(0f, 0f), Offset(w, 0f), s)
                drawLine(Win9x_Light, Offset(s, h - s), Offset(s, s), s)
                drawLine(Win9x_Light, Offset(s, s), Offset(w - s, s), s)
                drawLine(Win9x_Darkest, Offset(0f, h), Offset(w, h), s)
                drawLine(Win9x_Darkest, Offset(w, h), Offset(w, 0f), s)
                drawLine(Win9x_Dark, Offset(s, h - s), Offset(w - s, h - s), s)
                drawLine(Win9x_Dark, Offset(w - s, h - s), Offset(w - s, s), s)
            }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) Win9x_Black else Win9x_Dark,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun XpButton(text: String, onClick: () -> Unit, modifier: Modifier, enabled: Boolean) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = XP_BtnBlue,
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
private fun AeroButton(text: String, onClick: () -> Unit, modifier: Modifier, enabled: Boolean) {
    Box(
        modifier = modifier
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Aero_Gradient2, Aero_DeepBlue)))
            .border(1.dp, Aero_LightBlue.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(listOf(Aero_GlossTop, Color.Transparent))
                )
        )
        Text(
            text = text,
            color = Aero_White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun NotebookButton(text: String, onClick: () -> Unit, modifier: Modifier, enabled: Boolean) {
    Box(
        modifier = modifier
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .clip(RoundedCornerShape(4.dp))
            .background(NB_Cover)
            .border(2.dp, NB_Ink.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = FontFamily.Cursive
        )
    }
}

@Composable
private fun IpodButton(text: String, onClick: () -> Unit, modifier: Modifier, enabled: Boolean) {
    Box(
        modifier = modifier
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.verticalGradient(listOf(iPod_Red, iPod_DarkRed)))
            .border(1.dp, Color(0xFF880000), RoundedCornerShape(8.dp))
            .padding(horizontal = 18.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.verticalGradient(listOf(iPod_GlossWhite, Color.Transparent)))
        )
        Text(
            text = text,
            color = iPod_White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Windows 9x 3D-raised container (utility)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun Win9xPanel(
    modifier: Modifier = Modifier,
    raised: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(Win9x_Bg)
            .drawBehind {
                val w = size.width; val h = size.height; val s = 2.dp.toPx()
                if (raised) {
                    drawLine(Win9x_White, Offset(0f, h), Offset(0f, 0f), s)
                    drawLine(Win9x_White, Offset(0f, 0f), Offset(w, 0f), s)
                    drawLine(Win9x_Light, Offset(s, h - s), Offset(s, s), s)
                    drawLine(Win9x_Light, Offset(s, s), Offset(w - s, s), s)
                    drawLine(Win9x_Darkest, Offset(0f, h), Offset(w, h), s)
                    drawLine(Win9x_Darkest, Offset(w, h), Offset(w, 0f), s)
                    drawLine(Win9x_Dark, Offset(s, h - s), Offset(w - s, h - s), s)
                    drawLine(Win9x_Dark, Offset(w - s, h - s), Offset(w - s, s), s)
                } else {
                    drawLine(Win9x_Darkest, Offset(0f, h), Offset(0f, 0f), s)
                    drawLine(Win9x_Darkest, Offset(0f, 0f), Offset(w, 0f), s)
                    drawLine(Win9x_Dark, Offset(s, h - s), Offset(s, s), s)
                    drawLine(Win9x_Dark, Offset(s, s), Offset(w - s, s), s)
                    drawLine(Win9x_White, Offset(0f, h), Offset(w, h), s)
                    drawLine(Win9x_White, Offset(w, h), Offset(w, 0f), s)
                    drawLine(Win9x_Light, Offset(s, h - s), Offset(w - s, h - s), s)
                    drawLine(Win9x_Light, Offset(w - s, h - s), Offset(w - s, s), s)
                }
            }
            .padding(4.dp),
        content = content
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Themed Card container (used in task lists)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val theme = LocalAppTheme.current
    val clickMod = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    when (theme) {
        AppTheme.WINDOWS_9X -> Win9xPanel(
            modifier = modifier.fillMaxWidth().then(clickMod),
            raised = true,
            content = content
        )
        AppTheme.WINDOWS_XP -> Box(
            modifier = modifier
                .fillMaxWidth()
                .then(clickMod)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(1.dp, XP_BtnBlue.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                .padding(12.dp),
            content = content
        )
        AppTheme.FRUTIGER_AERO -> Box(
            modifier = modifier
                .fillMaxWidth()
                .then(clickMod)
                .clip(RoundedCornerShape(16.dp))
                .background(Aero_GlassWhite)
                .border(
                    1.dp,
                    Brush.verticalGradient(listOf(Aero_White, Aero_LightBlue)),
                    RoundedCornerShape(16.dp)
                )
                .padding(14.dp),
            content = content
        )
        AppTheme.NOTEBOOK -> Box(
            modifier = modifier
                .fillMaxWidth()
                .then(clickMod)
                .clip(RoundedCornerShape(3.dp))
                .background(NB_Paper)
                .border(1.dp, NB_LineBlue, RoundedCornerShape(3.dp))
                .padding(12.dp),
            content = content
        )
        AppTheme.IPOD_YOUTUBE -> Box(
            modifier = modifier
                .fillMaxWidth()
                .then(clickMod)
                .clip(RoundedCornerShape(10.dp))
                .background(iPod_Surface)
                .border(1.dp, iPod_Border, RoundedCornerShape(10.dp))
                .padding(12.dp),
            content = content
        )
    }
}
