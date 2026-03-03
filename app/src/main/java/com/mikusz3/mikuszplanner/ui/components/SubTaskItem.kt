package com.mikusz3.mikuszplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikusz3.mikuszplanner.data.model.SubTask
import com.mikusz3.mikuszplanner.ui.theme.AppTheme
import com.mikusz3.mikuszplanner.ui.theme.LocalAppTheme
import com.mikusz3.mikuszplanner.ui.theme.NB_Cover
import com.mikusz3.mikuszplanner.ui.theme.NB_Ink
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Bg
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Black
import com.mikusz3.mikuszplanner.ui.theme.Win9x_Navy
import com.mikusz3.mikuszplanner.ui.theme.XP_BtnBlue
import com.mikusz3.mikuszplanner.ui.theme.iPod_Border
import com.mikusz3.mikuszplanner.ui.theme.iPod_LightGray
import com.mikusz3.mikuszplanner.ui.theme.iPod_Red
import com.mikusz3.mikuszplanner.ui.theme.iPod_Surface
import com.mikusz3.mikuszplanner.ui.theme.iPod_White

@Composable
fun SubTaskItem(
    subTask: SubTask,
    onComplete: () -> Unit,
    onUncomplete: () -> Unit,
    onDelete: () -> Unit
) {
    val theme = LocalAppTheme.current

    when (theme) {
        AppTheme.WINDOWS_9X -> Win9xSubTask(subTask, onComplete, onUncomplete, onDelete)
        AppTheme.WINDOWS_XP -> DefaultSubTask(subTask, onComplete, onUncomplete, onDelete, XP_BtnBlue, Color(0xFF1E7A1E))
        AppTheme.FRUTIGER_AERO -> DefaultSubTask(subTask, onComplete, onUncomplete, onDelete,
            MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
        AppTheme.NOTEBOOK -> NotebookSubTask(subTask, onComplete, onUncomplete, onDelete)
        AppTheme.IPOD_YOUTUBE -> IpodSubTask(subTask, onComplete, onUncomplete, onDelete)
    }
}

@Composable
private fun Win9xSubTask(
    subTask: SubTask, onComplete: () -> Unit, onUncomplete: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Win9x_Bg)
            .alpha(if (subTask.isCompleted) 0.65f else 1f)
            .padding(vertical = 4.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(Color.White)
                .border(1.dp, Win9x_Black)
                .clickable { if (subTask.isCompleted) onUncomplete() else onComplete() },
            contentAlignment = Alignment.Center
        ) {
            if (subTask.isCompleted) {
                Text("✓", fontSize = 10.sp, color = Win9x_Navy, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = subTask.title,
            modifier = Modifier.weight(1f),
            fontSize = 12.sp,
            color = Win9x_Black,
            textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
            Text("✕", fontSize = 10.sp, color = Win9x_Black.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun DefaultSubTask(
    subTask: SubTask,
    onComplete: () -> Unit,
    onUncomplete: () -> Unit,
    onDelete: () -> Unit,
    activeColor: Color,
    doneColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (subTask.isCompleted) 0.7f else 1f)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { if (subTask.isCompleted) onUncomplete() else onComplete() },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = if (subTask.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (subTask.isCompleted) doneColor else activeColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = subTask.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (subTask.isCompleted) 0.5f else 1f),
            textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252).copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun NotebookSubTask(
    subTask: SubTask, onComplete: () -> Unit, onUncomplete: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(2.dp, NB_Cover, CircleShape)
                .clip(CircleShape)
                .background(if (subTask.isCompleted) NB_Cover.copy(alpha = 0.2f) else Color.Transparent)
                .clickable { if (subTask.isCompleted) onUncomplete() else onComplete() },
            contentAlignment = Alignment.Center
        ) {
            if (subTask.isCompleted) {
                Text("✓", fontSize = 11.sp, color = NB_Cover, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = subTask.title,
            modifier = Modifier.weight(1f),
            fontFamily = FontFamily.Cursive,
            fontSize = 15.sp,
            color = NB_Ink.copy(alpha = if (subTask.isCompleted) 0.5f else 0.85f),
            textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
            Text("✕", fontSize = 12.sp, color = NB_Ink.copy(alpha = 0.35f))
        }
    }
}

@Composable
private fun IpodSubTask(
    subTask: SubTask, onComplete: () -> Unit, onUncomplete: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (subTask.isCompleted) iPod_Surface.copy(alpha = 0.5f) else iPod_Surface)
            .border(1.dp, iPod_Border, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { if (subTask.isCompleted) onUncomplete() else onComplete() },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (subTask.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (subTask.isCompleted) Color(0xFF4CAF50) else iPod_Red,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = subTask.title,
            modifier = Modifier.weight(1f),
            color = if (subTask.isCompleted) iPod_LightGray else iPod_White,
            fontSize = 14.sp,
            textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252).copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
        }
    }
}
