package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MinistryRepository
import com.example.model.AudioPrayer
import com.example.model.MinistryVideo
import com.example.model.WorshipSong
import com.example.ui.theme.*

@Composable
fun UploadPrayerAudioDialog(
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var scriptureRef by remember { mutableStateOf("") }
    var scriptureText by remember { mutableStateOf("") }
    var narrator by remember { mutableStateOf(MinistryRepository.settings.value.userName) }
    var prompt1 by remember { mutableStateOf("Take a quiet breath and rest in the loving presence of Christ.") }
    var prompt2 by remember { mutableStateOf("What word or phrase in this passage speaks most directly to your current walk?") }
    var prompt3 by remember { mutableStateOf("Speak to God from the quiet depths of your soul.") }
    var closingPrayer by remember { mutableStateOf("Lord Jesus, may Thy peace guard our hearts and minds today. Amen.") }
    var selectedCategory by remember { mutableStateOf("Pray As You Go Daily") }
    var durationMinutes by remember { mutableIntStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AudioFile, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Pray As You Go Audio & Text", style = MaterialTheme.typography.titleMedium)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Publish a daily prayer reflection with synchronized scripture and contemplative prompts just like Pray As You Go.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Contemplation Title (e.g. Perfect Peace)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("prayer_title_input")
                )

                OutlinedTextField(
                    value = scriptureRef,
                    onValueChange = { scriptureRef = it },
                    label = { Text("Scripture Reference (e.g. Isaiah 26:3)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("prayer_scripture_ref_input")
                )

                OutlinedTextField(
                    value = scriptureText,
                    onValueChange = { scriptureText = it },
                    label = { Text("Scripture Passage Text") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("prayer_scripture_text_input")
                )

                OutlinedTextField(
                    value = narrator,
                    onValueChange = { narrator = it },
                    label = { Text("Narrator / Speaker Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Contemplation Reflection Prompts:", style = MaterialTheme.typography.labelMedium, color = GoldDark)
                OutlinedTextField(
                    value = prompt1,
                    onValueChange = { prompt1 = it },
                    label = { Text("Step 1: Sacred Opening") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = prompt2,
                    onValueChange = { prompt2 = it },
                    label = { Text("Step 2: Meditation Question") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = prompt3,
                    onValueChange = { prompt3 = it },
                    label = { Text("Step 3: Personal Prayer") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = closingPrayer,
                    onValueChange = { closingPrayer = it },
                    label = { Text("Concluding Benediction Prayer") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                // Category selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == "Pray As You Go Daily",
                        onClick = { selectedCategory = "Pray As You Go Daily" },
                        label = { Text("Daily", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedCategory == "Morning Lectio Divina",
                        onClick = { selectedCategory = "Morning Lectio Divina" },
                        label = { Text("Morning", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedCategory == "Evening Compline",
                        onClick = { selectedCategory = "Evening Compline" },
                        label = { Text("Evening", fontSize = 11.sp) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && scriptureText.isNotBlank()) {
                        val newPrayer = AudioPrayer(
                            title = title.trim(),
                            scriptureRef = scriptureRef.ifBlank { "Scripture Reading" },
                            scriptureText = scriptureText.trim(),
                            durationSeconds = durationMinutes * 60,
                            narrator = narrator.ifBlank { "Minister" },
                            reflectionPrompts = listOf(prompt1, prompt2, prompt3).filter { it.isNotBlank() },
                            prayerClosing = closingPrayer,
                            category = selectedCategory,
                            dateText = "Uploaded Today",
                            isFavorite = false,
                            isUserUploaded = true
                        )
                        MinistryRepository.addPrayerAudio(newPrayer)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                modifier = Modifier.testTag("publish_prayer_audio_button")
            ) {
                Text("Publish Prayer Audio")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun UploadWorshipSongDialog(
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var album by remember { mutableStateOf("Grace & Peace Collection") }
    var category by remember { mutableStateOf("Sacred Worship") }
    var lyrics by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableIntStateOf(4) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LibraryMusic, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Praise & Worship Song", style = MaterialTheme.typography.titleMedium)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Share praise, gospel, and adoration music with lyrics for the community to worship together.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Song Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("worship_title_input")
                )

                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Artist / Choir / Worship Team") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = album,
                    onValueChange = { album = it },
                    label = { Text("Album Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lyrics,
                    onValueChange = { lyrics = it },
                    label = { Text("Song Lyrics (Verses & Chorus)") },
                    minLines = 4,
                    maxLines = 8,
                    placeholder = { Text("[Verse 1]\nGrace that is greater than all our sin...\n\n[Chorus]\nMarvelous, infinite grace...") },
                    modifier = Modifier.fillMaxWidth().testTag("worship_lyrics_input")
                )

                Text("Worship Category:", style = MaterialTheme.typography.labelMedium, color = GoldDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = category == "Sacred Worship",
                        onClick = { category = "Sacred Worship" },
                        label = { Text("Worship", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = category == "Praise & Thanksgiving",
                        onClick = { category = "Praise & Thanksgiving" },
                        label = { Text("Praise", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = category == "Acoustic Adoration",
                        onClick = { category = "Acoustic Adoration" },
                        label = { Text("Acoustic", fontSize = 11.sp) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newSong = WorshipSong(
                            title = title.trim(),
                            artist = artist.ifBlank { "Shalom Worship Team" },
                            album = album,
                            durationSeconds = durationMinutes * 60,
                            lyrics = lyrics.ifBlank { "Praise the Lord, all ye nations; praise Him, all ye people!\nFor His merciful kindness is great toward us, and the truth of the Lord endureth forever." },
                            category = category,
                            isFavorite = false,
                            isUserUploaded = true
                        )
                        MinistryRepository.addWorshipSong(newSong)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                modifier = Modifier.testTag("publish_worship_song_button")
            ) {
                Text("Upload Song")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun UploadMinistryVideoDialog(
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var preacher by remember { mutableStateOf("") }
    var scriptureRef by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var sermonNotes by remember { mutableStateOf("") }
    var durationText by remember { mutableStateOf("25:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Christian Video Sermon", style = MaterialTheme.typography.titleMedium)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Upload Christian sermons, documentaries, and youth teachings for believers worldwide to watch.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Video Sermon Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("video_title_input")
                )

                OutlinedTextField(
                    value = preacher,
                    onValueChange = { preacher = it },
                    label = { Text("Preacher / Speaker") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = scriptureRef,
                    onValueChange = { scriptureRef = it },
                    label = { Text("Scripture Reference (e.g., Romans 8:28)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Sermon Description & Synopsis") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sermonNotes,
                    onValueChange = { sermonNotes = it },
                    label = { Text("Study Guide & Sermon Notes") },
                    minLines = 3,
                    maxLines = 6,
                    placeholder = { Text("Point 1: God is in control...\nPoint 2: Walking in righteousness...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newVideo = MinistryVideo(
                            title = title.trim(),
                            preacher = preacher.ifBlank { MinistryRepository.settings.value.userName },
                            ministryGroup = MinistryRepository.settings.value.churchOrMinistry,
                            scriptureRef = scriptureRef.ifBlank { "General Exhortation" },
                            durationText = durationText,
                            description = description.ifBlank { "An uplifting Christian ministry sermon for spiritual growth." },
                            sermonNotes = sermonNotes.ifBlank { "Study Notes:\n1. Keep your faith rooted in Christ Jesus.\n2. Seek first the Kingdom of God." },
                            viewCount = 1,
                            uploadedAt = "Just now",
                            isUserUploaded = true
                        )
                        MinistryRepository.addMinistryVideo(newVideo)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                modifier = Modifier.testTag("publish_video_button")
            ) {
                Text("Publish Video")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
