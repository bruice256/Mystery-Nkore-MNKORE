package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MinistryRepository
import com.example.model.ChatMessage
import com.example.model.FellowshipChat
import com.example.model.MessageType
import com.example.ui.components.CreateStatusDialog
import com.example.ui.components.StatusViewerDialog
import com.example.ui.components.VoiceCallDialog
import com.example.ui.components.VoiceMessageRecorder
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityChatScreen(
    onNavigateToStatuses: () -> Unit
) {
    val chats by MinistryRepository.chats.collectAsState()
    val allMessages by MinistryRepository.chatMessages.collectAsState()
    val statuses by MinistryRepository.statuses.collectAsState()

    var activeChat by remember { mutableStateOf<FellowshipChat?>(null) }
    var activeVoiceCallContact by remember { mutableStateOf<Pair<String, String>?>(null) }
    var viewingStatus by remember { mutableStateOf<com.example.model.MinistryStatus?>(null) }
    var showCreateStatusDialog by remember { mutableStateOf(false) }

    // If activeChat is selected, show conversation view with BackHandler
    if (activeChat != null) {
        val currentChat = activeChat!!
        BackHandler { activeChat = null }

        ChatConversationView(
            chat = currentChat,
            messages = allMessages[currentChat.id] ?: emptyList(),
            onBack = { activeChat = null },
            onStartVoiceCall = { name, role ->
                activeVoiceCallContact = Pair(name, role)
            }
        )
    } else {
        // Conversation List + Statuses Story Bar
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                // Status / Fellowship Story Bar
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(OffWhiteSurface)
                            .padding(vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Spiritual Statuses & Testimonies",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                            )
                            TextButton(onClick = onNavigateToStatuses) {
                                Text("See All", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // "Add My Status" item
                            item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { showCreateStatusDialog = true }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(GoldContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Post Status",
                                            tint = GoldDark,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("My Status", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            items(statuses) { status ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { viewingStatus = status }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(GoldPrimary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(GoldPrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = status.authorName.take(2).uppercase(),
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = status.authorName.split(" ").first(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Divider(color = OffWhiteBorder)
                    Text(
                        text = "Fellowship Outreach & Channels",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                items(chats) { chat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable { activeChat = chat }
                            .testTag("chat_item_${chat.id}"),
                        colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(0.5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if (chat.isChannel) GoldContainer else PeaceOliveLight),
                                contentAlignment = Alignment.Center
                            ) {
                                if (chat.isChannel) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = GoldDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = chat.avatarInitials,
                                        color = PeaceOlive,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = chat.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = chat.lastMessageTime,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = chat.lastMessageText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Voice Call Dialog
    activeVoiceCallContact?.let { (name, role) ->
        VoiceCallDialog(
            contactName = name,
            contactRole = role,
            onDismiss = { activeVoiceCallContact = null }
        )
    }

    // Status Viewer
    viewingStatus?.let { status ->
        StatusViewerDialog(
            status = status,
            onDismiss = { viewingStatus = null }
        )
    }

    // Create Status
    if (showCreateStatusDialog) {
        CreateStatusDialog(onDismiss = { showCreateStatusDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatConversationView(
    chat: FellowshipChat,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onStartVoiceCall: (name: String, role: String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var showShareFileDialog by remember { mutableStateOf(false) }
    var showVersePickerDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(chat.avatarInitials, color = GoldDark, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = chat.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (chat.isChannel) "${chat.membersCount} members • Online" else "Active in Fellowship",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnlineGreen
                        )
                    }

                    // Voice Fellowship Call Button
                    IconButton(
                        onClick = { onStartVoiceCall(chat.name, if (chat.isChannel) "Global Prayer Channel" else "Servant of God") },
                        modifier = Modifier.testTag("start_voice_call_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = MaterialTheme.colorScheme.primary)
                    }

                    // Video Fellowship Call Button
                    IconButton(
                        onClick = { onStartVoiceCall(chat.name, "Video Prayer Session") }
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubbleItem(msg = msg)
                }
            }

            // Input / Recorder Bottom Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    if (isRecordingVoice) {
                        VoiceMessageRecorder(
                            onSend = { durationSeconds ->
                                isRecordingVoice = false
                                MinistryRepository.sendMessage(
                                    chatId = chat.id,
                                    type = MessageType.VOICE_NOTE,
                                    text = "Voice Prayer Message",
                                    voiceDurationSeconds = durationSeconds
                                )
                            },
                            onCancel = { isRecordingVoice = false }
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // File attachment button
                            IconButton(
                                onClick = { showShareFileDialog = true },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachFile,
                                    contentDescription = "Share File / Bulletin",
                                    tint = GoldPrimary
                                )
                            }

                            // Bible verse attach button
                            IconButton(
                                onClick = { showVersePickerDialog = true },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = "Send Scripture Verse",
                                    tint = PeaceOlive
                                )
                            }

                            // Text Input
                            OutlinedTextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                placeholder = { Text("Message in Christ...", fontSize = 13.sp) },
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp),
                                maxLines = 4
                            )

                            if (textInput.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        MinistryRepository.sendMessage(
                                            chatId = chat.id,
                                            type = MessageType.TEXT,
                                            text = textInput.trim()
                                        )
                                        textInput = ""
                                    },
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary)
                                        .testTag("send_chat_message_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Send Message",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else {
                                // Mic button to start recording voice message
                                IconButton(
                                    onClick = { isRecordingVoice = true },
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(GoldContainer)
                                        .testTag("record_voice_message_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Record Voice Message",
                                        tint = GoldDark,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Share File Dialog (Bible study PDF, hymn sheet, bulletin)
    if (showShareFileDialog) {
        ShareFileDialog(
            onDismiss = { showShareFileDialog = false },
            onShare = { fileName, fileSize ->
                MinistryRepository.sendMessage(
                    chatId = chat.id,
                    type = MessageType.FILE_ATTACHMENT,
                    text = "Shared document: $fileName",
                    fileName = fileName,
                    fileSize = fileSize
                )
                showShareFileDialog = false
            }
        )
    }

    // Send Bible Verse Dialog
    if (showVersePickerDialog) {
        SendBibleVerseDialog(
            onDismiss = { showVersePickerDialog = false },
            onSendVerse = { ref, text ->
                MinistryRepository.sendMessage(
                    chatId = chat.id,
                    type = MessageType.BIBLE_VERSE,
                    text = text,
                    scriptureRef = ref
                )
                showVersePickerDialog = false
            }
        )
    }
}

@Composable
fun ChatBubbleItem(msg: ChatMessage) {
    val isMe = msg.isFromMe

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        if (!isMe) {
            Text(
                text = "${msg.senderName} (${msg.senderRole})",
                style = MaterialTheme.typography.labelSmall.copy(color = GoldDark, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(if (isMe) GoldPrimary else OffWhiteSurface)
                .padding(12.dp)
        ) {
            when (msg.messageType) {
                MessageType.TEXT -> {
                    Text(
                        text = msg.text,
                        color = if (isMe) Color.White else TextPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                MessageType.VOICE_NOTE -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isMe) Color.White.copy(alpha = 0.25f) else GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Voice Note",
                                tint = if (isMe) Color.White else GoldDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Voice Note (${msg.voiceDurationSeconds}s)",
                                color = if (isMe) Color.White else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Sacred Prayer Audio",
                                color = if (isMe) Color.White.copy(alpha = 0.8f) else TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                MessageType.FILE_ATTACHMENT -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = if (isMe) Color.White else GoldPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = msg.fileName ?: "Document.pdf",
                                color = if (isMe) Color.White else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${msg.fileSize ?: "1.2 MB"} • PDF Church Document",
                                color = if (isMe) Color.White.copy(alpha = 0.8f) else TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                MessageType.BIBLE_VERSE -> {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = if (isMe) GoldContainer else GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = msg.scriptureRef ?: "Scripture Verse",
                                color = if (isMe) Color.White else GoldDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = msg.text,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Serif,
                                color = if (isMe) Color.White else TextPrimary
                            )
                        )
                    }
                }
            }
        }

        Text(
            text = msg.timestamp,
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun ShareFileDialog(
    onDismiss: () -> Unit,
    onShare: (fileName: String, fileSize: String) -> Unit
) {
    var selectedFile by remember { mutableStateOf("Sunday_Prayer_Bulletin.pdf") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachFile, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Church File", style = MaterialTheme.typography.titleMedium)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select a document to share with the fellowship community:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                listOf(
                    "Sunday_Prayer_Bulletin.pdf" to "1.8 MB",
                    "Discipleship_Study_Guide_Week4.pdf" to "3.2 MB",
                    "Hymnal_Lyrics_Songbook.pdf" to "4.5 MB",
                    "Global_Missions_Outreach_Report.pdf" to "2.1 MB"
                ).forEach { (name, size) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedFile = name },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedFile == name) GoldContainer else OffWhiteSurface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = SacredCrimson)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(size, fontSize = 10.sp, color = TextMuted)
                            }
                            if (selectedFile == name) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = GoldDark)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onShare(selectedFile, "2.4 MB") },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Text("Share File")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun SendBibleVerseDialog(
    onDismiss: () -> Unit,
    onSendVerse: (ref: String, text: String) -> Unit
) {
    val verses = listOf(
        "Philippians 4:7" to "“And the peace of God, which passeth all understanding, shall keep your hearts and minds through Christ Jesus.”",
        "Psalm 23:1" to "“The Lord is my shepherd; I shall not want.”",
        "John 14:27" to "“Peace I leave with you, my peace I give unto you: not as the world giveth, give I unto you. Let not your heart be troubled, neither let it be afraid.”",
        "Isaiah 40:31" to "“But they that wait upon the Lord shall renew their strength; they shall mount up with wings as eagles; they shall run, and not be weary; and they shall walk, and not faint.”"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoStories, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send Scripture Encouragement", style = MaterialTheme.typography.titleMedium)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                verses.forEach { (ref, text) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSendVerse(ref, text) },
                        colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(ref, fontWeight = FontWeight.Bold, color = GoldDark, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text, fontSize = 11.sp, fontFamily = FontFamily.Serif, color = TextPrimary)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
