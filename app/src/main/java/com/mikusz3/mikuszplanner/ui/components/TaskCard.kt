package com.mikusz3.mikuszplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikusz3.mikuszplanner.data.model.TaskWithSubTasks
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import com.mikusz3.mikuszplanner.ui.theme.LocalAppTheme
import com.mikusz3.mikuszplanner.ui.theme.NB_Cover
import com.mikusz3.mikuszplanner.ui.theme.NB_Ink
import com.mikusz3.mikuszplanner.ui.theme.NB_StickyBlue
import com.mikusz3.mikuszplanner.ui.theme.NB_StickyPink
import com.mikusz3.mikuszplanner.ui.theme.NB_StickyYellow
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Bg
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Black
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Navy
import com.mikusz3.mikuszplanner.ui.theme.XP_BgPane
import com.mikusz3.mikuszplanner.ui.theme.XP_BtnBlue
import com.mikusz3.mikuszplanner.ui.theme.iPod_Border
import com.mikusz3.mikuszplanner.ui.theme.iPod_Card
import com.mikusz3.mikuszplanner.ui.theme.iPod_LightGray
import com.mikusz3.mikuszplanner.ui.theme.iPod_Red
import com.mikusz3.mikuszplanner.ui.theme.iPod_White

private val stickyColors = listOf(
    NB_StickyYellow, NB_StickyPink, NB_StickyBlue,
    Color(0xFFC8E6C9), Color(0xFFE1BEE7), Color(0xFFFFCCBC)
)

@Composable
fun TaskCard(
    taskWithSubTasks: TaskWithSubTasks,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    stickyIndex: Int = 0
) {
    val theme = LocalAppTheme.current
    val task = taskWithSubTasks.task
    val subCount = taskWithSubTasks.subTasks.size
    val doneCount = taskWithSubTasks.subTasks.count { it.isCompleted }
    val progress = if (subCount > 0) doneCount.toFloat() / subCount else 0f

    when (theme) {
        AppTheme.WINDOWS_9X -> Win9xTaskCard(task.title, task.isCompleted, subCount, doneCount, progress, onClick, onComplete, onDelete)
        AppTheme.WINDOWS_XP -> XpTaskCard(task.title, task.isCompleted, subCount, doneCount, progress, onClick, onComplete, onDelete)
        AppTheme.FRUTIGER_AERO -> AeroTaskCard(task.title, task.isCompleted, subCount, doneCount, progress, onClick, onComplete, onDelete)
        AppTheme.NOTEBOOK -> NotebookTaskCard(task.title, task.isCompleted, subCount, doneCount, stickyIndex, onClick, onComplete, onDelete)
        AppTheme.IPOD_YOUTUBE -> IpodTaskCard(task.title, task.isCompleted, subCount, doneCount, progress, onClick, onComplete, onDelete)
    }
}

@Composable
private fun Win9xTaskCard(
    title: String, completed: Boolean, subCount: Int, doneCount: Int, progress: Float,
    onClick: () -> Unit, onComplete: () -> Unit, onDelete: () -> Unit
) {
    Win9xPanel(
        modifier = Modifier.fillMaxWidth().alpha(if (completed) 0.65f else 1f).clickable(onClick = onClick),
        raised = !completed
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = if (completed) ({}) else onComplete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = if (completed) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (completed) Win9x_Navy else Win9x_Black,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = if (completed) FontWeight.Normal else FontWeight.Bold,
                    color = Win9x_Black,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
                if (subCount > 0) {
                    Spacer(Modifier.height(4.dp))
                    Text("$doneCount/$subCount steps", fontSize = 11.sp, color = Win9x_Black.copy(alpha = 0.7f))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp).padding(top = 2.dp),
                        color = Win9x_Navy,
                        trackColor = Win9x_Bg
                    )
                }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Text("✕", fontSize = 11.sp, color = Win9x_Black)
            }
        }
    }
}

@Composable
private fun XpTaskCard(
    title: String, completed: Boolean, subCount: Int, doneCount: Int, progress: Float,
    onClick: () -> Unit, onComplete: () -> Unit, onDelete: () -> Unit
) {
    ThemedCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = if (completed) ({}) else onComplete, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (completed) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (completed) Color(0xFF1E7A1E) else XP_BtnBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (completed) Color.Gray else Color.Black,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
                if (subCount > 0) {
                    Spacer(Modifier.height(4.dp))
                    Text("$doneCount of $subCount steps done", fontSize = 11.sp, color = Color.Gray)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        color = XP_BtnBlue,
                        trackColor = XP_BgPane,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFCC0000))
            }
        }
    }
}

@Composable
private fun AeroTaskCard(
    title: String, completed: Boolean, subCount: Int, doneCount: Int, progress: Float,
    onClick: () -> Unit, onComplete: () -> Unit, onDelete: () -> Unit
) {
    ThemedCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (completed) Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.primaryContainer
                    )
                    .border(2.dp, if (completed) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = if (completed) ({}) else onComplete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (completed) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (completed) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (completed) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
                if (subCount > 0) {
                    Spacer(Modifier.height(6.dp))
                    Text("$doneCount / $subCount steps", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.secondaryContainer,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252).copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun NotebookTaskCard(
    title: String, completed: Boolean, subCount: Int, doneCount: Int, stickyIndex: Int,
    onClick: () -> Unit, onComplete: () -> Unit, onDelete: () -> Unit
) {
    val stickyColor = stickyColors[stickyIndex % stickyColors.size]
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
            .background(stickyColor)
            .border(1.dp, NB_Ink.copy(alpha = 0.15f), RoundedCornerShape(topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(16.dp)
                .background(NB_Ink.copy(alpha = 0.1f), RoundedCornerShape(topEnd = 16.dp))
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = if (completed) ({}) else onComplete, modifier = Modifier.size(30.dp)) {
                Icon(
                    imageVector = if (completed) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = NB_Cover,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NB_Ink,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
                if (subCount > 0) {
                    Text(
                        text = "✓ $doneCount/$subCount",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 13.sp,
                        color = NB_Ink.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Text("✕", fontSize = 13.sp, color = NB_Ink.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun IpodTaskCard(
    title: String, completed: Boolean, subCount: Int, doneCount: Int, progress: Float,
    onClick: () -> Unit, onComplete: () -> Unit, onDelete: () -> Unit
) {
    ThemedCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (completed) Color(0xFF1A1A1A) else iPod_Red.copy(alpha = 0.15f))
                    .border(2.dp, if (completed) Color(0xFF555555) else iPod_Red, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = if (completed) ({}) else onComplete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (completed) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (completed) Color(0xFF555555) else iPod_Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (completed) iPod_LightGray else iPod_White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
                if (subCount > 0) {
                    Spacer(Modifier.height(4.dp))
                    Text("$doneCount/$subCount", fontSize = 11.sp, color = iPod_LightGray)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().padding(top = 3.dp),
                        color = iPod_Red,
                        trackColor = iPod_Border,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252).copy(alpha = 0.6f))
            }
        }
    }
}
