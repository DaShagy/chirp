package com.jjasystems.chirp.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jjasystems.chirp.chat.database.entity.ChatEntity
import com.jjasystems.chirp.chat.database.entity.ChatInfoEntity
import com.jjasystems.chirp.chat.database.entity.ChatMessageEntity
import com.jjasystems.chirp.chat.database.entity.ChatParticipantCrossRef
import com.jjasystems.chirp.chat.database.entity.ChatParticipantEntity
import com.jjasystems.chirp.chat.database.entity.ChatWithParticipants
import com.jjasystems.chirp.chat.database.view.LastMessageView
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
    suspend fun upsertChat(chatEntity: ChatEntity)

    @Upsert
    suspend fun upsertChats(chatEntity: List<ChatEntity>)

    @Query("""
        DELETE FROM chatentity WHERE chatId = :chatId
    """)
    suspend fun deleteChatById(chatId: String)

    @Query("""
        SELECT * FROM chatentity ORDER BY lastActivityAt DESC
    """)
    @Transaction
    fun getChatsWithParticipants(): Flow<List<ChatWithParticipants>>

    @Query("""
        SELECT DISTINCT c.* 
        FROM chatentity c 
        JOIN chatparticipantcrossref cpcr ON c.chatId = cpcr.chatId
        WHERE cpcr.isActive = 1
        ORDER BY lastActivityAt DESC
    """)
    @Transaction
    fun getChatsWithActiveParticipants(): Flow<List<ChatWithParticipants>>

    @Query("""
        SELECT * FROM chatentity WHERE chatId = :id
    """)
    @Transaction
    suspend fun getChatById(id: String): ChatWithParticipants?

    @Query("""
        DELETE FROM chatentity
    """)
    suspend fun deleteAllChats()

    @Query("""
        SELECT chatId FROM chatentity
    """)
    suspend fun getAllChatIds(): List<String>

    @Transaction
    suspend fun deleteChatsByIds(chatIds: List<String>) {
        chatIds.forEach { chatId ->
            deleteChatById(chatId)
        }
    }

    @Query("""
        SELECT COUNT(*) FROM chatentity
    """)
    fun getChatCount(): Flow<Int>

    @Query("""
        SELECT p.*
        FROM chatparticipantentity p
        JOIN chatparticipantcrossref cpcr ON p.userId = cpcr.userId
        WHERE cpcr.chatId = :chatId AND cpcr.isActive = true
        ORDER BY p.username
    """)
    @Transaction
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipantEntity>>

    @Query("""
        SELECT c.* 
        FROM chatentity c
        JOIN chatparticipantcrossref cpcr ON c.chatId = cpcr.chatId
        WHERE c.chatId = :chatId AND cpcr.isActive = true
    """)
    @Transaction
    fun getChatInfoById(chatId: String): Flow<ChatInfoEntity?>

    @Transaction
    suspend fun upsertChatWithParticipantsAndCrossRefs(
        chat: ChatEntity,
        participants: List<ChatParticipantEntity>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao
    ) {
        upsertChat(chat)
        participantDao.upsertParticipants(participants)

        val crossRefs = participants.map { participant ->
            ChatParticipantCrossRef(
                chatId = chat.chatId,
                userId = participant.userId,
                isActive = true
            )
        }
        crossRefDao.upsertCrossRefs(crossRefs)
        crossRefDao.syncChatParticipants(chat.chatId, participants)
    }

    @Transaction
    suspend fun upsertChatsWithParticipantsAndCrossRefs(
        chats: List<ChatWithParticipants>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao,
        messageDao: ChatMessageDao
    ) {
        upsertChats(chats.map { it.chat })

        val localChatIds = getAllChatIds()
        val serverChatIds = chats.map { it.chat.chatId }
        val staleChatIds = localChatIds - serverChatIds

        chats.forEach { chat ->
            chat.lastMessage?.run {
                messageDao.upsertMessage(
                    ChatMessageEntity(
                        messageId = messageId,
                        chatId = chatId,
                        content = content,
                        timestamp = timestamp,
                        senderId = senderId,
                        deliveryStatus = deliveryStatus
                    )
                )
            }
        }

        val allParticipants = chats.flatMap { it.participants }
        participantDao.upsertParticipants(allParticipants)

        val allCrossRefs = chats.flatMap { chatWithParticipants ->
            chatWithParticipants.participants.map { participant ->
                ChatParticipantCrossRef(
                    chatId = chatWithParticipants.chat.chatId,
                    userId = participant.userId,
                    isActive = true
                )
            }
        }
        crossRefDao.upsertCrossRefs(allCrossRefs)

        chats.forEach { chat ->
            crossRefDao.syncChatParticipants(
                chatId = chat.chat.chatId,
                participants = chat.participants
            )
        }

        deleteChatsByIds(staleChatIds)
    }
}