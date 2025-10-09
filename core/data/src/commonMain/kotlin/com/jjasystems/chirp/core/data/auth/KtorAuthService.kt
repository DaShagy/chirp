package com.jjasystems.chirp.core.data.auth

import com.jjasystems.chirp.core.data.dto.AuthInfoSerializable
import com.jjasystems.chirp.core.data.dto.request.ChangePasswordRequest
import com.jjasystems.chirp.core.data.dto.request.EmailRequest
import com.jjasystems.chirp.core.data.dto.request.LoginRequest
import com.jjasystems.chirp.core.data.dto.request.RefreshRequest
import com.jjasystems.chirp.core.data.dto.request.RegisterRequest
import com.jjasystems.chirp.core.data.dto.request.ResetPasswordRequest
import com.jjasystems.chirp.core.data.mapper.toDomain
import com.jjasystems.chirp.core.data.network.get
import com.jjasystems.chirp.core.data.network.post
import com.jjasystems.chirp.core.domain.auth.AuthInfo
import com.jjasystems.chirp.core.domain.auth.AuthService
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result
import com.jjasystems.chirp.core.domain.util.map
import com.jjasystems.chirp.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { authInfoSerializable ->
            authInfoSerializable.toDomain()
        }
    }

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

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/forgot-password",
            body = EmailRequest(
                email = email
            )
        )
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/reset-password",
            body = ResetPasswordRequest(
                newPassword = newPassword,
                token = token
            )
        )
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/change-password",
            body = ChangePasswordRequest(
                oldPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return httpClient.post<RefreshRequest, Unit>(
            route = "/auth/logout",
            body = RefreshRequest(
                refreshToken = refreshToken
            )
        ).onSuccess {
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }
    }
}