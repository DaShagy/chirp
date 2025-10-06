package com.jjasystems.chirp.chat.data.chat

import com.jjasystems.chirp.chat.data.dto.websocket.IncomingWebSocketDto
import com.jjasystems.chirp.chat.data.dto.websocket.IncomingWebSocketType
import com.jjasystems.chirp.chat.data.dto.websocket.WebSocketMessageDto
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.data.mapper.toEntity
import com.jjasystems.chirp.chat.data.network.KtorWebSocketConnector
import com.jjasystems.chirp.chat.database.ChirpChatDatabase
import com.jjasystems.chirp.chat.domain.chat.ChatConnectionClient
import com.jjasystems.chirp.chat.domain.chat.ChatRepository
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class WebSocketChatConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val db: ChirpChatDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    applicationScope: CoroutineScope
): ChatConnectionClient {

    override val chatMessages: Flow<ChatMessage> = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(it) }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingWebSocketDto.NewMessageDto>()
        .mapNotNull {
            db.chatMessageDao.getMessageById(it.id)?.toDomain()
        }
        .shareIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5_000L)
        )

    override val connectionState = webSocketConnector.connectionState

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingWebSocketDto? {
        return when (message.type) {
            IncomingWebSocketType.NEW_MESSAGE.name -> {
                json.decodeFromString<IncomingWebSocketDto.NewMessageDto>(message.payload)
            }
            IncomingWebSocketType.MESSAGE_DELETED.name -> {
                json.decodeFromString<IncomingWebSocketDto.MessageDeletedDto>(message.payload)
            }
            IncomingWebSocketType.PROFILE_PICTURE_UPDATED.name -> {
                json.decodeFromString<IncomingWebSocketDto.ProfilePictureUpdatedDto>(message.payload)
            }
            IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingWebSocketDto.ChatParticipantsChangedDto>(message.payload)
            }
            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingWebSocketDto) {
        when(message) {
            is IncomingWebSocketDto.ChatParticipantsChangedDto -> refreshChat(message)
            is IncomingWebSocketDto.MessageDeletedDto -> deleteMessage(message)
            is IncomingWebSocketDto.NewMessageDto -> handleNewMessage(message)
            is IncomingWebSocketDto.ProfilePictureUpdatedDto -> updateProfilePicture(message)
        }
    }

    private suspend fun refreshChat(message: IncomingWebSocketDto.ChatParticipantsChangedDto) {
        chatRepository.fetchChatById(message.chatId)
    }

    private suspend fun deleteMessage(message: IncomingWebSocketDto.MessageDeletedDto) {
        db.chatMessageDao.deleteMessageById(message.id)
    }

    private suspend fun handleNewMessage(message: IncomingWebSocketDto.NewMessageDto) {
        val chatExists = db.chatDao.getChatById(message.chatId) != null

        if(!chatExists) {
            chatRepository.fetchChatById(message.chatId)
        }

        val entity = message.toEntity()
        db.chatMessageDao.upsertMessage(entity)
    }

    private suspend fun updateProfilePicture(message: IncomingWebSocketDto.ProfilePictureUpdatedDto) {
        db.chatParticipantDao.updateProfilePictureUrl(
            userId = message.userId,
            newUrl = message.newUrl
        )

        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

        if(authInfo != null) {
            sessionStorage.set(
                info = authInfo.copy(
                    user = authInfo.user.copy(
                        profilePictureUrl = message.newUrl
                    )
                )
            )
        }
    }
}