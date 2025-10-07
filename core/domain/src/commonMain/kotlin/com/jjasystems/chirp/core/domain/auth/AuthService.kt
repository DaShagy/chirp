package com.jjasystems.chirp.core.domain.auth

import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult
import com.jjasystems.chirp.core.domain.util.Result

interface AuthService {
    suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote>
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote>

    suspend fun forgotPassword(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote>

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote>
}