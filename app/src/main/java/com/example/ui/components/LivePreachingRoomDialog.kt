package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.data.MinistryRepository
import com.example.model.LiveComment
import com.example.model.LivePreachingSession
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LivePreachingRoomDialog(
    session: LivePreachingSession,
    isBroadcaster: Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Google Meet controls state
    var isMicMuted by remember { mutableStateOf(false) }
    var isVideoMuted by remember { mutableStateOf(!isBroadcaster) }
    var isFrontCamera by remember { mutableStateOf(true) }
    var isHandRaised by remember { mutableStateOf(false) }
    var showChatDrawer by remember { mutableStateOf(false) }
    var showScriptureSlide by remember { mutableStateOf(false) }
    var commentInput by remember { mutableStateOf("") }
    var elapsedSeconds by remember { mutableIntStateOf(142) }

    // Floating reaction triggers
    var reactionCount by remember { mutableIntStateOf(0) }

    val liveComments by MinistryRepository.liveComments.collectAsState()
    val scope = rememberCoroutineScope()
    val chatListState = rememberLazyListState()

    // Timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            elapsedSeconds++
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101010))
        ) {
            // Main Stage: Video View / Camera Stream
            if (!isVideoMuted && hasCameraPermission) {
                // Real Android CameraX Preview
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }
                            val cameraSelector = if (isFrontCamera) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { previewView ->
                        // Rebind on camera flip
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }
                            val cameraSelector = if (isFrontCamera) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(previewView.context))
                    }
                )
            } else {
                // Sanctified Stage Graphic (when camera is off or viewing guest stream)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF231E17), Color(0xFF14120F), Color(0xFF0A0908))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f))
                                .border(2.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(90.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = session.preacherName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                color = Color.White
                            )
                        )
                        Text(
                            text = session.ministryRole,
                            style = MaterialTheme.typography.bodyMedium.copy(color = GoldLight)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(PeaceOlive.copy(alpha = 0.3f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Speaking: “${session.scriptureTheme}”",
                                color = OffWhiteBg,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Top Overlay: Google Meet / Sanctuary Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SacredCrimson)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${session.viewerCount + 1}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val mins = elapsedSeconds / 60
                    val secs = elapsedSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Scripture Presentation Slide toggle
                    IconButton(
                        onClick = { showScriptureSlide = !showScriptureSlide },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (showScriptureSlide) GoldPrimary else Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = "Scripture Slide",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Flip Camera (only if video active)
                    if (!isVideoMuted && hasCameraPermission) {
                        IconButton(
                            onClick = { isFrontCamera = !isFrontCamera },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = "Flip Camera",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Chat drawer toggle
                    IconButton(
                        onClick = { showChatDrawer = !showChatDrawer },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (showChatDrawer) GoldPrimary else Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Live Chat",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Scripture Overlay Slide (Optional preacher aid)
            AnimatedVisibility(
                visible = showScriptureSlide,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 70.dp, start = 20.dp, end = 20.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OffWhiteBg.copy(alpha = 0.95f)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SERMON PASSAGE • ${session.scriptureTheme}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldDark
                            )
                            IconButton(
                                onClick = { showScriptureSlide = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“Peace I leave with you, my peace I give unto you: not as the world giveth, give I unto you. Let not your heart be troubled, neither let it be afraid.”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Serif,
                                color = TextPrimary
                            )
                        )
                    }
                }
            }

            // Floating Reactions Burst (Amens, Doves, Hearts)
            if (reactionCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 120.dp, end = 24.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "🕊️", fontSize = 32.sp)
                        Text(text = "✝️", fontSize = 28.sp)
                        Text(text = "🙏", fontSize = 30.sp)
                        Text(text = "✨", fontSize = 26.sp)
                    }
                }
            }

            // Live Chat Overlay / Comments at Bottom Left
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 100.dp, end = 100.dp)
                    .fillMaxWidth(0.8f)
            ) {
                LazyColumn(
                    state = chatListState,
                    modifier = Modifier.heightIn(max = 180.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(liveComments.takeLast(5)) { comment ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.55f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${comment.senderName}: ",
                                    color = if (comment.isAmen) GoldLight else Color(0xFFC7D5E0),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = comment.text,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Google Meet Style Action Toolbar
            Surface(
                color = Color.Black.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mic Mute / Unmute
                    IconButton(
                        onClick = { isMicMuted = !isMicMuted },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isMicMuted) SacredCrimson else Color(0xFF333333))
                    ) {
                        Icon(
                            imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Microphone",
                            tint = Color.White
                        )
                    }

                    // Video Camera Toggle
                    IconButton(
                        onClick = { isVideoMuted = !isVideoMuted },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isVideoMuted) SacredCrimson else Color(0xFF333333))
                    ) {
                        Icon(
                            imageVector = if (isVideoMuted) Icons.Default.VideocamOff else Icons.Default.Videocam,
                            contentDescription = "Video Camera",
                            tint = Color.White
                        )
                    }

                    // Raise Hand for Prayer / Question
                    IconButton(
                        onClick = { isHandRaised = !isHandRaised },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isHandRaised) GoldPrimary else Color(0xFF333333))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PanTool,
                            contentDescription = "Raise Hand",
                            tint = Color.White
                        )
                    }

                    // "Amen" Reaction Burst Button
                    FilledTonalButton(
                        onClick = {
                            reactionCount++
                            MinistryRepository.addLiveComment(
                                LiveComment(
                                    senderName = "You",
                                    text = "Amen! Hallelujah!",
                                    isAmen = true
                                )
                            )
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("AMEN! 🙏", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    // End Preaching / Leave Live
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(SacredCrimson)
                            .testTag("end_live_preach_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "Leave Livestream",
                            tint = Color.White
                        )
                    }
                }
            }

            // Slide-over Chat Box when opened
            AnimatedVisibility(
                visible = showChatDrawer,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it }),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f)
            ) {
                Surface(
                    color = OffWhiteBg,
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Live Fellowship Prayer Chat",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            IconButton(onClick = { showChatDrawer = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close Chat")
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = OffWhiteBorder)

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(liveComments) { c ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (c.isAmen) GoldContainer else OffWhiteSurface
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = c.senderName,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = GoldDark,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = c.text,
                                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = commentInput,
                                onValueChange = { commentInput = it },
                                placeholder = { Text("Share prayer request or amen...", fontSize = 12.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = {
                                    if (commentInput.isNotBlank()) {
                                        MinistryRepository.addLiveComment(
                                            LiveComment(
                                                senderName = "You",
                                                text = commentInput.trim(),
                                                isAmen = commentInput.contains("amen", ignoreCase = true)
                                            )
                                        )
                                        commentInput = ""
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = GoldPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
