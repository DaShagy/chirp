package com.jjasystems.chirp.chat.data.message

import com.jjasystems.chirp.chat.data.dto.websocket.OutgoingWebSocketDto
import com.jjasystems.chirp.chat.data.dto.websocket.OutgoingWebSocketType
import com.jjasystems.chirp.chat.data.dto.websocket.WebSocketMessageDto
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.data.mapper.toEntity
import com.jjasystems.chirp.chat.data.mapper.toWebSocketDto
import com.jjasystems.chirp.chat.data.network.KtorWebSocketConnector
import com.jjasystems.chirp.chat.database.ChirpChatDatabase
import com.jjasystems.chirp.chat.domain.message.ChatMessageService
import com.jjasystems.chirp.chat.domain.message.MessageRepository
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.chat.domain.model.MessageWithSender
import com.jjasystems.chirp.chat.domain.model.OutgoingNewMessage
import com.jjasystems.chirp.core.data.database.safeDatabaseUpdate
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.onFailure
import com.jjasystems.chirp.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val db: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
) : MessageRepository {

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            db.chatMessageDao.updateDeliveryStatus(
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

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSocketDto()
            val localUser = sessionStorage.observeAuthInfo().first()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            val entity = dto.toEntity(
                senderId = localUser.id,
                deliveryStatus = ChatMessageDeliveryStatus.SENDING
            )

            db.chatMessageDao.upsertMessage(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        db.chatMessageDao.updateDeliveryStatus(
                            messageId = entity.messageId,
                            status = ChatMessageDeliveryStatus.FAILED.name,
                            timestamp = Clock.System.now().toEpochMilliseconds()
                        )
                    }.join()
                }
        }
    }

    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val message = db.chatMessageDao.getMessageById(messageId)
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            db.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                status = ChatMessageDeliveryStatus.SENDING.name
            )

            val outgoingNewMessage = OutgoingWebSocketDto.NewMessage(
                chatId = message.chatId,
                messageId = messageId,
                content = message.content
            )

            return webSocketConnector
                .sendMessage(outgoingNewMessage.toJsonPayload())
                .onFailure {
                    applicationScope.launch {
                        db.chatMessageDao.updateDeliveryStatus(
                            messageId = messageId,
                            status = ChatMessageDeliveryStatus.FAILED.name,
                            timestamp = Clock.System.now().toEpochMilliseconds()
                        )
                    }.join()
                }
        }
    }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError> {
        return chatMessageService
            .deleteMessage(messageId)
            .onSuccess {
                applicationScope.launch {
                    db.chatMessageDao.deleteMessageById(messageId)
                }.join()
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

    private fun OutgoingWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = OutgoingWebSocketType.NEW_MESSAGE.name,
            payload = json.encodeToString(this)
        )

        return json.encodeToString(webSocketMessage)
    }
}