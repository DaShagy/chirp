package com.jjasystems.chirp.chat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jjasystems.chirp.chat.database.dao.ChatDao
import com.jjasystems.chirp.chat.database.dao.ChatMessageDao
import com.jjasystems.chirp.chat.database.dao.ChatParticipantDao
import com.jjasystems.chirp.chat.database.dao.ChatParticipantsCrossRefDao
import com.jjasystems.chirp.chat.database.entity.ChatEntity
import com.jjasystems.chirp.chat.database.entity.ChatMessageEntity
import com.jjasystems.chirp.chat.database.entity.ChatParticipantCrossRef
import com.jjasystems.chirp.chat.database.entity.ChatParticipantEntity
import com.jjasystems.chirp.chat.database.view.LastMessageView

@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class
    ],
    views = [
        LastMessageView::class
    ],
    version = 1
)
abstract class ChirpChatDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "chirp.db"
    }
}