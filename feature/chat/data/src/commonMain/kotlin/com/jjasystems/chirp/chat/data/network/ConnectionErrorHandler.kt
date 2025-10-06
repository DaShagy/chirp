package com.jjasystems.chirp.chat.data.network

import com.jjasystems.chirp.chat.domain.model.ConnectionState

expect class ConnectionErrorHandler {
    fun getConnectionStateForError(cause: Throwable): ConnectionState
    fun transformException(exception: Throwable): Throwable
    fun isRetriableError(cause: Throwable): Boolean
}