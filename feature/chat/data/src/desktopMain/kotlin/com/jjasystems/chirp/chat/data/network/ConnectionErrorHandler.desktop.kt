package com.jjasystems.chirp.chat.data.network

import com.jjasystems.chirp.chat.domain.model.ConnectionState

actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return ConnectionState.NETWORK_ERROR
    }

    actual fun transformException(exception: Throwable): Throwable {
        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        return true
    }
}