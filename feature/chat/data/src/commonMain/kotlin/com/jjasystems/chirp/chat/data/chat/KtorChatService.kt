package com.jjasystems.chirp.chat.data.chat

import com.jjasystems.chirp.chat.data.dto.ChatSerializable
import com.jjasystems.chirp.chat.data.dto.request.CreateChatRequest
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.domain.chat.ChatService
import com.jjasystems.chirp.chat.domain.model.Chat
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
}