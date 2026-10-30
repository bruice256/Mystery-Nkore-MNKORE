package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.MinistryRepository
import com.example.model.MinistryVideo
import com.example.ui.components.UploadMinistryVideoDialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchVideosScreen() {
    val videos by MinistryRepository.videos.collectAsState()
    var selectedVideo by remember { mutableStateOf<MinistryVideo?>(null) }
    var showUploadDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredVideos = videos.filter { v ->
        v.title.contains(searchQuery, ignoreCase = true) ||
                v.preacher.contains(searchQuery, ignoreCase = true) ||
                v.scriptureRef.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showUploadDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.VideoCall, contentDescription = null) },
                text = { Text("Upload Video Sermon", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("upload_video_fab")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = GoldDark,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Christian Sermons & Media",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "Watch preaching, testimonies & Bible documentaries",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search video sermons, preachers, scriptures...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Video Cards List
            items(filteredVideos) { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedVideo = video }
                        .testTag("video_card_${video.id}"),
                    colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column {
                        // Video Thumbnail simulation with play overlay & duration badge
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF2C241B), Color(0xFF1E1812), Color(0xFF120F0C))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Center Play Button
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.9f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            // Duration badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Black.copy(alpha = 0.75f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = video.durationText,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Scripture tag
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldDark.copy(alpha = 0.85f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = video.scriptureRef,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Video info
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = video.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "By ${video.preacher} • ${video.ministryGroup}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = PeaceOlive,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = video.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                ),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${video.viewCount} views • ${video.uploadedAt}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                TextButton(onClick = { selectedVideo = video }) {
                                    Text("Watch Sermon", color = GoldDark, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = GoldDark)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Video Player Dialog
    selectedVideo?.let { video ->
        VideoPlayerModal(
            video = video,
            onDismiss = { selectedVideo = null }
        )
    }

    // Upload Video Dialog
    if (showUploadDialog) {
        UploadMinistryVideoDialog(
            onDismiss = { showUploadDialog = false }
        )
    }
}

@Composable
fun VideoPlayerModal(
    video: MinistryVideo,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var progressSeconds by remember { mutableIntStateOf(120) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Sermon Notes

    LaunchedEffect(isPlaying, playbackSpeed) {
        while (isPlaying) {
            delay((1000L / playbackSpeed).toLong())
            progressSeconds++
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = OffWhiteBg,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                // Video Screen Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color.Black)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                            Text(
                                text = "${playbackSpeed}x",
                                color = GoldLight,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable {
                                        playbackSpeed = when (playbackSpeed) {
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            1.5f -> 0.75f
                                            else -> 1.0f
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Center Play / Pause
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.85f))
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        // Progress slider & time
                        Column {
                            val mins = progressSeconds / 60
                            val secs = progressSeconds % 60
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = String.format("%02d:%02d", mins, secs),
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = video.durationText,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                            Slider(
                                value = progressSeconds.toFloat(),
                                onValueChange = { progressSeconds = it.toInt() },
                                valueRange = 0f..1800f,
                                colors = SliderDefaults.colors(
                                    thumbColor = GoldPrimary,
                                    activeTrackColor = GoldPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Details Area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Preacher: ${video.preacher} • ${video.scriptureRef}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = PeaceOlive,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = OffWhiteSurfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).height(38.dp)
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Synopsis", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Sermon Study Notes", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            when (selectedTab) {
                                0 -> {
                                    Text(
                                        text = "About this Sermon:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = GoldDark
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = video.description,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            lineHeight = 22.sp,
                                            color = TextPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "“Faith cometh by hearing, and hearing by the word of God.” — Romans 10:17",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Serif,
                                            color = PeaceOlive
                                        )
                                    )
                                }
                                1 -> {
                                    Text(
                                        text = "Study Guide & Preacher Notes:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = GoldDark
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = video.sermonNotes,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            lineHeight = 24.sp,
                                            fontFamily = FontFamily.Serif,
                                            color = TextPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
