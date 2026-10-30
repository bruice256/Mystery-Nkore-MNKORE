package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioPrayer
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerSheet(
    prayer: AudioPrayer,
    onDismiss: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgressSeconds by remember { mutableIntStateOf(35) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var fontSizeMultiplier by remember { mutableFloatStateOf(1.0f) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Reflection & Prompts, 1: Full Scripture, 2: Closing Prayer
    var showChimeNotification by remember { mutableStateOf(false) }

    // Simulated audio playback ticker
    LaunchedEffect(isPlaying, playbackSpeed) {
        while (isPlaying && currentProgressSeconds < prayer.durationSeconds) {
            val delayMillis = (1000L / playbackSpeed).toLong()
            delay(delayMillis)
            currentProgressSeconds++
            if (currentProgressSeconds >= prayer.durationSeconds) {
                isPlaying = false
                showChimeNotification = true
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        },
        modifier = Modifier.fillMaxHeight(0.94f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = prayer.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = prayer.dateText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Text size toggle
                    IconButton(
                        onClick = {
                            fontSizeMultiplier = if (fontSizeMultiplier >= 1.3f) 0.9f else fontSizeMultiplier + 0.15f
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Adjust Text Size",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Bell / Chime simulation
                    IconButton(
                        onClick = { showChimeNotification = !showChimeNotification },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Prayer Chime",
                            tint = if (showChimeNotification) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (prayer.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (prayer.isFavorite) SacredCrimson else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (showChimeNotification) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = GoldContainer),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = GoldDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sacred monastic bell chime active for silent contemplation",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnGoldContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Prayer Title & Scripture Reference
            Text(
                text = prayer.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = PeaceOlive,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${prayer.scriptureRef} • Read by ${prayer.narrator}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PeaceOlive,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs for Reading Mode (Pray As You Go synchronized text experience)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = OffWhiteSurfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .height(40.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Contemplation", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Scripture Text", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Benediction", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable Content Area for Corresponding Text
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(OffWhiteCard)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedTab) {
                        0 -> {
                            // Reflection Prompts
                            Text(
                                text = "Lectio Divina Reflection Guide",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            prayer.reflectionPrompts.forEachIndexed { index, prompt ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (currentProgressSeconds >= (index + 1) * 75)
                                            GoldContainer.copy(alpha = 0.6f)
                                        else OffWhiteSurface
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(GoldPrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = prompt,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontFamily = FontFamily.Serif,
                                                fontSize = (15 * fontSizeMultiplier).sp,
                                                lineHeight = (22 * fontSizeMultiplier).sp,
                                                color = TextPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            // Full Scripture Reading Text
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column {
                                    Text(
                                        text = prayer.scriptureRef,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = prayer.scriptureText,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontFamily = FontFamily.Serif,
                                            fontSize = (17 * fontSizeMultiplier).sp,
                                            lineHeight = (26 * fontSizeMultiplier).sp,
                                            color = TextPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = OffWhiteSurfaceVariant),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "“Thy word is a lamp unto my feet, and a light unto my path.” — Psalm 119:105",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontFamily = FontFamily.Serif,
                                                color = PeaceOlive
                                            ),
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }

                        2 -> {
                            // Closing Prayer
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolunteerActivism,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Concluding Benediction",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = prayer.prayerClosing,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontSize = (16 * fontSizeMultiplier).sp,
                                        lineHeight = (25 * fontSizeMultiplier).sp,
                                        textAlign = TextAlign.Center,
                                        color = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Audio Player Controls & Progress Slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Time & Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val currentMin = currentProgressSeconds / 60
                    val currentSec = currentProgressSeconds % 60
                    val totalMin = prayer.durationSeconds / 60
                    val totalSec = prayer.durationSeconds % 60

                    Text(
                        text = String.format("%02d:%02d", currentMin, currentSec),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%02d:%02d", totalMin, totalSec),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Slider(
                    value = currentProgressSeconds.toFloat(),
                    onValueChange = { currentProgressSeconds = it.toInt() },
                    valueRange = 0f..prayer.durationSeconds.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = OffWhiteBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("audio_player_slider")
                )

                // Buttons row: Speed, Rewind 15s, Play/Pause, Forward 15s, Speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Playback speed
                    TextButton(
                        onClick = {
                            playbackSpeed = when (playbackSpeed) {
                                0.8f -> 1.0f
                                1.0f -> 1.2f
                                1.2f -> 1.5f
                                else -> 0.8f
                            }
                        }
                    ) {
                        Text(
                            text = "${playbackSpeed}x",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Replay 15s
                    IconButton(
                        onClick = {
                            currentProgressSeconds = (currentProgressSeconds - 15).coerceAtLeast(0)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Replay10,
                            contentDescription = "Rewind 10s",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Play / Pause FAB
                    FloatingActionButton(
                        onClick = { isPlaying = !isPlaying },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("audio_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause Prayer" else "Play Prayer",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Forward 15s
                    IconButton(
                        onClick = {
                            currentProgressSeconds = (currentProgressSeconds + 15).coerceAtMost(prayer.durationSeconds)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Forward10,
                            contentDescription = "Forward 10s",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Reset / Repeat
                    IconButton(
                        onClick = { currentProgressSeconds = 0; isPlaying = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Restart",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
