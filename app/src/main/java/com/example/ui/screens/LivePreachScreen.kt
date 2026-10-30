package com.example.ui.screens

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
import com.example.data.MinistryRepository
import com.example.model.LivePreachingSession
import com.example.ui.components.LivePreachingRoomDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePreachScreen() {
    val liveSessions by MinistryRepository.liveSessions.collectAsState()
    var activeSessionToJoin by remember { mutableStateOf<LivePreachingSession?>(null) }
    var isBroadcastingMyLive by remember { mutableStateOf(false) }

    // Start New Preach Room Dialog
    var showStartPreachDialog by remember { mutableStateOf(false) }
    var preachTitle by remember { mutableStateOf("") }
    var preachScripture by remember { mutableStateOf("") }

    // Join by Code input
    var roomCodeInput by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showStartPreachDialog = true },
                containerColor = SacredCrimson,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Videocam, contentDescription = null) },
                text = { Text("Preach Live Online", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("start_preach_fab")
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
            // Google Meet Preaching Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(GoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CastConnected,
                                    contentDescription = null,
                                    tint = GoldDark,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Live Preaching & Fellowship",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Text(
                                    text = "Global online sanctuary just like Google Meet",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Two primary action cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // "New Preach Meeting" button
                            Button(
                                onClick = { showStartPreachDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = SacredCrimson),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("new_preaching_meeting_button")
                            ) {
                                Icon(Icons.Default.VideoCall, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("New Preach", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            // "Join Sanctuary" button
                            OutlinedButton(
                                onClick = {
                                    if (roomCodeInput.isNotBlank()) {
                                        activeSessionToJoin = LivePreachingSession(
                                            title = "Joined Sanctuary: $roomCodeInput",
                                            preacherName = "Host Pastor",
                                            scriptureTheme = "Faith and Unity",
                                            roomCode = roomCodeInput
                                        )
                                        isBroadcastingMyLive = false
                                    } else if (liveSessions.isNotEmpty()) {
                                        activeSessionToJoin = liveSessions.first()
                                        isBroadcastingMyLive = false
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldDark),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("join_with_code_button")
                            ) {
                                Icon(Icons.Default.MeetingRoom, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Join Code", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Room Code Input
                        OutlinedTextField(
                            value = roomCodeInput,
                            onValueChange = { roomCodeInput = it },
                            placeholder = { Text("Enter sanctuary code (e.g. shalom-sanctuary-live)") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = GoldPrimary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Live Broadcasts Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Live Preaching Sanctuaries",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SacredCrimson.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${liveSessions.count { it.isLive }} LIVE NOW",
                            color = SacredCrimson,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(liveSessions) { session ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeSessionToJoin = session
                            isBroadcastingMyLive = false
                        }
                        .testTag("session_card_${session.id}"),
                    colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (session.isLive) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(SacredCrimson)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "BROADCASTING",
                                        color = SacredCrimson,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                } else {
                                    Text(
                                        text = session.startTimeText,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${session.viewerCount} attendees",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = session.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Preacher: ${session.preacherName} (${session.ministryRole})",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PeaceOlive,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Scripture Theme: ${session.scriptureTheme}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Code: ${session.roomCode}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )

                            Button(
                                onClick = {
                                    activeSessionToJoin = session
                                    isBroadcastingMyLive = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (session.isLive) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (session.isLive) Color.White else TextPrimary
                                ),
                                shape = RoundedCornerShape(18.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (session.isLive) Icons.Default.LiveTv else Icons.Default.Event,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (session.isLive) "Enter Sanctuary" else "Set Reminder",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Live Google Meet Preaching Sanctuary Dialog
    activeSessionToJoin?.let { session ->
        LivePreachingRoomDialog(
            session = session,
            isBroadcaster = isBroadcastingMyLive,
            onDismiss = { activeSessionToJoin = null }
        )
    }

    // Start New Preaching Meeting Dialog
    if (showStartPreachDialog) {
        AlertDialog(
            onDismissRequest = { showStartPreachDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = SacredCrimson)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Live Preaching", style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Broadcast your sermon live to the global community just like Google Meet with real-time video, prayer reactions, and chat.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = preachTitle,
                        onValueChange = { preachTitle = it },
                        label = { Text("Sermon Topic / Title") },
                        placeholder = { Text("e.g. The Peace of Christ in Stormy Times") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("preach_title_field")
                    )

                    OutlinedTextField(
                        value = preachScripture,
                        onValueChange = { preachScripture = it },
                        label = { Text("Scripture Passage") },
                        placeholder = { Text("e.g. John 14:27 & Psalm 91") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("preach_scripture_field")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val session = MinistryRepository.createLivePreachingSession(
                            title = preachTitle.ifBlank { "Live Ministry Preaching & Fellowship" },
                            scripture = preachScripture.ifBlank { "John 14:27" }
                        )
                        showStartPreachDialog = false
                        activeSessionToJoin = session
                        isBroadcastingMyLive = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SacredCrimson),
                    modifier = Modifier.testTag("launch_preach_room_button")
                ) {
                    Text("Go Live Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartPreachDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
