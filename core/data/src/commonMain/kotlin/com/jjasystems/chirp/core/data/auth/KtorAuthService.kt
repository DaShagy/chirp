package com.jjasystems.chirp.core.data.auth

import com.jjasystems.chirp.core.data.dto.request.EmailRequest
import com.jjasystems.chirp.core.data.dto.request.RegisterRequest
import com.jjasystems.chirp.core.data.networking.post
import com.jjasystems.chirp.core.domain.auth.AuthService
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(
                email = email
            )
        )
    }
}