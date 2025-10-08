package com.jjasystems.chirp.chat.domain.notification

import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.EmptyResult

interface DeviceTokenService {
    suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote>

    suspend fun unregisterToken(
        token: String
    ): EmptyResult<DataError.Remote>
}