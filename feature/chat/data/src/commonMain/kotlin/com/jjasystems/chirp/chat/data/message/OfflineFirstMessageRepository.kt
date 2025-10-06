package com.jjasystems.chirp.chat.data.message

import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.data.mapper.toEntity
import com.jjasystems.chirp.chat.database.ChirpChatDatabase
import com.jjasystems.chirp.chat.domain.message.ChatMessageService
import com.jjasystems.chirp.chat.domain.message.MessageRepository
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.chat.domain.model.MessageWithSender
import com.jjasystems.chirp.core.data.database.safeDatabaseUpdate
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val db: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService
) : MessageRepository {

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            db.chatMessageDao.updaterDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    db.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = messages.map { it.toEntity() },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null // Only sync for most recent page
                    )
                    messages
                }
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return db
            .chatMessageDao
            .getMessagesByChatId(chatId)
            .map { messages ->
                messages.map { it.toDomain() }
            }
    }
}