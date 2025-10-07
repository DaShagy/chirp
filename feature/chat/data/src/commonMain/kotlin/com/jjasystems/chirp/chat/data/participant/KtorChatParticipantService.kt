package com.jjasystems.chirp.chat.data.participant

import com.jjasystems.chirp.chat.data.dto.ChatParticipantSerializable
import com.jjasystems.chirp.chat.data.dto.request.ConfirmProfilePictureRequest
import com.jjasystems.chirp.chat.data.dto.response.ProfilePictureUploadUrlResponse
import com.jjasystems.chirp.chat.data.mapper.toDomain
import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.chat.domain.model.ProfilePictureUploadUrl
import com.jjasystems.chirp.chat.domain.participant.ChatParticipantService
import com.jjasystems.chirp.core.data.network.delete
import com.jjasystems.chirp.core.data.network.get
import com.jjasystems.chirp.core.data.network.post
import com.jjasystems.chirp.core.data.network.safeCall
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlin.collections.component1
import kotlin.collections.component2

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

    override suspend fun getLocalParticipant(): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantSerializable>(
            route = "/participants",
        ).map { it.toDomain() }
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrl, DataError.Remote> {
        return httpClient.post<Unit, ProfilePictureUploadUrlResponse>(
            route = "/participants/profile-picture-upload",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit
        ).map { it.toDomain() }
    }

    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return safeCall {
            httpClient.put {
                url(uploadUrl)
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                setBody(imageBytes)
            }
        }
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return httpClient.post<ConfirmProfilePictureRequest, Unit>(
            route = "/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(publicUrl)
        )
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/participants/profile-picture"
        )
    }
}