package com.jjasystems.chirp.chat.data.notification

import com.jjasystems.chirp.chat.data.dto.request.RegisterDeviceTokenRequest
import com.jjasystems.chirp.chat.domain.notification.DeviceTokenService
import com.jjasystems.chirp.core.data.network.delete
import com.jjasystems.chirp.core.data.network.post
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorDeviceTokenService(
    private val httpClient: HttpClient
): DeviceTokenService {
    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform
            )
        )
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/notification/$token"
        )
    }
}