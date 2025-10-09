package com.jjasystems.chirp.chat.domain.chat

import com.jjasystems.chirp.chat.domain.model.Chat
import com.jjasystems.chirp.chat.domain.model.ChatInfo
import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    fun getChatInfoById(chatId: String): Flow<ChatInfo>
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipant>>
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>
    suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote>
    suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote>
    suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote>
    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote>
    suspend fun deleteAllChats()
}