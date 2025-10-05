package com.jjasystems.chirp.chat.data.chat

import com.jjasystems.chirp.chat.data.dto.ChatSerializable
import com.jjasystems.chirp.chat.data.dto.request.CreateChatRequest
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.domain.chat.ChatService
import com.jjasystems.chirp.chat.domain.model.Chat
import com.jjasystems.chirp.core.data.networking.get
import com.jjasystems.chirp.core.data.networking.post
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatService(
    private val httpClient: HttpClient
): ChatService {
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatSerializable>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map { it.toDomain() }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatSerializable>>(
            route = "/chat"
        ).map { chats ->
            chats.map { it.toDomain() }
        }
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.get<ChatSerializable>(
            route = "/chat/$chatId"
        ).map { it.toDomain() }
    }
}