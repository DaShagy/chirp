package com.jjasystems.chirp.chat.data.message

import com.jjasystems.chirp.chat.data.dto.ChatMessageSerializable
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.domain.message.ChatMessageService
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.core.data.network.get
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatMessageService(
    private val httpClient: HttpClient
): ChatMessageService {

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError.Remote> {
        return httpClient.get<List<ChatMessageSerializable>>(
            route = "/chat/$chatId/messages",
            queryParams = buildMap {
                this["pageSize"] = ChatMessageConstants.PAGE_SIZE
                if(before != null) {
                    this["before"] = before
                }
            }
        ).map { it.map { it.toDomain()} }
    }
}