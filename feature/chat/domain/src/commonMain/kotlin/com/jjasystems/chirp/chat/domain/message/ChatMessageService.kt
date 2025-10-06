package com.jjasystems.chirp.chat.domain.message

import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result

interface ChatMessageService {
    suspend fun fetchMessages(
        chatId: String,
        before: String? = null
    ): Result<List<ChatMessage>, DataError.Remote>

    suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote>
}