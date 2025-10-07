package com.jjasystems.chirp.chat.domain.participant

import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result

interface ChatParticipantRepository {
    suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError>
}