package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object MinistryRepository {

    // --- Pray As You Go Audio Sessions ---
    private val _prayers = MutableStateFlow<List<AudioPrayer>>(
        listOf(
            AudioPrayer(
                id = "prayer-1",
                title = "Be Still and Know: Embracing Divine Peace",
                scriptureRef = "Psalm 46:10-11",
                scriptureText = "“Be still, and know that I am God: I will be exalted among the heathen, I will be exalted in the earth. The Lord of hosts is with us; the God of Jacob is our refuge.”",
                durationSeconds = 480,
                narrator = "Pastor Grace Sterling",
                reflectionPrompts = listOf(
                    "Welcome into this sacred pause. Take three deep breaths, releasing your worldly anxieties into the Father's gentle hands.",
                    "As you hear the sacred words of the Psalmist, what turbulence in your spirit is Christ inviting you to release?",
                    "Listen to the stillness. God is not in the violent storm or the rushing wind, but in the whisper of divine grace.",
                    "Speak to the Lord right now in your own heart, offering whatever burden weighs heaviest upon you."
                ),
                prayerClosing = "Lord Jesus Christ, Prince of Peace, quiet our troubled hearts. Anchor our souls in Thy unfailing presence today and forevermore. Amen.",
                category = "Pray As You Go Daily",
                dateText = "Today's Contemplation",
                isFavorite = true
            ),
            AudioPrayer(
                id = "prayer-2",
                title = "Walking on Water: Overcoming Fear with Faith",
                scriptureRef = "Matthew 14:27-31",
                scriptureText = "“But straightway Jesus spake unto them, saying, Be of good cheer; it is I; be not afraid. And Peter answered him and said, Lord, if it be thou, bid me come unto thee on the water. And he said, Come.”",
                durationSeconds = 540,
                narrator = "Rev. Emmanuel Peace",
                reflectionPrompts = listOf(
                    "Picture the stormy sea of Galilee in the fourth watch of the night. Where in your life do the waves feel too high?",
                    "Jesus steps across the waves and speaks directly to you: 'Be of good cheer; it is I; be not afraid.'",
                    "What step of faith is the Spirit asking you to take out of the boat today?",
                    "Even when Peter began to sink, the outstretched hand of the Master caught him immediately."
                ),
                prayerClosing = "O Master of the winds and the waves, stretch forth Thy righteous right hand and steady our faltering steps. Glory be to Thy name. Amen.",
                category = "Morning Lectio Divina",
                dateText = "Yesterday",
                isFavorite = false
            ),
            AudioPrayer(
                id = "prayer-3",
                title = "The Lord Is My Shepherd: Green Pastures and Still Waters",
                scriptureRef = "Psalm 23:1-3",
                scriptureText = "“The Lord is my shepherd; I shall not want. He maketh me to lie down in green pastures: he leadeth me beside the still waters. He restoreth my soul: he leadeth me in the paths of righteousness for his name's sake.”",
                durationSeconds = 420,
                narrator = "Sister Mary Chidinma",
                reflectionPrompts = listOf(
                    "Allow the peaceful image of green pastures to rest over your weary mind.",
                    "The Shepherd knows every sheep by name. He knows your strengths, your bruises, and your deepest longings.",
                    "Rest beside His still waters. Let Him restore the strength of your inner being."
                ),
                prayerClosing = "Good Shepherd, lead us today that we may lack nothing in Thy love. Amen.",
                category = "Evening Compline",
                dateText = "Oct 28",
                isFavorite = true
            )
        )
    )
    val prayers: StateFlow<List<AudioPrayer>> = _prayers.asStateFlow()

    // --- Praise & Worship Vault ---
    private val _worshipSongs = MutableStateFlow<List<WorshipSong>>(
        listOf(
            WorshipSong(
                id = "song-1",
                title = "Shalom Over My Soul",
                artist = "The Jerusalem Choir & Strings",
                album = "Sacred Golden Peace",
                durationSeconds = 295,
                lyrics = """
[Verse 1]
When peace like a river attendeth my way
When sorrows like sea billows roll
Whatever my lot, Thou hast taught me to say
It is well, it is well with my soul

[Chorus]
Shalom, divine Shalom
Pour down like rain upon our home
Prince of Peace, forever reign
All glory to Thy Holy Name!

[Verse 2]
Though Satan should buffet, though trials should come
Let this blest assurance control
That Christ hath regarded my helpless estate
And hath shed His own blood for my soul

[Bridge]
Holy, holy, Lord God Almighty
The heavens declare Thy marvelous grace
In the shelter of Thy presence
We find our dwelling place
                """.trimIndent(),
                category = "Sacred Worship",
                isFavorite = true
            ),
            WorshipSong(
                id = "song-2",
                title = "Way Maker, Miracle Worker",
                artist = "Grace Outreach Collective",
                album = "Living Water Streams",
                durationSeconds = 340,
                lyrics = """
[Verse 1]
You are here, moving in our midst
I worship You, I worship You
You are here, working in this place
I worship You, I worship You

[Chorus]
You are Way maker, miracle worker
Promise keeper, light in the darkness
My God, that is who You are!

[Tag]
Even when I don't see it, You're working
Even when I don't feel it, You're working
You never stop, You never stop working!
                """.trimIndent(),
                category = "Praise & Thanksgiving",
                isFavorite = false
            ),
            WorshipSong(
                id = "song-3",
                title = "Acoustic Prayer: As the Deer Panteth",
                artist = "Davidic Harp & Flute",
                album = "Meditations of David",
                durationSeconds = 230,
                lyrics = """
[Verse 1]
As the deer panteth for the water
So my soul longeth after Thee
You alone are my heart's desire
And I long to worship Thee

[Chorus]
You alone are my strength, my shield
To You alone may my spirit yield
You alone are my heart's desire
And I long to worship Thee
                """.trimIndent(),
                category = "Acoustic Adoration",
                isFavorite = true
            )
        )
    )
    val worshipSongs: StateFlow<List<WorshipSong>> = _worshipSongs.asStateFlow()

    // --- Christian Videos & Sermon Broadcasts ---
    private val _videos = MutableStateFlow<List<MinistryVideo>>(
        listOf(
            MinistryVideo(
                id = "vid-1",
                title = "The Power of Persistent Prayer & Intercession",
                preacher = "Bishop John O. Adeyemi",
                ministryGroup = "Global Outreach Revival",
                scriptureRef = "Luke 18:1-8 & James 5:16",
                durationText = "34:20",
                description = "An inspiring message on breaking spiritual barriers through earnest, fervent, and persistent prayer. Discover how prayers shift nations and heal hearts.",
                sermonNotes = "Key Takeaways:\n1. Prayer is not convincing God; it is aligning our heart with His sovereign will.\n2. Do not faint before your miracle manifests.\n3. The prayer of a righteous person is powerful and effective.",
                viewCount = 3820,
                uploadedAt = "2 days ago"
            ),
            MinistryVideo(
                id = "vid-2",
                title = "Restoring the Altar of Family Worship",
                preacher = "Rev. Ruth MacIntyre",
                ministryGroup = "Shalom Family Ministries",
                scriptureRef = "Joshua 24:15",
                durationText = "26:45",
                description = "Practical biblical wisdom for cultivating faith, unity, and daily devotion within your household in modern turbulent times.",
                sermonNotes = "Three Pillars:\n1. Family Bible altar: 10 minutes every evening.\n2. Blessing your children with scripture.\n3. Walking in unconditional forgiveness at home.",
                viewCount = 2150,
                uploadedAt = "5 days ago"
            ),
            MinistryVideo(
                id = "vid-3",
                title = "Overcoming Anxiety with the Peace of Christ",
                preacher = "Pastor Grace Sterling",
                ministryGroup = "Peace of God Ministry",
                scriptureRef = "Philippians 4:6-7",
                durationText = "21:10",
                description = "Learn how to turn worries into petitions with thanksgiving, experiencing the supernatural peace of God that surpasses human comprehension.",
                sermonNotes = "Points:\n1. Be anxious for nothing: Cast your burdens onto Him.\n2. In everything by prayer and supplication with thanksgiving.\n3. The peace of God guards your heart and mind.",
                viewCount = 5410,
                uploadedAt = "1 week ago"
            )
        )
    )
    val videos: StateFlow<List<MinistryVideo>> = _videos.asStateFlow()

    // --- Live Preaching & Preaching Sessions (Google Meet Style) ---
    private val _liveSessions = MutableStateFlow<List<LivePreachingSession>>(
        listOf(
            LivePreachingSession(
                id = "live-1",
                title = "Global Miracle & Healing Service - The God of Peace",
                preacherName = "Apostle David K. Mensah",
                ministryRole = "Senior Pastor & Evangelist",
                scriptureTheme = "Isaiah 9:6 & John 14:27",
                isLive = true,
                viewerCount = 482,
                startTimeText = "Live (24 min in)",
                roomCode = "shalom-sanctuary-live"
            ),
            LivePreachingSession(
                id = "live-2",
                title = "Evening Youth Fellowship & Apologetics Q&A",
                preacherName = "Pastor Caleb Faith",
                ministryRole = "Youth Outreach Director",
                scriptureTheme = "1 Peter 3:15",
                isLive = false,
                viewerCount = 0,
                startTimeText = "Starting at 7:00 PM EST",
                roomCode = "youth-fire-fellowship"
            )
        )
    )
    val liveSessions: StateFlow<List<LivePreachingSession>> = _liveSessions.asStateFlow()

    // In-room live comments
    private val _liveComments = MutableStateFlow<List<LiveComment>>(
        listOf(
            LiveComment(senderName = "Sister Hannah (Nairobi)", text = "Amen! Hallelujah to the King of Kings!", isAmen = true),
            LiveComment(senderName = "Brother Mark (London)", text = "Praying for family restoration right now pastor.", isAmen = false),
            LiveComment(senderName = "Grace O. (Chicago)", text = "The Word is ministering so deeply to my heart today.", isAmen = false),
            LiveComment(senderName = "Elder Samuel", text = "Amen and Amen! The peace of God passes all understanding!", isAmen = true)
        )
    )
    val liveComments: StateFlow<List<LiveComment>> = _liveComments.asStateFlow()

    // --- Fellowship Community Chats ---
    private val _chats = MutableStateFlow<List<FellowshipChat>>(
        listOf(
            FellowshipChat(
                id = "chat-1",
                name = "Global Prayer Intercessors",
                isChannel = true,
                unreadCount = 2,
                lastMessageText = "Please join us in intercession for the global missions outreach tonight at 8 PM.",
                lastMessageTime = "11:15 AM",
                membersCount = 1420,
                avatarInitials = "GP"
            ),
            FellowshipChat(
                id = "chat-2",
                name = "Pastor Grace Sterling",
                isChannel = false,
                unreadCount = 1,
                lastMessageText = "Voice note: Morning pastoral blessing (0:42)",
                lastMessageTime = "10:45 AM",
                membersCount = 2,
                avatarInitials = "PG"
            ),
            FellowshipChat(
                id = "chat-3",
                name = "Worship & Praise Team",
                isChannel = true,
                unreadCount = 0,
                lastMessageText = "File shared: Sunday_Order_Of_Worship_Songsheets.pdf",
                lastMessageTime = "Yesterday",
                membersCount = 38,
                avatarInitials = "WT"
            ),
            FellowshipChat(
                id = "chat-4",
                name = "Brother Caleb Mensah",
                isChannel = false,
                unreadCount = 0,
                lastMessageText = "Amen brother, God bless you for sending that scripture!",
                lastMessageTime = "Yesterday",
                membersCount = 2,
                avatarInitials = "BC"
            )
        )
    )
    val chats: StateFlow<List<FellowshipChat>> = _chats.asStateFlow()

    // Chat messages by chatId
    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "chat-1" to listOf(
                ChatMessage(
                    id = "msg-1-1",
                    chatId = "chat-1",
                    senderName = "Evangelist Daniel",
                    senderRole = "Missionary",
                    isFromMe = false,
                    messageType = MessageType.TEXT,
                    text = "Peace be unto all the brethren in Christ. Let us lift up our missionaries laboring in rural regions today.",
                    timestamp = "10:15 AM"
                ),
                ChatMessage(
                    id = "msg-1-2",
                    chatId = "chat-1",
                    senderName = "Evangelist Daniel",
                    senderRole = "Missionary",
                    isFromMe = false,
                    messageType = MessageType.BIBLE_VERSE,
                    text = "“How beautiful upon the mountains are the feet of him that bringeth good tidings, that publisheth peace; that bringeth good tidings of good, that publisheth salvation!”",
                    timestamp = "10:16 AM",
                    scriptureRef = "Isaiah 52:7"
                ),
                ChatMessage(
                    id = "msg-1-3",
                    chatId = "chat-1",
                    senderName = "You",
                    senderRole = "Outreach Leader",
                    isFromMe = true,
                    messageType = MessageType.TEXT,
                    text = "Amen! Our team is standing in agreement. The harvest is plentiful and the Lord provides strength.",
                    timestamp = "10:42 AM"
                ),
                ChatMessage(
                    id = "msg-1-4",
                    chatId = "chat-1",
                    senderName = "Sister Deborah",
                    senderRole = "Prayer Warrior",
                    isFromMe = false,
                    messageType = MessageType.VOICE_NOTE,
                    text = "Voice Prayer: Intercession for the nations",
                    timestamp = "11:10 AM",
                    voiceDurationSeconds = 38
                ),
                ChatMessage(
                    id = "msg-1-5",
                    chatId = "chat-1",
                    senderName = "Sister Deborah",
                    senderRole = "Prayer Warrior",
                    isFromMe = false,
                    messageType = MessageType.TEXT,
                    text = "Please join us in intercession for the global missions outreach tonight at 8 PM.",
                    timestamp = "11:15 AM"
                )
            ),
            "chat-2" to listOf(
                ChatMessage(
                    id = "msg-2-1",
                    chatId = "chat-2",
                    senderName = "Pastor Grace Sterling",
                    senderRole = "Senior Pastor",
                    isFromMe = false,
                    messageType = MessageType.TEXT,
                    text = "Grace and peace to you, beloved! Thank you for facilitating the audio devotionals for our outreach.",
                    timestamp = "10:30 AM"
                ),
                ChatMessage(
                    id = "msg-2-2",
                    chatId = "chat-2",
                    senderName = "Pastor Grace Sterling",
                    senderRole = "Senior Pastor",
                    isFromMe = false,
                    messageType = MessageType.VOICE_NOTE,
                    text = "Voice note: Morning pastoral blessing",
                    timestamp = "10:45 AM",
                    voiceDurationSeconds = 42
                )
            ),
            "chat-3" to listOf(
                ChatMessage(
                    id = "msg-3-1",
                    chatId = "chat-3",
                    senderName = "Choir Director Paul",
                    senderRole = "Worship Leader",
                    isFromMe = false,
                    messageType = MessageType.FILE_ATTACHMENT,
                    text = "Here is the finalized music sheet for this weekend's worship encounter.",
                    timestamp = "Yesterday",
                    fileName = "Sunday_Order_Of_Worship_Songsheets.pdf",
                    fileSize = "2.4 MB"
                )
            )
        )
    )
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    // --- Spiritual Statuses / Stories ---
    private val _statuses = MutableStateFlow<List<MinistryStatus>>(
        listOf(
            MinistryStatus(
                id = "status-1",
                authorName = "Pastor Emmanuel",
                authorRole = "Revivalist",
                timeAgo = "1 hour ago",
                textContent = "“The steadfast love of the Lord never ceases; His mercies never come to an end; they are new every morning; great is Your faithfulness!”",
                scriptureVerse = "Lamentations 3:22-23",
                hasPhoto = false
            ),
            MinistryStatus(
                id = "status-2",
                authorName = "Sister Sarah Joy",
                authorRole = "Praise Singer",
                timeAgo = "3 hours ago",
                textContent = "Praise Report! We prayed for our brother in the hospital and the doctor declared a total turnaround today. All glory to Jesus!",
                scriptureVerse = "Psalm 103:2-3",
                hasPhoto = true
            ),
            MinistryStatus(
                id = "status-3",
                authorName = "Outreach Evangelism Team",
                authorRole = "Missions",
                timeAgo = "5 hours ago",
                textContent = "Over 120 souls gave their lives to Christ today in our community food & Gospel distribution crusade! Continue to keep them in your prayers.",
                scriptureVerse = "Luke 15:10",
                hasPhoto = true
            )
        )
    )
    val statuses: StateFlow<List<MinistryStatus>> = _statuses.asStateFlow()

    // --- User Settings ---
    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    // --- Actions ---

    fun addPrayerAudio(prayer: AudioPrayer) {
        _prayers.value = listOf(prayer) + _prayers.value
    }

    fun togglePrayerFavorite(prayerId: String) {
        _prayers.value = _prayers.value.map {
            if (it.id == prayerId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun addWorshipSong(song: WorshipSong) {
        _worshipSongs.value = listOf(song) + _worshipSongs.value
    }

    fun toggleWorshipFavorite(songId: String) {
        _worshipSongs.value = _worshipSongs.value.map {
            if (it.id == songId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun addMinistryVideo(video: MinistryVideo) {
        _videos.value = listOf(video) + _videos.value
    }

    fun addLiveComment(comment: LiveComment) {
        _liveComments.value = _liveComments.value + comment
    }

    fun createLivePreachingSession(title: String, scripture: String): LivePreachingSession {
        val session = LivePreachingSession(
            id = UUID.randomUUID().toString(),
            title = title,
            preacherName = _settings.value.userName,
            ministryRole = _settings.value.userRole,
            scriptureTheme = scripture,
            isLive = true,
            viewerCount = 1,
            startTimeText = "Just started",
            roomCode = "shalom-${System.currentTimeMillis() % 10000}"
        )
        _liveSessions.value = listOf(session) + _liveSessions.value
        return session
    }

    fun sendMessage(
        chatId: String,
        type: MessageType,
        text: String,
        voiceDurationSeconds: Int = 0,
        fileName: String? = null,
        fileSize: String? = null,
        scriptureRef: String? = null
    ) {
        val newMsg = ChatMessage(
            chatId = chatId,
            senderName = "You",
            senderRole = _settings.value.userRole,
            isFromMe = true,
            messageType = type,
            text = text,
            timestamp = "Just now",
            voiceDurationSeconds = voiceDurationSeconds,
            fileName = fileName,
            fileSize = fileSize,
            scriptureRef = scriptureRef
        )

        val currentList = _chatMessages.value[chatId] ?: emptyList()
        _chatMessages.value = _chatMessages.value + (chatId to (currentList + newMsg))

        // Update last message in chat list
        _chats.value = _chats.value.map {
            if (it.id == chatId) {
                val preview = when (type) {
                    MessageType.TEXT -> text
                    MessageType.VOICE_NOTE -> "Voice note (${voiceDurationSeconds}s)"
                    MessageType.FILE_ATTACHMENT -> "File: ${fileName ?: "attachment"}"
                    MessageType.BIBLE_VERSE -> "Scripture: ${scriptureRef ?: text}"
                }
                it.copy(lastMessageText = preview, lastMessageTime = "Just now")
            } else it
        }
    }

    fun addStatus(status: MinistryStatus) {
        _statuses.value = listOf(status) + _statuses.value
    }

    fun updateSettings(newSettings: UserSettings) {
        _settings.value = newSettings
    }
}
