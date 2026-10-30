package com.example.model

import java.util.UUID

enum class MessageType {
    TEXT,
    VOICE_NOTE,
    FILE_ATTACHMENT,
    BIBLE_VERSE
}

data class AudioPrayer(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val scriptureRef: String,
    val scriptureText: String,
    val reflectionAudioUrl: String = "",
    val durationSeconds: Int = 300,
    val narrator: String = "Rev. Emmanuel Peace",
    val reflectionPrompts: List<String> = emptyList(),
    val prayerClosing: String = "In the name of the Father, and of the Son, and of the Holy Spirit. Amen.",
    val category: String = "Pray As You Go Daily",
    val dateText: String = "Today",
    val isFavorite: Boolean = false,
    val isUserUploaded: Boolean = false
)

data class WorshipSong(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val artist: String,
    val album: String = "Sacred Peace Vol. 1",
    val durationSeconds: Int = 245,
    val lyrics: String,
    val category: String = "Sacred Worship",
    val audioUrl: String = "",
    val isFavorite: Boolean = false,
    val isUserUploaded: Boolean = false
)

data class MinistryVideo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val preacher: String,
    val ministryGroup: String = "Global Shalom Outreach",
    val scriptureRef: String,
    val durationText: String = "28:15",
    val videoUrl: String = "",
    val description: String,
    val sermonNotes: String,
    val viewCount: Int = 1240,
    val uploadedAt: String = "Yesterday",
    val isUserUploaded: Boolean = false
)

data class LivePreachingSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val preacherName: String,
    val ministryRole: String = "Lead Pastor & Evangelist",
    val scriptureTheme: String,
    val isLive: Boolean = true,
    val viewerCount: Int = 342,
    val startTimeText: String = "Live Now",
    val roomCode: String = "shalom-preach-live"
)

data class LiveComment(
    val id: String = UUID.randomUUID().toString(),
    val senderName: String,
    val text: String,
    val time: String = "Just now",
    val isAmen: Boolean = false
)

data class FellowshipChat(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isChannel: Boolean = false,
    val unreadCount: Int = 0,
    val lastMessageText: String = "",
    val lastMessageTime: String = "10:30 AM",
    val membersCount: Int = 1,
    val avatarInitials: String = "SH"
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val chatId: String,
    val senderName: String,
    val senderRole: String = "Disciple",
    val isFromMe: Boolean = false,
    val messageType: MessageType = MessageType.TEXT,
    val text: String = "",
    val timestamp: String = "10:30 AM",
    val voiceDurationSeconds: Int = 0,
    val fileName: String? = null,
    val fileSize: String? = null,
    val scriptureRef: String? = null
)

data class MinistryStatus(
    val id: String = UUID.randomUUID().toString(),
    val authorName: String,
    val authorRole: String = "Intercessor",
    val timeAgo: String = "2 hours ago",
    val textContent: String,
    val scriptureVerse: String? = null,
    val hasPhoto: Boolean = false,
    val isSeen: Boolean = false
)

data class UserSettings(
    val userName: String = "Minister of the Gospel",
    val userRole: String = "Ministry Outreach Leader",
    val churchOrMinistry: String = "Shalom International Fellowship",
    val dailyReminderTime: String = "06:30 AM",
    val chimeAtPrayerEnd: Boolean = true,
    val audioAutoPlayNext: Boolean = false,
    val liveDefaultCameraFront: Boolean = true,
    val liveNoiseCancellation: Boolean = true,
    val highQualityAudio: Boolean = true,
    val isDarkMode: Boolean = false
)
