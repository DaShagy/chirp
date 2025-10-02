package com.jjasystems.chirp.chat.domain.chat

import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result

interface ChatParticipantService {
    suspend fun searchParticipant(
        query: String
    ): Result<ChatParticipant, DataError.Remote>
}