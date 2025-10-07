package com.jjasystems.chirp.chat.data.participant

import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.chat.domain.participant.ChatParticipantRepository
import com.jjasystems.chirp.chat.domain.participant.ChatParticipantService
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
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

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote> {
        val result = chatParticipantService.getProfilePictureUploadUrl(mimeType)

        if(result is Result.Failure) {
            return result
        }

        val uploadUrl = (result as Result.Success).data

        val uploadResult = chatParticipantService.uploadProfilePicture(
            uploadUrl = uploadUrl.uploadUrl,
            imageBytes = imageBytes,
            headers = uploadUrl.headers
        )

        if(uploadResult is Result.Failure) {
            return uploadResult
        }

        return chatParticipantService
            .confirmProfilePictureUpload(uploadUrl.publicUrl)
            .onSuccess { url ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            profilePictureUrl = uploadUrl.publicUrl
                        )
                    )
                )
            }
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return chatParticipantService
            .deleteProfilePicture()
            .onSuccess { url ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            profilePictureUrl = null
                        )
                    )
                )
            }
    }
}