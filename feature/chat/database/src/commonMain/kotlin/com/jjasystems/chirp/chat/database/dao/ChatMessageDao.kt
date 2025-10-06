package com.jjasystems.chirp.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jjasystems.chirp.chat.database.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity)

    @Upsert
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    @Query("""
        DELETE FROM chatmessageentity WHERE messageId = :messageId
    """)
    suspend fun deleteMessageById(messageId: String)

    @Query("""
        DELETE FROM chatmessageentity WHERE messageId IN (:messageIds)
    """)
    suspend fun deleteMessagesById(messageIds: List<String>)

    @Query("""
        SELECT * FROM chatmessageentity WHERE chatId = :chatId ORDER BY timestamp DESC
    """)
    @Transaction
    fun getMessagesByChatId(chatId: String): Flow<List<ChatMessageEntity>>

    @Query("""
        SELECT * FROM chatmessageentity WHERE messageId = :messageId
    """)
    suspend fun getMessageById(messageId: String): ChatMessageEntity?

    @Query("""
        UPDATE chatmessageentity
        SET deliveryStatus = :status, deliveryStatusTimestamp = :timestamp
        WHERE messageId = :messageId
    """)
    suspend fun updaterDeliveryStatus(messageId: String, status: String, timestamp: Long)
}