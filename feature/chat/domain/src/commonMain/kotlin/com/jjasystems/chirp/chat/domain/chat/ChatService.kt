package com.jjasystems.chirp.chat.domain.chat

import com.jjasystems.chirp.chat.domain.model.Chat
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result

interface ChatService {
    suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote>
}