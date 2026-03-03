package com.mikusz3.mikuszplanner.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun ConfettiOverlay(
    isVisible: Boolean,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    val colors = listOf(
        0xFFFF5252.toInt(),
        0xFF00BCD4.toInt(),
        0xFFFFEB3B.toInt(),
        0xFF4CAF50.toInt(),
        0xFF7C4DFF.toInt(),
        0xFFFF9800.toInt(),
        0xFFE91E63.toInt(),
        0xFF2196F3.toInt()
    )

    val parties = listOf(
        Party(
            colors = colors,
            emitter = Emitter(200L, TimeUnit.MILLISECONDS).max(120),
            position = Position.Relative(0.0, 0.0),
            angle = 135,
            spread = 60,
            size = listOf(Size.SMALL, Size.LARGE),
            timeToLive = 4000L
        ),
        Party(
            colors = colors,
            emitter = Emitter(200L, TimeUnit.MILLISECONDS).max(120),
            position = Position.Relative(1.0, 0.0),
            angle = 45,
            spread = 60,
            size = listOf(Size.SMALL, Size.LARGE),
            timeToLive = 4000L
        ),
        Party(
            colors = colors,
            emitter = Emitter(300L, TimeUnit.MILLISECONDS).max(200),
            position = Position.Relative(0.5, 0.0),
            angle = 90,
            spread = 130,
            size = listOf(Size.SMALL, Size.LARGE, Size.LARGE),
            timeToLive = 4000L
        )
    )

    KonfettiView(
        modifier = modifier,
        parties = parties,
        updateListener = object : OnParticleSystemUpdateListener {
            override fun onParticleSystemEnded(system: PartySystem, activeSystems: Int) {
                if (activeSystems == 0) onFinished()
            }
        }
    )
}
