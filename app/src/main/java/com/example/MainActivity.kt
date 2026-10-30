package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LivePreachingRoomDialog
import com.example.ui.components.ShalomTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.*

enum class MinistryNavDestination(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    REFLECT("Reflect", Icons.Filled.Headphones, Icons.Outlined.Headphones, "nav_reflect"),
    PREACH("Preach", Icons.Filled.Videocam, Icons.Outlined.Videocam, "nav_preach"),
    WORSHIP("Worship", Icons.Filled.MusicNote, Icons.Outlined.MusicNote, "nav_worship"),
    VIDEOS("Videos", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircleOutline, "nav_videos"),
    FELLOWSHIP("Fellowship", Icons.Filled.Forum, Icons.Outlined.Forum, "nav_fellowship"),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShalomMinistryTheme {
                MainAppScaffold()
            }
        }
    }
}

@Composable
fun MainAppScaffold() {
    var currentDestination by remember { mutableStateOf(MinistryNavDestination.REFLECT) }
    var quickLaunchLiveRoom by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ShalomTopAppBar(
                title = "Shalom Ministry",
                subtitle = when (currentDestination) {
                    MinistryNavDestination.REFLECT -> "Pray As You Go • Daily Contemplation"
                    MinistryNavDestination.PREACH -> "Live Online Preaching Sanctuary"
                    MinistryNavDestination.WORSHIP -> "Praise & Worship Music Vault"
                    MinistryNavDestination.VIDEOS -> "Sermons & Bible Teachings"
                    MinistryNavDestination.FELLOWSHIP -> "Global Believers Community"
                    MinistryNavDestination.SETTINGS -> "Ministry Outreach Preferences"
                },
                onLiveClick = { currentDestination = MinistryNavDestination.PREACH }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                MinistryNavDestination.values().forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = destination.title
                            )
                        },
                        label = {
                            Text(
                                text = destination.title,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldDark,
                            selectedTextColor = GoldDark,
                            indicatorColor = GoldContainer,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(destination.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                MinistryNavDestination.REFLECT -> {
                    PrayAsYouGoScreen(
                        onNavigateToLive = { currentDestination = MinistryNavDestination.PREACH }
                    )
                }
                MinistryNavDestination.PREACH -> {
                    LivePreachScreen()
                }
                MinistryNavDestination.WORSHIP -> {
                    WorshipScreen()
                }
                MinistryNavDestination.VIDEOS -> {
                    WatchVideosScreen()
                }
                MinistryNavDestination.FELLOWSHIP -> {
                    CommunityChatScreen(
                        onNavigateToStatuses = { /* stay in fellowship tab */ }
                    )
                }
                MinistryNavDestination.SETTINGS -> {
                    SettingsScreen()
                }
            }
        }
    }
}
