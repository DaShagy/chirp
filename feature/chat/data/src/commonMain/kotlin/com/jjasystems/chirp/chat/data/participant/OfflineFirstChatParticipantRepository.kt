package com.jjasystems.chirp.chat.data.participant

import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.chat.domain.participant.ChatParticipantRepository
import com.jjasystems.chirp.chat.domain.participant.ChatParticipantService
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.onSuccess
import kotlinx.coroutines.flow.first

class OfflineFirstChatParticipantRepository(
    private val sessionStorage: SessionStorage,
    private val chatParticipantService: ChatParticipantService
): ChatParticipantRepository {

    override suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError> {
        return chatParticipantService
            .getLocalParticipant()
            .onSuccess { participant ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl
                        )
                    )
                )
            }
    }
}