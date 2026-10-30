package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun VoiceMessageRecorder(
    onSend: (durationSeconds: Int) -> Unit,
    onCancel: () -> Unit
) {
    var recordingSeconds by remember { mutableIntStateOf(0) }
    val waveBars = remember { mutableStateListOf(10, 18, 25, 14, 30, 22, 16, 28, 35, 20, 12, 24) }

    // Waveform & Timer animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            recordingSeconds++
            for (i in waveBars.indices) {
                waveBars[i] = Random.nextInt(8, 36)
            }
        }
    }

    Surface(
        color = GoldContainer,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Cancel / Delete
            IconButton(
                onClick = onCancel,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Cancel Recording",
                    tint = SacredCrimson
                )
            }

            // Pulsing Mic Icon & Timer
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(SacredCrimson)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val mins = recordingSeconds / 60
                val secs = recordingSeconds % 60
                Text(
                    text = String.format("%02d:%02d", mins, secs),
                    style = MaterialTheme.typography.titleMedium,
                    color = OnGoldContainer
                )
            }

            // Animated Waveform Bars
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(36.dp)
            ) {
                waveBars.forEach { height ->
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(height.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(GoldDark)
                    )
                }
            }

            // Send voice note
            IconButton(
                onClick = { onSend(recordingSeconds.coerceAtLeast(1)) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary)
                    .testTag("send_voice_note_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Voice Note",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
