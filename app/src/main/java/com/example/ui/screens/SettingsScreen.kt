package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.UserSettings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val currentSettings by MinistryRepository.settings.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showGivingDialog by remember { mutableStateOf(false) }
    var showPrayerHotlineDialog by remember { mutableStateOf(false) }
    var showFaithStatementDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ministry Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(GoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentSettings.userName.take(2).uppercase(),
                            color = GoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentSettings.userName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = currentSettings.userRole,
                            style = MaterialTheme.typography.bodySmall.copy(color = PeaceOlive)
                        )
                        Text(
                            text = currentSettings.churchOrMinistry,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    IconButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = GoldPrimary)
                    }
                }
            }

            // Section 1: Pray As You Go & Audio Reflection Settings
            SettingsSectionHeader(title = "Pray As You Go & Devotionals")
            Card(
                colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Monastic Prayer Chime", fontWeight = FontWeight.SemiBold)
                            Text("Play gentle bell chime at prayer meditation pauses", fontSize = 12.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = currentSettings.chimeAtPrayerEnd,
                            onCheckedChange = {
                                MinistryRepository.updateSettings(currentSettings.copy(chimeAtPrayerEnd = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldContainer)
                        )
                    }

                    Divider(color = OffWhiteBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto-Advance Devotionals", fontWeight = FontWeight.SemiBold)
                            Text("Automatically proceed to next contemplation", fontSize = 12.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = currentSettings.audioAutoPlayNext,
                            onCheckedChange = {
                                MinistryRepository.updateSettings(currentSettings.copy(audioAutoPlayNext = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldContainer)
                        )
                    }

                    Divider(color = OffWhiteBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("High Quality Spiritual Audio", fontWeight = FontWeight.SemiBold)
                            Text("Lossless choral & acoustic audio playback", fontSize = 12.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = currentSettings.highQualityAudio,
                            onCheckedChange = {
                                MinistryRepository.updateSettings(currentSettings.copy(highQualityAudio = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldContainer)
                        )
                    }
                }
            }

            // Section 2: Live Preaching & Google Meet Style Settings
            SettingsSectionHeader(title = "Live Preaching & Video Sanctuary")
            Card(
                colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Default to Front Camera", fontWeight = FontWeight.SemiBold)
                            Text("Open selfie camera upon launching live preach room", fontSize = 12.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = currentSettings.liveDefaultCameraFront,
                            onCheckedChange = {
                                MinistryRepository.updateSettings(currentSettings.copy(liveDefaultCameraFront = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldContainer)
                        )
                    }

                    Divider(color = OffWhiteBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sanctuary Noise Cancellation", fontWeight = FontWeight.SemiBold)
                            Text("Filter background noise for crystal-clear preaching", fontSize = 12.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = currentSettings.liveNoiseCancellation,
                            onCheckedChange = {
                                MinistryRepository.updateSettings(currentSettings.copy(liveNoiseCancellation = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldContainer)
                        )
                    }
                }
            }

            // Section 3: Daily Prayer Watches & Notifications
            SettingsSectionHeader(title = "Sacred Prayer Watches")
            Card(
                colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Morning Dawn Watch", fontWeight = FontWeight.SemiBold)
                        }
                        Text("06:00 AM", color = GoldDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Divider(color = OffWhiteBorder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Brightness5, contentDescription = null, tint = SacredAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Noonday Peace Intercession", fontWeight = FontWeight.SemiBold)
                        }
                        Text("12:00 PM", color = GoldDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Divider(color = OffWhiteBorder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NightsStay, contentDescription = null, tint = PeaceOlive, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Evening Compline Reflection", fontWeight = FontWeight.SemiBold)
                        }
                        Text("09:00 PM", color = GoldDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Section 4: Ministry Outreach & Giving
            SettingsSectionHeader(title = "Kingdom Outreach & Fellowship")
            Card(
                colors = CardDefaults.cardColors(containerColor = OffWhiteSurface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Tithes & Offerings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showGivingDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = GoldPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tithes, Offerings & Missions Giving", fontWeight = FontWeight.SemiBold)
                            Text("Support global Bible translation, crusades & charity", fontSize = 12.sp, color = TextSecondary)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                    }

                    Divider(color = OffWhiteBorder)

                    // 24/7 Prayer Hotline
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrayerHotlineDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = PeaceOlive)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("24/7 Intercession & Prayer Hotline", fontWeight = FontWeight.SemiBold)
                            Text("Submit urgent prayer requests to pastoral staff", fontSize = 12.sp, color = TextSecondary)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                    }

                    Divider(color = OffWhiteBorder)

                    // Statement of Faith
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showFaithStatementDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AutoStories, contentDescription = null, tint = GoldDark)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Statement of Christian Faith", fontWeight = FontWeight.SemiBold)
                            Text("Core biblical doctrines & Shalom ministry vision", fontSize = 12.sp, color = TextSecondary)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var name by remember { mutableStateOf(currentSettings.userName) }
        var role by remember { mutableStateOf(currentSettings.userRole) }
        var ministry by remember { mutableStateOf(currentSettings.churchOrMinistry) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Ministry Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Spiritual Role (e.g. Pastor, Minister, Worship Leader)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ministry,
                        onValueChange = { ministry = it },
                        label = { Text("Ministry / Fellowship Affiliation") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        MinistryRepository.updateSettings(
                            currentSettings.copy(
                                userName = name.ifBlank { "Servant of God" },
                                userRole = role.ifBlank { "Disciple" },
                                churchOrMinistry = ministry.ifBlank { "Shalom Fellowship" }
                            )
                        )
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Giving Portal Dialog
    if (showGivingDialog) {
        var amount by remember { mutableStateOf("50") }
        var fund by remember { mutableStateOf("Global Missions & Crusades") }
        var isCompleted by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showGivingDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = GoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kingdom Seed & Offering")
                }
            },
            text = {
                if (isCompleted) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OnlineGreen, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "“God loveth a cheerful giver.”",
                            style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif, color = GoldDark)
                        )
                        Text(
                            text = "Thank you for sowing $$amount into $fund! A receipt has been generated.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "“Give, and it shall be given unto you; good measure, pressed down, and shaken together, and running over.” — Luke 6:38",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Serif, color = PeaceOlive)
                        )
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Offering Amount ($ USD)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Select Fund Purpose:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = fund == "Missions",
                                onClick = { fund = "Missions" },
                                label = { Text("Missions", fontSize = 11.sp) }
                            )
                            FilterChip(
                                selected = fund == "Orphans",
                                onClick = { fund = "Orphans" },
                                label = { Text("Charity", fontSize = 11.sp) }
                            )
                            FilterChip(
                                selected = fund == "Crusades",
                                onClick = { fund = "Crusades" },
                                label = { Text("Crusades", fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (isCompleted) {
                    Button(onClick = { showGivingDialog = false }) { Text("Amen") }
                } else {
                    Button(
                        onClick = { isCompleted = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text("Sow Seed ($$amount)")
                    }
                }
            },
            dismissButton = {
                if (!isCompleted) {
                    TextButton(onClick = { showGivingDialog = false }) { Text("Close") }
                }
            }
        )
    }

    // Prayer Hotline Dialog
    if (showPrayerHotlineDialog) {
        var petitionText by remember { mutableStateOf("") }
        var isSent by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showPrayerHotlineDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = PeaceOlive)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Urgent Prayer Request")
                }
            },
            text = {
                if (isSent) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = SacredCrimson, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Petition Received in Faith",
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        )
                        Text(
                            text = "The 24/7 Global Intercessors altar has taken your petition before the Throne of Grace. Walk in peace!",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Share your prayer petition, healing need, or family breakthrough request:",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = petitionText,
                            onValueChange = { petitionText = it },
                            label = { Text("Your Prayer Petition") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                if (isSent) {
                    Button(onClick = { showPrayerHotlineDialog = false }) { Text("Amen") }
                } else {
                    Button(
                        onClick = { if (petitionText.isNotBlank()) isSent = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PeaceOlive)
                    ) {
                        Text("Submit to Altar")
                    }
                }
            },
            dismissButton = {
                if (!isSent) {
                    TextButton(onClick = { showPrayerHotlineDialog = false }) { Text("Cancel") }
                }
            }
        )
    }

    // Statement of Faith Dialog
    if (showFaithStatementDialog) {
        AlertDialog(
            onDismissRequest = { showFaithStatementDialog = false },
            title = {
                Text(
                    text = "Shalom Outreach Statement of Faith",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = GoldDark
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "1. The Holy Scriptures",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "We believe the Holy Bible to be the inspired, infallible, and authoritative Word of God.",
                        fontSize = 12.sp
                    )

                    Text(
                        text = "2. The Triune Godhead",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "There is one God, eternally existent in three persons: Father, Son, and Holy Spirit.",
                        fontSize = 12.sp
                    )

                    Text(
                        text = "3. Salvation in Jesus Christ",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Salvation is by grace through faith in the finished work of Jesus Christ on the Cross of Calvary.",
                        fontSize = 12.sp
                    )

                    Text(
                        text = "4. Global Ministry Outreach",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "We are commanded to preach the Gospel to every creature and publish the peace of Christ worldwide.",
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showFaithStatementDialog = false }) { Text("Amen") }
            }
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(start = 4.dp, top = 6.dp)
    )
}
