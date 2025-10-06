package com.jjasystems.chirp.chat.data.chat

import com.jjasystems.chirp.chat.data.dto.ChatParticipantSerializable
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.domain.chat.ChatParticipantService
import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.core.data.network.get
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatParticipantService(
    private val httpClient: HttpClient
): ChatParticipantService {

    override suspend fun searchParticipant(query: String): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantSerializable>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { it.toDomain() }
    }
}